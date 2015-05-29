<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 傀儡 */
class S100_CombinePuppetCache extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_puppetcache_info", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
