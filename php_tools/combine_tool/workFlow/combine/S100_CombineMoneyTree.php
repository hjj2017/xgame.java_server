<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 合并征收数据 */
class S100_CombineMoneyTree extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_money_tree", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
