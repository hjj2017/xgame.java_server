<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 好友 */
class S100_ClearRelationInfo extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
    	$sqlArr = array();
    	
    	$sqlArr []= <<<__sql
delete from t_relation_info where charId in ( select human_uuid from combine_clearByHuman )
__sql;

    	$sqlArr []= <<<__sql
delete from t_relation_info where targetCharId in ( select human_uuid from combine_clearByHuman )
__sql;

        // 执行 SQL 语句
        SQLHelper::executeNonQueryArray(
            $myParam->_sheepPDO,
            $sqlArr
        );
    }
}
