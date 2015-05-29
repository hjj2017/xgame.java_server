<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 护送次数 */
class S100_CombineEscortTimes extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_escort_times", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
