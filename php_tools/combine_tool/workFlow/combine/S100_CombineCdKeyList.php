<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** CdKey 活动, 这个表会非常大 */
class S100_CombineCdKeyList extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
//        CombineHelper::combine(
//            "t_cdkeylist", null, $myParam->_sheepPDO, $myParam->_wolfPDO
//        );
    }
}
