<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 在线购买 */
class S100_CombineOnlineBuy extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_online_buy", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
