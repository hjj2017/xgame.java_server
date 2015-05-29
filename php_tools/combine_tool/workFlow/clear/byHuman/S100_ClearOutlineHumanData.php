<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 离线数据 */
class S100_ClearOutlineHumanData extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        // 执行 SQL 语句
        SQLHelper::executeNonQuery(
            $myParam->_sheepPDO,
            "delete from t_outline_human_data where human_uuid in ( select human_uuid from combine_clearByHuman )"
        );
    }
}
