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
	foreach (scandir("../etc/") as $handle) {

		if (is_dir($handle)) {
			// 如果当前句柄是目录,
			// 则直接跳过!
			continue;
		}

		// 创建并包含文件
        require_once("../etc/${handle}");
	}
}

// 获取当前时间
$nowTime = intval(date("YmdHis", time()));
// 获取白名单
$whiteList = $GLOBALS["WHITE_LIST"];

if ($nowTime >= intval($GLOBALS["MAINTENANCE_START_TIME"]) &&
    $nowTime <= intval($GLOBALS["MAINTENANCE_END_TIME"]) &&
    $whiteList[@$_REQUEST["platform_uuid"]] != 1) {
    // 如果当前服务器正在维护中,
    // 并且玩家又不在白名单中,
    // 则直接退出!
    header("Location: maintenance.php");
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
	// @see etc/CdnURL.php
	$cdnURL = $GLOBALS["CDN_URL"];
	// 客户端版本号
	// @see etc/ResVer.php
	$resVer = $GLOBALS["RES_VER"];

	// 游戏服务器地址, 默认为当前服务器域名
    $gameServerHost = $_SERVER["HTTP_HOST"];
    // 游戏服务器端口号
    // @see etc/GameServerPort.php
    $gameServerPort = $GLOBALS["GAME_SERVER_PORT"];
    // 设置服务器名称
    // @see etc/ServerName.php
    $serverName = $GLOBALS["SERVER_NAME"];

    // 原有的页面参数
    $origPageParamStr = "";

    // 定义页面参数
    if ($_SERVER["QUERY_STRING"]) {
        // 获取页面参数
        $origPageParamStr = $_SERVER["QUERY_STRING"];
    }

    $url = $cdnURL;
    $url .= "/${resVer}";
    $url .= "/index.html?${origPageParamStr}";
    $url .= "&game_server_host=${gameServerHost}";
    $url .= "&game_server_port=${gameServerPort}";
    $url .= "&res_ver=${resVer}";
    $url .= "&server_name=${serverName}";
    $url .= "&t=" . time();

    // 设置目标地址
    return $url;
}

echo getFrameUrl();

