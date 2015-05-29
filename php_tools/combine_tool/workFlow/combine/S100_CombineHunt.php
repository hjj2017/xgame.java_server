<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 猎命 */
class S100_CombineHunt extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_hunt", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
