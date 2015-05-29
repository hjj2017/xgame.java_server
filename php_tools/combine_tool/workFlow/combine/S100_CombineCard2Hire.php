<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 卡牌招募 */
class S100_CombineCard2Hire extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_card2_hire", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
