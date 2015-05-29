<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 宠物 */
class S100_CombineSmallPet extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_small_pet", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
