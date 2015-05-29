<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 师徒 */
class S100_RenameMentorShip extends AbstractWorkNode {
    // @Overrid
    public function doAction($myParam) {
        // 更新收件人名称
        SQLHelper::executeNonQuery(
            $myParam->_gamePDO,
            "update t_mentorship_info as A,
                    t_human_renaming as B
                set A.targetCharName = B.`human_new_full_name`
              where A.targetCharId = B.`human_uuid`"
        );
    }
}
