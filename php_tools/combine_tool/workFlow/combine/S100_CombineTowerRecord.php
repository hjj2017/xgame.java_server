<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 爬塔记录 */
class S100_CombineTowerRecord extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_tower_record", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
