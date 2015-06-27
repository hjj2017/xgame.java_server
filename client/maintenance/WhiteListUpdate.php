<?php
// import
require_once("WebApp.php");
require_once("MyLog.php");

/**
 * 更新白名单
 *
 * @author hjj2017
 * @since 2014/7/17
 *
 */
class WhiteListUpdate {
    /**
     * 执行页面
     *
     */
    public function doAction() {
        // 获取白名单列表
        $whiteList = $_GET["whiteList"];

        if (!$whiteList) {
            // 如果白名单列表为空,
            // 则直接退出!
            MyLog::LOG()->info("白名单为空");
            return;
        }

        // 记录日志信息
        MyLog::LOG()->info("接到白名单列表 = $whiteList");
        // 获取目标文件
        $targetFile = WebApp::getPhysicalPath("configs/WhiteListConfig.php");
        MyLog::LOG()->info("准备打开目标文件 $targetFile");

        // 打开文件
        $fp = fopen($targetFile, "w");

        if (!$fp) {
            // 如果打开文件失败,
            // 则直接退出!
            MyLog::LOG()->error("打开文件失败");
            return;
        }

        // 获取白名单数组
        $whiteArr = preg_split("[,]", $whiteList);

        $text = "";
        $text .= "<?php\n";
        $text .= "\$whiteList = array(\n";

        foreach ($whiteArr as $white) {
        	if (strlen($white)) {
            	$text .= "    '". addslashes($white) . "' => 1, \n";
            }
        }

        $text .= "    0 => 0\n";
        $text .= ");\n\n";
        $text .= '$GLOBALS["whiteList"] = $whiteList;';

        // 写出白名单
        $result = fwrite($fp, $text);

        if (!$result) {
            MyLog::LOG()->error("写出文件失败");
        }

        fflush($fp);
        fclose($fp);

        echo 1;
    }
}

