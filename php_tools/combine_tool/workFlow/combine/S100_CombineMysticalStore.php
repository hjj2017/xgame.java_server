<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 神秘商店 */
class S100_CombineMysticalStore extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_mystical_store", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
