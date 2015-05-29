<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 建筑 */
class S100_CombineBuilding extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_building", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
