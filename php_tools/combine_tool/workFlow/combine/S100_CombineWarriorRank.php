<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 合并武将排行榜数据 */
class S100_CombineWarriorRank extends AbstractWorkNode {
    // @Override
    function doAction($myParams) {
        CombineHelper::combine(
            "t_warrior_rank", null, $myParams->_sheepPDO, $myParams->_wolfPDO
        );
    }
}
