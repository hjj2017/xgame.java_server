<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 命格 */
class S100_CombineFate extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_fate", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
