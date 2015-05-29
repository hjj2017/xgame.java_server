<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 观星 */
class S100_CombineObservatory extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_observatory", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
