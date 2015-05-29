<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 宝物 */
class S100_CombineCimeliaInfo extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_cimelia_info", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
