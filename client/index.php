<?php
// import
require_once("WebApp.php");

/**
 * 创建并包含文件
 *
 * @param $fn String
 * @return void
 *
 */
function touchAndRequireOnce($fn) {
    if (!file_exists($fn)) {
        touch($fn);
    }

    require_once($fn);
}

// 创建并包含文件
touchAndRequireOnce("maintenance/TimeConfig.php");
touchAndRequireOnce("maintenance/WhiteListConfig.php");

// 获取当前时间
$nowTime = time();
$whiteList = $GLOBALS["whiteList"];

if ($nowTime >= intval(MAINTENANCE_START_TIME) &&
    $nowTime <= intval(MAINTENANCE_END_TIME) &&
    $whiteList[@$_REQUEST["openid"]] != 1) {
    // 如果当前服务器正在维护中,
    // 并且玩家又不在白名单中,
    // 则直接退出!
    require_once("WebApp.php");
    // 显示维护页面
    global $theApp;
    $theApp->start("maintenance/Show");
    die(-1);
}

//
// 改页面地址用于腾讯 CVM 服务器中,
// 其目的是让页面跳转到帝联 CDN 服务器上的首页
//
// 以下为可配置项
// 游戏服务器地址, 默认为当前服务器域名
// 游戏服务器端口号
$gameServerPort = 8001;
// CDN 地址
$cdnHost = "http://syhdkdsg.dnion.com/hdsg/";
// 客户端版本号
$clientVer = "release";
// 统计汇报地址 ?passportID={0}&humanUUID={1}&moduleName={2}&state={3}&resURL={4}&timespan={5}&serverID={6}
$report = urlencode("http://gm.gz.1251012822.clb.myqcloud.com:8010/client_res");

/**
 * 获取令当前页面要跳转的目标 URL
 *
 * @return String
 *
 */
function getFrameUrl() {
    // 定义全局变量
    global $gameServerPort, $cdnHost, $clientVer, $report;
    $gameServerHost = $_SERVER["HTTP_HOST"];

    // 定义页面参数
    if ($_SERVER["QUERY_STRING"]) {
        // 获取页面参数
        $params = "?" . $_SERVER["QUERY_STRING"];
    }
    else {
        $params = "?";
    }

    // 设置目标地址
    return $cdnHost . $clientVer . "/index.html" . $params . "&serverip=" . $gameServerHost . "&serverport=" . $gameServerPort . "&resver=" . $clientVer . "&reporturl=" . $report . "&t=" . rand();
}
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <title>回到三国志</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>
</head>
<frameset cols="100%" rows="100%" frameborder="0px">
    <frame scrolling="auto" src="<?php echo getFrameUrl(); ?>"></frame>
</frameset>
</html>
