<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 道具 */
class S100_CombineItemExInfo extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_itemex_info", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
