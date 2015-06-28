<?php
// @import
require_once("MyLog.php");
require_once("etc/AppName.php");
require_once("etc/ServerName.php");
require_once("etc/ZooKeeper.php");

/**
 * Zookeeper 服务
 *
 * @auth jinhaijiang
 * @since 2015/6/28
 *
 */
class ZkServ extends Zookeeper {
    /**
     * 应用名称
     *
     * @var String
     *
     */
    public $_appName;

    /**
     * 服务器名称
     *
     * @var String
     *
     */
    public $_serverName;

    /**
     * ZooKeeper 路径字典
     *
     * @var Array
     *
     */
    private $_zkPathMap;

    /**
     * 启动服务
     *
     */
    public function startUp() {
        // 获取应用名称和服务器名称
        $appName = $this->_appName;
        $serverName = $this->_serverName;

        // 初始化路径字典
        $this->_zkPathMap = array(
            "/${appName}/${serverName}/conf/maintenanceTimeStr" => "updateMaintenanceTime",
            "/${appName}/${serverName}/conf/whiteList" => "updateWhiteList",
        );

        foreach ($this->_zkPathMap as $key => $val) {
            // 输出调试日志
            MyLog::LOG()->debug("准备监听 : $key");
            // 设置监听
            $this->get($key, array($this, "watch"));
        }
    }

    /**
     * 监听数据变化
     *
     * @param $eventType
     * @param $eventState
     * @param $eventKey
     *
     */
    public function watch($eventType, $eventState, $eventKey) {
        // 记录日志信息
        MyLog::LOG()->info("接到数据");
        // 输出调试信息
        MyLog::LOG()->debug("eventType = ${eventType}, eventState = ${eventState}, eventKey = ${eventKey}");

        // 获取数据
        $data = $this->get($eventKey);
        // 获取函数引用并调用
        $funcRef = $this->_zkPathMap[$eventKey];
        $this->$funcRef($data);

        // 循环监听
        $this->get($eventKey, array($this, "watch"));
    }

    /**
     * 更新停服维护时间
     *
     * @param String $value
     * @return void
     *
     */
    private function updateMaintenanceTime($value) {
        // 记录日志信息
        MyLog::LOG()->info("维护时间 = $value");
        // 获取 JSON 数组
        $jsonArr = json_decode($value);

        $startTimeStr = $jsonArr[0];
        $endTimeStr = $jsonArr[1];

        // 目标文件
        $targetFile = dirname(__FILE__) . "/etc/MaintenanceTime.php";
        // 打开文件
        $fp = fopen($targetFile, "w");

        if (!$fp) {
            // 如果打开文件失败,
            // 则直接退出!
            MyLog::LOG()->error("打开文件 ${targetFile} 失败!!");
            return;
        }

        $text = <<< __EOF
<?php
\$GLOBALS["MAINTENANCE_START_TIME"] = $startTimeStr;
\$GLOBALS["MAINTENANCE_END_TIME"] = $endTimeStr;

__EOF;

        // 写出文件内容
        $result = fwrite($fp, $text);

        if (!$result) {
            // 如果写出文件失败,
            // 则记录错误日志
            MyLog::LOG()->error(
                "写出文件 ${targetFile} 失败!!"
            );
        }

        fflush($fp);
        fclose($fp);

        // 输出日志信息
        MyLog::LOG()->info("文件 ${targetFile} 已更新");
    }

    /**
     * 更新白名单
     *
     * @return void
     *
     */
    private function updateWhiteList() {
        // 获取白名单
        $whiteList = $this->get("whiteList");
    }
}

// 获取服务器名称
$appName = $GLOBALS["APP_NAME"];
$serverName = $GLOBALS["SERVER_NAME"];

// ZooKeeper 配置
$zkHost = $GLOBALS["ZK_SERVER_HOST"];
$zkPort = $GLOBALS["ZK_SERVER_PORT"];

// 创建服务对象
$servObj = new ZkServ("${zkHost}:${zkPort}");
$servObj->_appName = $appName;
$servObj->_serverName = $serverName;
// 启动服务
$servObj->startUp();

while (true) {
    MyLog::LOG()->info("live");
    sleep(60);
}

