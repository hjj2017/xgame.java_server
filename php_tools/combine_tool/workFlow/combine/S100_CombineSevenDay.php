<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 合并七日礼包数据 */
class S100_CombineSevenDay extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_active_sevenday", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
