<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 任务 */
class S100_CombineTaskBase extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_task_base", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
