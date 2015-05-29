<?php
// @import
require_once(__DIR__ . "/../util/FileUtil.php");

/**
 * 收集类对象
 *
 * @author jinhaijiang
 * @since 2014/12/19
 *
 */
class Callback_CollectClazzObj implements ITraverseDirCallback {
    /**
     * 类对象数组
     *
     * @var array
     *
     */
    private $_clazzObjArr = array();

    // @Override
    function doAction($pathName) {
        if (stripos($pathName, ".php") !== (strlen($pathName) - 4)) {
            // 如果不是 .php 文件,
            // 则直接退出!
            return;
        }

        // 包含文件
        require_once($pathName);
        // 获取类名称并创建对象
        $shortFileName = FileUtil::getFileName($pathName);
        $clazzName = str_replace(".php", "", $shortFileName);

        if (class_exists($clazzName)) {
            // 创建类对象
            $clazzObj = new $clazzName;
            // 添加类对象到数组
            $this->_clazzObjArr []= $clazzObj;
        } else {
            // 如果类名称与文件名不同, 
            // 则直接退出!
            MyLog::LOG()->error("在 ${pathName} 文件中未定义 ${clazzName} 类");
            die;
        }
    }

    /**
     * 获取类对象数组
     *
     * @return array
     *
     */
    public function getClazzObjArr() {
        return $this->_clazzObjArr;
    }
}
