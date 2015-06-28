<?php
// 引入所有配置文件
loadAllConf();

/**
 * 加载所有配置文件
 *
 * @return void
 *
 */
function loadAllConf() {
	// 遍历配置目录中的所有文件,
	// 并包含进来!
	foreach (scandir("etc/") as $handle) {

		if (is_dir($handle)) {
			// 如果当前句柄是目录,
			// 则直接跳过!
			continue;
		}

		// 创建并包含文件
		touchAndRequireOnce("etc/" . $handle);
	}
}

/**
 * 创建并包含文件
 *
 * @param String $fn
 * @return void
 *
 */
function touchAndRequireOnce($fn) {
    if (!file_exists($fn)) {
        touch($fn);
    }

    require_once($fn);
}

// 获取当前时间
$nowTime = time();
$whiteList = $GLOBALS["whiteList"];

if ($nowTime >= intval(MAINTENANCE_START_TIME) &&
    $nowTime <= intval(MAINTENANCE_END_TIME) &&
    $whiteList[@$_REQUEST["openid"]] != 1) {
    // 如果当前服务器正在维护中,
    // 并且玩家又不在白名单中,
    // 则直接退出!
    header("redirect: /maintenance.php");
    die(-1);
}

/**
 * 获取令当前页面要跳转的目标 URL
 *
 * @return String
 *
 */
function getFrameUrl() {
	//
	// 该页面地址用于腾讯 CVM 服务器中,
	// 其目的是让页面跳转到帝联 CDN 服务器上的首页
	//
	// 以下为可配置项
	// CDN 地址
	// @see configs/Cdn.php
	$cdnHost = CLIENT_CDN_URL;
	// 客户端版本号
	// @see configs/ResVer.php
	$clientVer = CLIENT_VER;
	
	// 游戏服务器地址, 默认为当前服务器域名
    $gameServerHost = $_SERVER["HTTP_HOST"];
    // 游戏服务器端口号
    // @see configs/ServerPort.php
    $gameServerPort = CLIENT_PORT;
    // 设置服务器名称
    // @see configs/ServerName.php
    $serverName = $GLOBALS["serverName"];

    // 统计汇报地址 : http://gm.gz.1251012822.clb.myqcloud.com:8010/client_res
    $reportUrl = "";
    // 原有的页面参数
    $origPageParamStr = "";

    // 定义页面参数
    if ($_SERVER["QUERY_STRING"]) {
        // 获取页面参数
        $origPageParamStr = "?" . $_SERVER["QUERY_STRING"];
    }
    else {
        $origPageParamStr = "?";
    }

    $url = $cdnHost;
    $url .= $clientVer;
    $url .= "/index.html" . $origPageParamStr;
    $url .= "&serverip=" . $gameServerHost;
    $url .= "&serverport=" . $gameServerPort;
    $url .= "&resver=" . $clientVer;
    $url .= "&reporturl=" . $reportUrl;
    $url .= "&server_name=" . $serverName;
    $url .= "&t=" . time();
    
    // 设置目标地址
    return $url;
}
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>Xgame</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>
</head>
<frameset cols="100%" rows="100%" frameborder="0px">
<frame scrolling="auto" src="<?php echo getFrameUrl(); ?>"></frame>
</frameset>
</html>

