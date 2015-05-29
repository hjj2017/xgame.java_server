<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 钱庄数据 */
class S100_CombineBankInfo extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_bank_info", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
