<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 师徒 */
class S100_CombineMentorShipData extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_mentorship_data", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
