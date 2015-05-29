<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** Cd 冷却时间 */
class S100_CombineCd extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_cd", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
