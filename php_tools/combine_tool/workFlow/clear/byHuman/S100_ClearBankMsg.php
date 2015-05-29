<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 钱庄消息 */
class S100_ClearBankMsg extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        // 执行 SQL 语句
        SQLHelper::executeNonQuery(
            $myParam->_sheepPDO, 
        	"delete from t_bank_msg where `charId` in ( select human_uuid from combine_clearByHuman )"
        );
    }
}
