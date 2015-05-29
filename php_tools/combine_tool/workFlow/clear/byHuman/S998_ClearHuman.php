<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");

/**
 * 清理角色数据
 *
 * @author jinhaijiang
 * @since 2014/12/19
 *
 */
class S998_ClearHuman extends AbstractWorkNode {
	// @Override
	function doAction($myParam) {
		if ($myParam == null || 
			$myParam->_sheepPDO == null) {
			// 如果羊服配置为空, 
			// 则直接退出!
			return;
		}

		$sqlArr = array();

		// 删除玩家角色
		$sqlArr []= <<<__sql
delete from t_human_info where id in ( select human_uuid from combine_clearByHuman )
__sql;

		// t_user_info 数据表的 Id 字段名称
		$IdColName = null;

		if (SQLHelper::hasColumnAtSpecificDb(
			$myParam->_sheepPDO, null, "t_user_info", "passportId")) {
			// 设置为 passportId
			$IdColName = "passportId";
		} else if (SQLHelper::hasColumnAtSpecificDb(
			$myParam->_sheepPDO, null, "t_user_info", "Id")) {
			// 设置为 Id
			$IdColName = "Id";
		}

		// 更新玩家角色的 OpenId
		$sqlArr []= <<<__sql
update t_human_info as H, t_user_info as U
   set H.open_id = U.openId
 where H.passportId = U.${IdColName}
   and H.open_id is null
__sql;
		
		// 执行 SQL 语句
      	SQLHelper::executeNonQueryArray(
			$myParam->_sheepPDO, 
			$sqlArr
		);
	}
}

