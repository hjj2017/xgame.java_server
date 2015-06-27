<?php
require_once("global.php");

/**
 * 页面入口函数
 *
 */
function main() {
	// 登陆键
	$loginKey = LOGIN_KEY; // @see global.php

	$uid = $_REQUEST["uid"];
	$platform = $_REQUEST["platform"];
	$gkey = $_REQUEST["gkey"];
	$skey = $_REQUEST["skey"];
	$time = $_REQUEST["time"];
	$sign = $_REQUEST["sign"];

	// 创建原始字符串
	$origStr = "${uid}${platform}${gkey}${skey}${time}#${loginKey}";
	$md5 = md5($origStr);

	if ($md5 != $sign) {
		die(json_encode(array(
			"errno" => -2, 
			"errmsg" => "无效参数, MD5 验证失败", 
		)));
	}

	// 获取平台 UUId
	$platformUUId = getPlatformUUId($uid);
	// 去问问游戏服
	questGameServer(
		$platformUUId
	);
}

/**
 * 问一下游戏服
 * 
 * @param String $platformUUId
 * @return void
 * 
 */
function questGameServer($platformUUId) {
	// 获取游戏服 HTTP 地址
	$restfulAddr = GAME_SERVER_RESTFUL_ADDR;

	$url = "";
	
	// 创建 URL 地址
	$url .= "${restfulAddr}/check_user?";
	$url .= "&platform_uuid=${platformUUId}";

	// 创建 CURL 实例
	$ch = curl_init();

	curl_setopt($ch, CURLOPT_URL, $url);
	curl_setopt($ch, CURLOPT_HEADER, TRUE);
	curl_setopt($ch, CURLOPT_NOBODY, TRUE);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
	
	// 执行 CURL并返回结果
	$result = curl_exec($ch);
	// 关闭 CURL
	curl_close($ch);

	if ($result == "0") {
		die(json_encode(array(
			"errno" => -1, 
			"errmsg" => "未创建角色"
		)));
	}

	echo json_encode(array(
		"errno" => 0,
		"errmsg" => "查询成功", 
		"data" => $result
	));
}

main();
