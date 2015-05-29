<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 清理征收数据 */
class S100_ClearMoneyTree extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        // 执行 SQL 语句
        SQLHelper::executeNonQuery(
            $myParam->_sheepPDO,
            "delete from t_money_tree where `human_uuid` in ( select human_uuid from combine_clearByHuman )"
        );
    }
}
