<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 翅膀 */
class S100_CombineWing extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_wing", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
