<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 活跃度 */
class S100_CombineLiveness extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_liveness", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
