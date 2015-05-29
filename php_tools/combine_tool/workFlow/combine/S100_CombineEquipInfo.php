<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 装备信息 */
class S100_CombineEquipInfo extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_equip_info", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
