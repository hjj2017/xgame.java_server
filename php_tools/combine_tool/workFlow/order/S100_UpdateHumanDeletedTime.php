<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../SQLHelper.php");

/** 更新角色删除时间 */
class S100_UpdateHumanDeletedTime extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        SQLHelper::executeNonQuery(
            $myParam->_wolfPDO,
            "update t_human_info set deleteTime = null where deleteTime = '0000-00-00'"
        );
    }
}
