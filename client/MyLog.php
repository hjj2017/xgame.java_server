<?php
// import
require_once("libs/apache-log4php-2.3.0/Logger.php");
require_once("WebApp.php");

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
            Logger::configure(WebApp::getPhysicalPath("php.config/log4php.config.xml"));
            MyLog::$_logger = Logger::getLogger("MyLog");
        }

        return MyLog::$_logger;
    }
}
