<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** VIP 奖励 */
class S100_CombineVipReward extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_vip_reward", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
