<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 爬塔 */
class S100_RenameTowerRecord extends AbstractWorkNode {
    // @Overrid
    public function doAction($myParam) {
        // 更新收件人名称
        SQLHelper::executeNonQuery(
            $myParam->_gamePDO,
            "update t_tower_record as A,
                    t_human_renaming as B
                set A.`name` = B.`human_new_full_name`
              where A.`uuid` = B.`human_uuid`"
        );
    }
}
