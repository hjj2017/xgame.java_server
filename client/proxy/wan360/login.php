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
	$pf = $_REQUEST["platform"];
	$gkey = $_REQUEST["gkey"];
	$skey = $_REQUEST["skey"];
	$time = $_REQUEST["time"];
	$isAdult = $_REQUEST["is_adult"];
	$exts = $_REQUEST["exts"];
	$type = $_REQUEST["type"];
	$sign = $_REQUEST["sign"];

	// 创建原始字符串
	$origStr = "${uid}${pf}${gkey}${skey}${time}${isAdult}${exts}#${loginKey}";
	$md5 = md5($origStr);

	if ($md5 != $sign) {
		die(json_encode(array(
			"errno" => -2, 
			"errmsg" => "无效参数, MD5 验证失败", 
		)));
	}

	// 获取平台 UUId
	$platformUUId = getPlatformUUId($uid);

	// 使用 CURL 向游戏服务器写出登陆票
	writeLoginTicket($platformUUId, $md5);
	// 重定向到 index.php 页面
	gotoIndexPage($platformUUId, $md5);
}

/**
 * 写出登陆票
 * 
 * @param String $platformUUId
 * @param String $md5
 * @return void
 * 
 */
function writeLoginTicket($platformUUId, $md5) {
	// 获取游戏服 HTTP 地址
	$restfulAddr = GAME_SERVER_RESTFUL_ADDR;

	$url = "";

	// 创建 URL 地址
	$url .= "${restfulAddr}/login?";
	$url .= "&platform_uuid=${platformUUId}";
	$url .= "&md5=${md5}";

	// 创建 CURL 实例
	$ch = curl_init();

	curl_setopt($ch, CURLOPT_URL, $url);
	curl_setopt($ch, CURLOPT_HEADER, false);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

	// 执行 CURL
	curl_exec($ch);
	// 关闭 CURL
	curl_close($ch);
}

/**
 * 跳转到网站首页
 * 
 * @param $platformUUId
 * @param $md5
 * @return void
 * 
 */
function gotoIndexPage($platformUUId, $md5) {
	// 原始的页面参数字符串
	$origPageParamStr = "";

	if ($_SERVER["QUERY_STRING"]) {
		// 获取页面参数
		$origPageParamStr = $_SERVER["QUERY_STRING"];
	}

	$url = "";

	$url .= "&platform_uuid=${platformUUId}";
	$url .= "&md5=${md5}";
	$url .= "&t=" . time();

	// 跳转到首页
	header("location: /?${origPageParamStr}${url}");
}

// 执行主函数
main();

