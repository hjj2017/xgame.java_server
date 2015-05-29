<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 合并竞技场排行榜数据 */
class S100_CombineArenaRank extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_arena_rank", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
