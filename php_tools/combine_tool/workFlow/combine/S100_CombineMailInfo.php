<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 邮件 */
class S100_CombineMailInfo extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_mail_info", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
