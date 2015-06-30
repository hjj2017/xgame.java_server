<?php
// 日志目录
$logDir = dirname(dirname(__FILE__)) . "/log";

//
// 设置 log4php 配置
//
$GLOBALS["LOG_4_PHP"] = array(
    "rootLogger" => array(
        "appenders" => array("default", "myAppender"),
    ),

    "appenders" => array(
        // default appender
        "default" => array(
            "class" => "LoggerAppenderConsole",
            "layout" => array(
                "class" => "LoggerLayoutPattern",
                "params" => array("conversionPattern" => "[%d{Y/m/d H:i:s,u}] [%p] %C{1}.%M --> %m%n"),
            ),
        ),

        // myAppender
        "myAppender" => array(
            "class" => "LoggerAppenderDailyFile",
            "layout" => array(
                "class" => "LoggerLayoutPattern",
                "params" => array("conversionPattern" => "[%d{Y/m/d H:i:s,u}] [%p] %C{1}.%M --> %m%n"),
            ),

            "params" => array(
                "file" => "${logDir}/MyLog-%s.log",
                "datePattern" => "Ymd",
                "append" => true,
            )
        )
    )
);
