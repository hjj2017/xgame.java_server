<?php
// 登陆键, 由第三方平台提供
define("LOGIN_KEY", "6XqyDKxVgefklCBbodkupshT3JiO0pFm");
// 支付键, 由第三方平台提供
define("EXCHANGE_KEY", "bw4T0rE0JO25BFRTFMNqGzVNsIvdjGHa");
// 游戏服 Restful 地址
define("GAME_SERVER_RESTFUL_ADDR", "http://127.0.0.1:8042");
 
/**
 * 获取平台 UUId
 * 
 * @param mixed $Id
 * @return String
 * 
 */
function getPlatformUUId($Id) {
	return "360-${Id}";
}
