<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 邮件 */
class S100_ClearMailInfo extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
    	$sqlArr = array();

    	// 删除发件人的邮件
    	$sqlArr []= <<<__sql
delete from t_mail_info where deleted = 1 or charId in ( select human_uuid from combine_clearByHuman )
__sql;
    	
		$sqlArr []= <<<__sql
delete from t_mail_info where recId in ( select human_uuid from combine_clearByHuman )
__sql;
    	
        // 执行 SQL 语句, 删除收件人的邮件
        SQLHelper::executeNonQueryArray(
        	$myParam->_sheepPDO, 
        	$sqlArr
        );
    }
}
