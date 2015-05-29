<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 爬塔 */
class S100_RenameRotateTableConfig extends AbstractWorkNode {
    // @Overrid
    public function doAction($myParam) {
    	// 获取旧名称和新名称
    	$oldName = $myParam->_oldFullName;
    	$newName = $myParam->_newFullName;

    	// SQL 语句
    	$sql = <<<__sql
update t_rotate_table_config as A
   set A.`logsJson` = replace(A.`logsJson`, '"owner":"${oldName}"', '"owner":"${newName}"')
__sql;

        // 更新收件人名称
        SQLHelper::executeNonQuery(
            $myParam->_gamePDO, 
        	$sql
        );
    }
}
