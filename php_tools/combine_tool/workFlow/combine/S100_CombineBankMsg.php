<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 钱庄消息 */
class S100_CombineBankMsg extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_bank_msg", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
