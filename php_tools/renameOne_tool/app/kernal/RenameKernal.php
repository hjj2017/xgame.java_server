<?php
// @define
define("__WORKFLOW_DIR", __DIR__ . "/../../workFlow");
// @import
require_once(__DIR__ . "/../MyLog.php");
require_once(__DIR__ . "/../util/FileUtil.php");
require_once(__DIR__ . "/Kernal_RenameByHuman.php");

/**
 * 合服工具核心类
 *
 * @author jinhaijiang
 * @since 2014/12/19
 *
 */
class RenameKernal {
    /**
     * 开始合服, 合服过程包括:
     * <ol>
     * <li>清除无用数据</li>
     * <li>更名</li>
     * <li>合并数据</li>
     * </ol>
     *
     */
    public function start() {
        // 获取数据配置
        $dbCfgHash = $GLOBALS["dbConfig"];
        $dbCfgKeyArr = array_keys($dbCfgHash);

        foreach ($dbCfgKeyArr as $dbCfgKey) {
            if (!stripos($dbCfgKey, "group") === false) {
                // 如果不是以 group 字符串开头,
                // 则直接跳过...
                MyLog::LOG()->warn("skip ${dbCfgKey}");
                continue;
            }

            $dbCfg = $dbCfgHash[$dbCfgKey];
            // 处理一组服务器
            self::processOneGroup($dbCfg);
        }
    }

    /**
     * 处理单个组
     *
     * @param $dbCfg
     * @return void
     *
     */
    private static function processOneGroup($dbCfg) {
        if ($dbCfg == null) {
            // 如果参数对象为空,
            // 则直接退出!
            MyLog::LOG()->error("参数对象为空");
            return;
        }

        for ($i = $dbCfg["startIndex"]; $i <= $dbCfg["endIndex"]; $i++) {
            // 处理单个服务器的数据
            self::processOneServer($dbCfg, $i);
        }
    }

    /**
     * 处理单个服务器的数据
     *
     * @param $dbCfg
     * @param $index
     * @return void
     *
     */
    private static function processOneServer($dbCfg, $index) {
        self::startRename(
            self::createMyParams($dbCfg, $index)
        );
    }

    /**
     * 创建自定义参数
     *
     * @param $dbCfg
     * @param $index
     * @return MyParams | null
     *
     */
    private static function createMyParams($dbCfg, $index) {
        if ($dbCfg == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return null;
        }

        // 服务器名称
        $serverName = "s${index}";

        if ($index <= 9) {
            $serverName = "s0${index}";
        }

        // 数据库名称
        $dbName = "hdsgz_${serverName}_game";
        // 数据库服务器地址
        $dbHost = $dbCfg["gameDbHost"];

        $dbConnStr = "mysql:host=${dbHost};dbname=${dbName}";
        $dbUser = $dbCfg["gameDbUser"];
        $dbPass = $dbCfg["gameDbPass"];

        try {
            // 记录日志文本
            MyLog::LOG()->info("创建数据库连接 = ${dbConnStr}");
            // 创建 PDO 对象
            $pdo = new PDO(
                $dbConnStr, $dbUser, $dbPass,
                array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,)
            );

            // 设置编码
            $pdo->exec("set names 'utf8'");

            // 创建自定义参数
            $myParam = new MyParam();
            // 附上游戏服服 PDO 对象
            $myParam->_gamePDO = $pdo;
            // 设置玩家角色 UUId
            $myParam->_humanUUId = $GLOBALS["humanUUId"];
            // 设置玩家名称
            $myParam->_newOrigName = $GLOBALS["newOrigName"];
            $myParam->_newFullName = $GLOBALS["newFullName"];

            return $myParam;
        } catch (Exception $ex) {
            // 记录错误日志
            MyLog::LOG()->error($ex->getMessage(), $ex);
        }

        return null;
    }

    /**
     * 开始改名
     *
     * @param $myParam MyParams
     * @return void
     *
     */
    private static function startRename($myParam) {
        if ($myParam == null) {
            // 如果参数对象为空,
            // 则直接退出!
            MyLog::LOG()->error("参数对象为空");
            return;
        }

        // 1. 更名
        // 包括玩家名称, 邮件的收件人和发件人名称.
        // 公共数据包括军团名称等
        MyLog::LOG()->info(">>> 更名");
        Kernal_RenameByHuman::process($myParam);
    }
}

