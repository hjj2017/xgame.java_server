<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 钱庄数据 */
class S100_ClearBankInfo extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        // 执行 SQL 语句
        SQLHelper::executeNonQuery(
            $myParam->_sheepPDO, 
        	"delete from t_bank_info where `id` in ( select human_uuid from combine_clearByHuman )"
        );
    }
}
