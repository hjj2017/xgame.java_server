<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 角色 */
class S001_CombineHuman extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_human_info", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
