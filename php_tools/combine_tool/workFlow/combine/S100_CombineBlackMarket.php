<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 黑市 */
class S100_CombineBlackMarket extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_black_market", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
