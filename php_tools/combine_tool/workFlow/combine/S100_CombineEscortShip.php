<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 护送船只 */
class S100_CombineEscortShip extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_escort_ship", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
