<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 点梦成金 */
class S100_CombineMidasTouch extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_midas_touch", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
