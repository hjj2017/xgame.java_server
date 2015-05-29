<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 宠物技能 */
class S100_CombineSmallPetRank extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_smallpet_rank", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
