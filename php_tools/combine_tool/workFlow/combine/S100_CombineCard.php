<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 卡牌 */
class S100_CombineCard extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_card", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
