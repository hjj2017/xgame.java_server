<?php
// @import
require_once(__DIR__ . "/../lib/apache-log4php-2.3.0/Logger.php");

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
            // 创建日志对象
            Logger::configure(__DIR__ . "/../etc/log4php.config.xml");
            MyLog::$_logger = Logger::getLogger("MyLog");
        }

        return MyLog::$_logger;
    }
}
