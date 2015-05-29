<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 卡牌洗炼 */
class S100_ClearCardBaptize extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        // 执行 SQL 语句
        SQLHelper::executeNonQuery(
            $myParam->_sheepPDO,
            "delete from t_card_baptize where human_uuid in ( select human_uuid from combine_clearByHuman )"
        );
    }
}
