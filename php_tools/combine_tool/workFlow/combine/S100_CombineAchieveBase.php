<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 成就数据 */
class S100_CombineAchieveBase extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_achieve_base", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
