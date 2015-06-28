<?php
/**
 * 获取游戏服服务器名称
 *
 * @return String
 *
 */
function __GET_SERVER_NAME() {
	// 获取文件名称
	$serverName = exec("pwd");

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

// 保存服务器名称
$GLOBALS["SERVER_NAME"] = __GET_SERVER_NAME();
