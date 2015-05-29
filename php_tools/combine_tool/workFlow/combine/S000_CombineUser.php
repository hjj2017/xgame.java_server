<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 用户 */
class S000_CombineUser extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
    	// 合并数据
        CombineHelper::combine(
            "t_user_info", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
