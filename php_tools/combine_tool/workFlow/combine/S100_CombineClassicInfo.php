<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 经典战役 */
class S100_CombineClassicInfo extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_classic_info", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
