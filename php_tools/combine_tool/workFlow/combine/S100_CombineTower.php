<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 爬塔 */
class S100_CombineTower extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_tower", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
