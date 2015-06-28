<?php
// import
require_once("lib/apache-log4php-2.3.0/Logger.php");
require_once("etc/Log4PHP.Conf.php");

/**
 * 自定义日志类
 *
 */
class MyLog {
    /** 日志对象 */
    private static $_logger = null;

    /**
     * 获取 LOG 对象
     *
     * @return Logger
     *
     */
    public static function LOG() {
        if (MyLog::$_logger == null) {
            // 读取配置
            Logger::configure($GLOBALS["LOG_4_PHP"]);
            // 创建日志对象
            MyLog::$_logger = Logger::getLogger("MyLog");
        }

        return MyLog::$_logger;
    }
}
