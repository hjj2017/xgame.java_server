<?php
ini_set('date.timezone','Asia/Shanghai');
// @import
require_once(__DIR__ . "/app/kernal/CombineKernal.php");
require_once(__DIR__ . "/etc/dbConfig.php");

/**
 * 合服工具 APP 类
 *
 * @author jinhaijiang
 * @since 2014/12/17
 *
 */
class App_Combine {
    /**
     * 开始合服
     *
     */
    public function start() {
        // 开始合服
        $kernal = new CombineKernal();
        $kernal->start();
    }
}

// 开始合服过程
$theApp = new App_Combine();
$theApp->start();

