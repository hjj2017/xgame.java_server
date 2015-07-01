<?php
// @import
require_once("../etc/MaintenanceTime.php");

/**
 * 获取维护开始时间
 *
 * @return String
 *
 */
function getStartTimeStr() {
    // 获取今天的日期
    $today = intval(date("Ymd"));

    // 获取开始时间和开始日期
    $startTime = intval($GLOBALS["MAINTENANCE_START_TIME"]);
    $startDate = intval($startTime / 1000000);

    if ($startDate == $today) {
        // 获取小时和分钟
        return date("H:i", strtotime("${startTime}"));
    } else {
        // 返回具体日期时间
        return date(
            "Y年m月d日 H:i",
            strtotime("${startTime}")
        );
    }
}

/**
 * 获取维护结束时间
 *
 * @return String
 *
 */
function getEndTimeStr() {
    // 获取今天的日期
    $today = intval(date("Ymd"));
    // 获取维护结束时间和结束日期
    $endTime = intval($GLOBALS["MAINTENANCE_END_TIME"]);
    $endDate = intval($endTime / 1000000);

    if ($endDate == $today) {
        // 如果维护时间是在同一天
        return date("H:i", strtotime("${endTime}"));
    } else {
        // 如果需要维护好几天...
        return date(
            "Y年m月d日 H:i",
            strtotime("${endTime}")
        );
    }
}

/**
 * 获取维护开始日期
 *
 * @return String
 *
 */
function getStartDateStr() {
    // 获取小时和分钟
    return date("Y年m月d日", strtotime($GLOBALS["MAINTENANCE_START_TIME"]));
}
?>
<!DOCTYPE html>
<html>
<head>
    <title>维护中</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"></meta>
</head>
<body bgcolor="#090909" topmargin="16">

<table align="center" border="0" background="maintenance/BG.jpg" width="760" height="580">
    <tr height="200">
        <td></td>
        <td></td>
        <td></td>
    </tr>
    <tr height="200">
        <td width="90"></td>
        <td  valign="top">
            <font face="微软雅黑" color="#ffffff">
                尊敬的玩家朋友们：<br />
                　　为了给各位玩家带来更好的游戏体验，
                我们将更新服务器，
                预计更新时间为：<?php echo getStartTimeStr(); ?>——<?php echo getEndTimeStr(); ?>，
                请大家安排好上线时间。<br />
                　　我们一直致力于不断改善游戏品质和服务质量，
                非常感谢各位玩家朋友长期以来的支持与厚爱！
            </font>
        </td>
        <td width="400"></td>
    </tr>
    <tr>
        <td></td>
        <td align="right" valign="top">
            <font face="微软雅黑" color="#ffffff">
                《<font onclick="javascript: gotoTest();">Xgame</font>》运营团队<br /><?php echo getStartDateStr(); ?>
            </font>
        </td>
        <td></td>
    </tr>
</table>

</body>
</html>
