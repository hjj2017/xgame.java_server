<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 玩家离线数据 */
class S100_CombineOutlineHumanData extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_outline_human_data", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
