<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 加入军团申请 */
class S100_CombineArmyApplyLog extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_armyapplylog", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
