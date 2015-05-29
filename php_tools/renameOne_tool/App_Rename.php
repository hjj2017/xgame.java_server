<?php
ini_set('date.timezone','Asia/Shanghai');
// @import
require_once(__DIR__ . "/app/kernal/RenameKernal.php");
require_once(__DIR__ . "/etc/dbConfig.php");

/**
 * 改名工具 APP 类
 *
 * @author jinhaijiang
 * @since 2014/12/17
 *
 */
class App_Rename {
    /**
     * 开始合服
     *
     */
    public function start() {
        // 开始合服
        $kernal = new RenameKernal();
        $kernal->start();
    }
}

// 开始合服过程
$theApp = new App_Rename();
$theApp->start();

