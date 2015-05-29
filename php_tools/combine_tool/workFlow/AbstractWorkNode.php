<?php
require_once(__DIR__ . "/MyParam.php");

/**
 * 抽象的工作节点
 *
 * @author jinhaijiang
 * @since 2014/12/18
 *
 */
abstract class AbstractWorkNode {
    /**
     * 执行动作
     *
     * @param $myParam MyParams
     * @return void
     *
     */
    abstract function doAction($myParam);
}
