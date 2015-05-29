<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 合并签到数据 */
class S100_CombineMoneyBowl extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_money_bowl", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
