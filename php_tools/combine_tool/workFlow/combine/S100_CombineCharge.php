<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 充值 */
class S100_CombineCharge extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_charge", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
