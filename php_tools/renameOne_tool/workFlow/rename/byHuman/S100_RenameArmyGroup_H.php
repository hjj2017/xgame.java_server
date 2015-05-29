<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 军团 */
class S100_RenameArmyGroup_H extends AbstractWorkNode {
    // @Overrid
    public function doAction($myParam) {
        // 更新创建者名称
        SQLHelper::executeNonQuery(
            $myParam->_gamePDO,
            "update t_armygroup as A, t_human_renaming as B
                set A.creatorRoleName = B.`human_new_full_name`
              where A.creatorRoleId = B.`human_uuid`"
        );

        // 更新军团长名称
        SQLHelper::executeNonQuery(
            $myParam->_gamePDO,
            "update t_armygroup as A, t_human_renaming as B
                set A.leaderRoleName = B.`human_new_full_name`
              where A.leaderRoleId = B.`human_uuid`"
        );
    }
}
