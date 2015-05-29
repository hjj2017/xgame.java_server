<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 军团秘境 */
class S100_CombineArmyFairland extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_army_fairyland", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
