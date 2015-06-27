<?php
// import
require_once("WebApp.php");

// 获取 action
$action = $_REQUEST["action"];
MyLog::LOG()->info("收到框架请求 : action = $action");

// 执行 Web 应用
global $theApp;
$theApp->start($action);
