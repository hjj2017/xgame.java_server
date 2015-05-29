<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 合并竞技场排行榜数据 */
class S100_CombineSingleArena extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_single_arena", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
