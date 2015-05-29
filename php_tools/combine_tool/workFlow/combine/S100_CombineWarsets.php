<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 合并新单人副本数据 */
class S100_CombineWarSets extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_warsets_manager", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
