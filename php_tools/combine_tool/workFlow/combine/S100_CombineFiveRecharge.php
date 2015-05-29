<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 5 连冲 */
class S100_CombineFiveRecharge extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_five_recharge", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
