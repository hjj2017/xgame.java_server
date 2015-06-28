<?php
require_once("global.php");
require_once(__DIR__ . "/../../configs/ServerName.php");

/**
 * 页面入口函数
 *
 */
function main() {
	// 支付键
	$exchangeKey = EXCHANGE_KEY; // @see global.php

	$uid = $_REQUEST["uid"];
	$pf = $_REQUEST["platform"];
	$gkey = $_REQUEST["gkey"];
	$skey = $_REQUEST["skey"];
	$time = $_REQUEST["time"];
	$sign = $_REQUEST["sign"];
	$roleUUId = $_REQUEST["role_id"];
	$roleName = $_REQUEST["role_name"];
	$orderId = $_REQUEST["order_id"];
	$gold = $_REQUEST["coins"];
	$rmb = $_REQUEST["moneys"];

	// 创建原始字符串
	$origStr = "${uid}${platform}${gkey}${skey}${time}${orderId}${gold}${rmb}#${exchangeKey}";
	$md5 = md5($origStr);

	if ($md5 != $sign) {
		die(json_encode(array(
			"errno" => -2, 
			"errmsg" => "无效参数, MD5 验证失败", 
		)));
	}

	// 获取服务器名称
	$serverName = $GLOBALS["serverName"];

	// 获取平台 UUId
	$platformUUId = getPlatformUUId($uid);
	// 告诉游戏服务器
	tellGameServer(
		$platformUUId, $pf, $serverName, 
		$roleUUId, $roleName, 
		$orderId, $gold, $rmb, $time
	);
}

/**
 * 告诉游戏服务器
 *
 * @param String $platformUUId
 * @param String $pf
 * @param String $serverName
 * @param Long $roleUUId
 * @param String $roleName
 * @param String $orderId
 * @param Integer $gold
 * @param Integer $rmb
 * @param Long $orderTime
 * @return void
 *
 */
function tellGameServer($platformUUId, $pf, $serverName, $roleUUId, $roleName, $orderId, $gold, $rmb, $orderTime) {
	// 获取游戏服 HTTP 地址
	$restfulAddr = GAME_SERVER_RESTFUL_ADDR;

	$url = "";

	// 创建 URL 地址
	$url .= "${restfulAddr}/exchange?";
	$url .= "&platform_uuid=${platformUUId}";
	$url .= "&pf=${pf}";
	$url .= "&server_name=${serverName}";
	$url .= "&role_uuid=${roleUUId}";
	$url .= "&role_name=${roleName}";
	$url .= "&order_id=${orderId}";
	$url .= "&gold=${gold}";
	$url .= "&rmb=${rmb}";
	$url .= "&order_time=${orderTime}";

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

	if (!$result) {
		die("gameServer return false");
	}

	if ($result == "1") {
		// 如果游戏服返回的是成功消息, 
		// 则显示 JSON 字符串!
		$jsonObj = array(
			"errno" => 0, 
			"errmsg" => "充值成功", 
			"data" => array(
				"order_id" => $orderId,
				"platform" => $pf,
				"role_id" => $roleUUId, 
				"role_name" => $roleName, 
				"time" => $time,
			)
		);

		// 显示 JSON 字符串
		echo json_encode($jsonObj);
	} else {
		// 如果游戏服返回的不是成功消息,
		// 则直接显示返回字符串!
		echo $result;
	}
}

// 执行主函数
main();

