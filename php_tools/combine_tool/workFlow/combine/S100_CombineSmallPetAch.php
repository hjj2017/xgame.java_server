<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 宠物成就 */
class S100_CombineSmallPetAch extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_smallpet_ach", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
