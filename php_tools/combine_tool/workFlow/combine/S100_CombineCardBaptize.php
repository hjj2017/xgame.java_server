<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 卡牌洗炼 */
class S100_CombineCardBaptize extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_card_baptize", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
