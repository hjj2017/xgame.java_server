<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 好友关系 */
class S100_CombineRelationInfo extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_relation_info", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
