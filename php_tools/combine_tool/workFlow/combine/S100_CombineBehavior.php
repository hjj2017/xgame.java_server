<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 行为数据 */
class S100_CombineBehavior extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_behavior", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
