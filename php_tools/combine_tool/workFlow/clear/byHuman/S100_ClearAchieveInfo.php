<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 成就数据 */
class S100_ClearAchieveInfo extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        // 执行 SQL 语句
        SQLHelper::executeNonQuery(
            $myParam->_sheepPDO,
            "delete from t_achieve_info where `id` in ( select human_uuid from combine_clearByHuman )"
        );
    }
}
