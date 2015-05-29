<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 合并精彩活动数据 */
class S100_CombineWonderfulActivity extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_wonderful_activity", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
