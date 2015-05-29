<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 卡牌招募数据 */
class S100_ClearCard2Hire extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        // 执行 SQL 语句
        SQLHelper::executeNonQuery(
            $myParam->_sheepPDO,
            "delete from t_card2_hire where human_uuid in ( select human_uuid from combine_clearByHuman )"
        );
    }
}
