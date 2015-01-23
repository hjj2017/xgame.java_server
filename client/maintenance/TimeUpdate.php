<?php
// import
require_once("WebApp.php");
require_once("MyLog.php");

/**
 * 更新服务器维护时间
 *
 * @author hjj2017
 *
 */
class TimeUpdate {
    /**
     * 执行页面
     *
     */
    function doAction() {
        // 获取开始时间和结束时间
        $startTimeStr = @$_REQUEST["startTime"];
        $endTimeStr = @$_REQUEST["endTime"];
        // 记录日志信息
        MyLog::LOG()->info("获取开始时间 = $startTimeStr, 结束时间 = $endTimeStr");

        // 获取开始时间和结束时间
        $startTime = strtotime($startTimeStr);
        $endTime = strtotime($endTimeStr);

        // 写出配置文件
        $this->writeOutConfig(
            $startTime, $endTime
        );

        // 更新 HTML 文件
        $this->updateHtml(
            $startTime, $endTime
        );

        echo 1;
    }

    /**
     * 写出配置文件, 也就是 TimeConfig.php 文件
     *
     * @param $startTime
     * @param $endTime
     *
     */
    private function writeOutConfig(
        $startTime,
        $endTime) {
        // 获取目标文件
        $targetFile = WebApp::getPhysicalPath("maintenance/TimeConfig.php");
        MyLog::LOG()->info("准备打开目标文件 $targetFile");

        // 打开文件并获取句柄
        $fp = fopen($targetFile, "w");

        if (!$fp) {
            // 如果打开文件失败,
            // 则直接退出!
            MyLog::LOG()->error("打开文件失败");
            return;
        }

        $text = <<< __phpCode
<?php
define("MAINTENANCE_START_TIME", "$startTime");
define("MAINTENANCE_END_TIME", "$endTime");

__phpCode;


        // 写出配置文件
        MyLog::LOG()->info("准备写出文件 $targetFile, 内容 = $text");
        $result = fwrite($fp, $text);

        if (!$result) {
            // 如果写出文件失败,
            // 则直接退出!
            MyLog::LOG()->error("写出文件内容失败");
            return;
        }

        // 刷新并关闭文件句柄
        fflush($fp);
        fclose($fp);
    }

    /**
     * 更新 HTML 文件, 也就是 maintenance/Show.html
     *
     * @param $startTime
     * @param $endTime
     *
     */
    private function updateHtml(
        $startTime,
        $endTime) {

        $startTimeStr = date("H:i", $startTime);
        $endTimeStr = date("H:i", $endTime);
        $sigDateStr = date("Y年m月d日", $startTime);

        if (!$this->isTheSameDay($startTime, $endTime)) {
            // 如果不在同一天,
            // 直接加上次日
            $endTimeStr = "次日" . $endTimeStr;
        }

        // 获取目标文件
        $tmplFile = WebApp::getPhysicalPath("maintenance/Show.template.html");
        $htmlFile = WebApp::getPhysicalPath("maintenance/Show.html");
        MyLog::LOG()->info("准备打开目标文件 $tmplFile ( 用于读取 ) 和 $htmlFile ( 用于写出 )");

        // 打开文件并获取句柄
        $fp_tmpl = fopen($tmplFile, "r");
        $fp_html = fopen($htmlFile, "w");

        if (!$fp_tmpl ||
            !$fp_html) {
            // 如果打开文件失败,
            // 则直接退出!
            MyLog::LOG()->error("打开文件失败");
            return;
        }

        while (!feof($fp_tmpl)) {
            // 读取模板文件
            $ln = fgets($fp_tmpl);
            // 替换开始时间和结束时间
            $ln = str_replace("##MAINTENANCE_START_TIME##", $startTimeStr, $ln);
            $ln = str_replace("##MAINTENANCE_END_TIME##", $endTimeStr, $ln);
            $ln = str_replace("##SIG_DATE##", $sigDateStr, $ln);
            // 写出文件内容
            fwrite($fp_html, $ln);
        }

        fflush($fp_html);
        fclose($fp_html);
        fclose($fp_tmpl);
    }

    /**
     * 是否为同一天
     *
     * @param $startTime
     * @param $endTime
     * @return bool
     *
     */
    private static function isTheSameDay($startTime, $endTime) {
        $startTimeObj = getdate($startTime);
        $endTimeObj = getdate($endTime);
        return ($startTimeObj["yday"] == $endTimeObj["yday"]);
    }
}
