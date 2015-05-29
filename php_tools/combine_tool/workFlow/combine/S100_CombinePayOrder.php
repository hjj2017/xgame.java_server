<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 充值 */
class S100_CombinePayOrder extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_pay_order", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
