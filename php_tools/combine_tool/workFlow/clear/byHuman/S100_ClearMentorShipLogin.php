<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 师徒登陆 */
class S100_ClearMentorShipLogin extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
    	$sqlArr = array();

    	$sqlArr []= <<<__sql
delete from t_mentorship_login where charId in ( select human_uuid from combine_clearByHuman )
__sql;

    	$sqlArr []= <<<__sql
delete from t_mentorship_login where stuCharId in ( select human_uuid from combine_clearByHuman )
__sql;

        // 执行 SQL 语句
        SQLHelper::executeNonQueryArray(
            $myParam->_sheepPDO,
            $sqlArr
        );
    }
}
