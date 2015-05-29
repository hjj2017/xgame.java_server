<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 好友 */
class S100_RenameRelationShip extends AbstractWorkNode {
    // @Overrid
    public function doAction($myParam) {
        // 更新收件人名称
        SQLHelper::executeNonQuery(
            $myParam->_gamePDO,
            "update t_relation_info as A,
                    t_human_renaming as B
                set A.targetCharName = B.`human_new_full_name`
              where A.targetCharId = B.`human_uuid`"
        );
    }
}
