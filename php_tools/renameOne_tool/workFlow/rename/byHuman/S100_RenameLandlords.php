<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 斗地主 */
class S100_RenameLandlords extends AbstractWorkNode {
    // @Overrid
    public function doAction($myParam) {
        // 更新地主名称
        SQLHelper::executeNonQuery(
            $myParam->_gamePDO,
            "update t_landlords as A,
                    t_human_renaming as B
                set A.`humanName` = B.`human_new_full_name`
              where A.`humanId` = B.`human_uuid`"
        );

        // 获取旧名称和新名称
        $oldName = $myParam->_oldFullName;
        $newName = $myParam->_newFullName;

        // 更新斗地主互动信息
        $sql = <<< __sql
update t_landlords as A
   set A.`eventList` = replace(A.`eventList`, '"name":"${oldName}"', '"name":"${newName}"')
__sql;

        // 执行 SQL
        SQLHelper::executeNonQuery(
            $myParam->_gamePDO,
            $sql
        );
    }
}
