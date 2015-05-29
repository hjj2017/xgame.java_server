<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 精彩活动 */
class S100_CleartWonderfulActivity extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        // 执行 SQL 语句
        SQLHelper::executeNonQuery(
            $myParam->_sheepPDO,
            "delete from t_wonderful_activity where `uuid` in ( select human_uuid from combine_clearByHuman )"
        );
    }
}
