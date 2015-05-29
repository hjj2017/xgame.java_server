<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 军团秘境 */
class S100_RenameArmyFairyland_H extends AbstractWorkNode {
    // @Overrid
    public function doAction($myParam) {
        // 执行 SQL 语句
        SQLHelper::executeNonQuery(
            $myParam->_gamePDO,
            "update t_army_fairyland as A, t_human_renaming as B
                set A.roleName = B.`human_new_full_name`
              where A.`id` = B.`human_uuid`"
        );
    }
}
