<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 邮件 */
class S100_RenameMail extends AbstractWorkNode {
    // @Overrid
    public function doAction($myParam) {
        // 更新收件人名称
        SQLHelper::executeNonQuery(
            $myParam->_gamePDO,
            "update t_mail_info as A, t_human_renaming as B
                set A.recName = B.`human_new_full_name`
              where A.recId = B.`human_uuid`"
        );

        // 更新发件人名称
        SQLHelper::executeNonQuery(
            $myParam->_gamePDO,
            "update t_mail_info as A, t_human_renaming as B
                set A.sendName = B.`human_new_full_name`
              where A.sendId = B.`human_uuid`"
        );
    }
}
