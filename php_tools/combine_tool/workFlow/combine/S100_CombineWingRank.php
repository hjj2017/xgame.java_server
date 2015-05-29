<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 合并神翼排行榜数据 */
class S100_CombineWingRank extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_wing_rank", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
