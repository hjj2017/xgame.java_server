<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 清理七日礼包数据 */
class S100_ClearSevenDay extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        // 执行 SQL 语句
        SQLHelper::executeNonQuery(
            $myParam->_sheepPDO,
            "delete from t_active_sevenday where `human_uuid` in ( select human_uuid from combine_clearByHuman )"
        );
    }
}
