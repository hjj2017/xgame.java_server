<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 单人副本 */
class S100_CombineSingleWar extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_single_war", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
