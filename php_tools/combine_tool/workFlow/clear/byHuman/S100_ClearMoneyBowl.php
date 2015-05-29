<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 清理聚宝盆数据 */
class S100_ClearMoneyBowl extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        // 执行 SQL 语句
        SQLHelper::executeNonQuery(
            $myParam->_sheepPDO,
            "delete from t_money_bowl where `id` in ( select human_uuid from combine_clearByHuman )"
        );
    }
}
