<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 师徒 */
class S100_ClearMentorShipInfo extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
    	$sql = array();

    	// 删除学生数据
    	$sqlArr []= <<<__sql
delete from t_mentorship_info where charId in ( select human_uuid from combine_clearByHuman )
__sql;

    	// 删除老师的数据
    	$sqlArr []= <<<__sql
delete from t_mentorship_info where targetCharId in ( select human_uuid from combine_clearByHuman )
__sql;

        // 执行 SQL 语句, 
        SQLHelper::executeNonQueryArray(
            $myParam->_sheepPDO,
            $sqlArr
        );
    }
}
