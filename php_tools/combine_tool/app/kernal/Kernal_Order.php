<?php
// @import
require_once(__DIR__ . "/../../workFlow/MyParam.php");
require_once(__DIR__ . "/../util/FileUtil.php");
require_once(__DIR__ . "/Callback_CollectClazzObj.php");
require_once(__DIR__ . "/CombineKernal.php");

/**
 * 整理玩家角色数据
 *
 * @author jinhaijiang
 * @since 2015/01/06
 *
 */
class Kernal_Order {
    /**
     * 清理数据
     *
     * @param $myParam MyParams
     * @return void
     *
     */
    static function process($myParam) {
        if ($myParam == null) {
            // 如果参数对象为空,
            // 则直接退出!
            MyLog::LOG()->error("参数对象为空");
            return;
        }

        // 创建收集器
        $collector = new Callback_CollectClazzObj();
        // 遍历 clear/byHuman 目录,
        // 完成清理工作
        FileUtil::traverseDir(
            __WORKFLOW_DIR . "/order",
            $collector
        );

        // 获取类对象数组
        $clazzObjArr = $collector->getClazzObjArr();

        if (count($clazzObjArr) <= 0) {
            // 如果类对象数组为空,
            // 则直接退出!
            return;
        }

        foreach ($clazzObjArr as $clazzObj) {
            // 记录日志
            MyLog::LOG()->info("执行 : " . get_class($clazzObj));
            // 开始工作
            $clazzObj->doAction($myParam);
        }
    }
}
