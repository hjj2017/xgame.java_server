<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 军团成员 */
class S100_RenameArmyGroupMember_H extends AbstractWorkNode {
    // @Overrid
    public function doAction($myParam) {
        // 更新创建者名称
        SQLHelper::executeNonQuery(
            $myParam->_gamePDO,
            "update t_armygroupmember as A, t_human_renaming as B
                set A.RoleName = B.`human_new_full_name`
              where A.RoleId = B.`human_uuid`"
        );
    }
}
