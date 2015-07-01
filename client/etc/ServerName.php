<?php
//
// 最新的获取服务器名称的方式, 是:
// 通过 Apache 或 Nginx 的 URL 重写方式进行...
// 在被重写的路径末端加入 "&server_name=S00" 这样的方式!
//
// Nginx 配置例子:
// rewrite ^/index.html(.*)$ /proxy/3rd_platform/index.php?server_name=S00 last;
//
// 保存服务器名称到全局变量
$GLOBALS["SERVER_NAME"] = __getServerName();

/**
 * 获取服务器名称
 *
 * @return String
 *
 */
function __getServerName() {
	// 首先根据请求参数获取服务器名称
	$serverName = __getServerNameByRequest();

	if (strlen($serverName) > 0) {
		return $serverName;
	}

	// 如果请求参数中没有给出服务器名称, 那么
	// 根据目录名获取服务器名称
	$serverName = __getServerNameByDirName();

	if (strlen($serverName) > 0) {
		return $serverName;
	}

	// 都找完了, 还是为空,
	// 则给个默认值
	return $serverName = "S00";
}

/**
 * 根据请求参数获取服务器名称
 *
 * @return String
 *
 */
function __getServerNameByRequest() {
	if (array_key_exists(
		"server_name", $_REQUEST)) {
		// 如果请求参数中有 "server_name",
		// 则获取服务器名称
		return strtoupper($_REQUEST["server_name"]);
	} else {
		// 如果没有则直接退出!
		return null;
	}
}

/**
 * 获取游戏服服务器名称
 *
 * @return String
 *
 */
function __getServerNameByDirName() {
	// 获取文件名称
	$serverName = dirname(dirname(__FILE__));

	// 出现 s00_game/client 的位置
	$rIndex = stripos($serverName, "_game/client");
	// 截断字符串
	$serverName = substr($serverName, 0, $rIndex);

	// 从最右边开始找到第一次出现 "/" 的位置
	$lIndex = strrpos($serverName, "/") + 1;
	// 截取掉 "/" 之前的所有字符
	$serverName = substr($serverName, $lIndex);
	// 为提高兼容性, 增加一个 "-" 字符
	$serverName = strtoupper($serverName);

	return $serverName;
}
