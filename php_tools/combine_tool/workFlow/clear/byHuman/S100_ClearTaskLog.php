<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/**
 * 清理邮件数据
 *
 * @author jinhaijiang
 * @since 2014/12/19
 *
 */
class S100_ClearTaskLog extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        // 执行 SQL 语句
        SQLHelper::executeNonQuery(
            $myParam->_sheepPDO,
            "delete from t_task_log where charId in ( select human_uuid from combine_clearByHuman )"
        );
    }
}
