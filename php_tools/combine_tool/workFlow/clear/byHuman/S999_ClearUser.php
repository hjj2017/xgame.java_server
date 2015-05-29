<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");

/**
 * 清理玩家数据
 *
 * @author jinhaijiang
 * @since 2014/12/19
 *
 */
class S999_ClearUser extends AbstractWorkNode {
	/**
	 * 狼服数据库是否已更新?
	 *
	 * @var boolean
	 *
	 */
	private static $_wolfDbOk = false;

	// @Override
	function doAction($myParam) {
		if (self::$_wolfDbOk == false) {
			// 修改狼服主键
			self::renewPk($myParam->_wolfPDO);
	      	// 狼服已经清理完
	      	self::$_wolfDbOk = true;
		}

		// 修改羊服主键
		self::renewPk($myParam->_sheepPDO);
	}

	/**
	 * 更新主键
	 *
	 * @param PDO $pdo
	 * @return void
	 *
	 */
	private static function renewPk($pdo) {
		if ($pdo == null) {
			// 如果参数对象为空,
			// 则直接退出!
			return;
		}

		if (SQLHelper::isPrimaryKey(
			$pdo, null, "t_user_info", "openId")) {
			// 如果 t_user_info 数据表中的 openId 已经是主键,
			// 则直接退出!
			return;
		}

		//
		// 考虑到 PassportId 有重复的问题,
		// 所以在合并 t_user_info 数据表之前, 先要取消 PassportId 的主键设置!
		// 也就是取消 t_user_info 表的 Id 主键!
		// 我们需要改用 OpenId,
		// 注意 : OpenId 是绝对不会重复的!
		// 但是同一个玩家有可能在两个服务器上都有号...
		// 这样也就造成了两个服上的 OpenId 也会重复的情况!
		// 这里就需要特殊处理一下.
		//
		$sqlArr = array();

		// 创建临时表,
		// 统计 openId 重复的数据
		$sqlArr []= <<<__sql
create table if not exists `combine_duplicatePassportId` (
    passportId bigint
);
__sql;
		 
		// 事先清空表
		$sqlArr []= <<<__sql
truncate table `combine_duplicatePassportId`;
__sql;

		if (SQLHelper::hasColumnAtSpecificDb($pdo, null, "t_user_info", "passportId")) {
			// 如果 t_user_info 表中有 passportId 列
			if (SQLHelper::hasColumnAtSpecificDb($pdo, null, "t_user_info", "Id")) {
				// 如果还有 Id 列,
				// 那么就让 Id 列的数值 = passportId
				$sqlArr []= <<<__sql
update t_user_info set `Id` = passportId;
__sql;
			} else {
				// 如果没有 Id 列,
				// 那么更名 passportId 列为 Id
				$sqlArr []= <<<__sql
alter table t_user_info change passportId `Id` bigint;
__sql;
			}
		}

		// 添加 passportId
		$sqlArr []= <<<__sql
insert into `combine_duplicatePassportId`
select A.`Id`
  from `t_user_info` as A,
	   `t_user_info` as B
 where A.openId = B.openId
   and A.`Id` < B.`Id`;
__sql;
	
		// 删除重复的数据
		$sqlArr []= <<<__sql
delete from `t_user_info`
 where `Id` in (
select `passportId`
  from `combine_duplicatePassportId`
);
__sql;

		// 删除原有主键!
		$sqlArr []= <<<__sql
alter table t_user_info drop primary key
__sql;

		// 设置 openId 为新主键
		$sqlArr []= <<<__sql
alter table t_user_info add primary key ( `openId` )
__sql;

		if (SQLHelper::hasColumnAtSpecificDb(
			$pdo, null, 
			"t_user_info", 
			"passportId") == false) {
			// 如果 t_user_info 表中没有 passportId, 
			// 则将 Id 修改为 passportId
			$sqlArr []= <<<__sql
alter table `t_user_info` change `Id` `passportId` bigint;
__sql;
		}
	
		if (SQLHelper::isKey($pdo, null, "t_user_info", "passportId") == false) {
			// 如果 Id 字段不是键,
			// 则设置为键!
			$sqlArr []= <<<__sql
alter table `t_user_info` add key ( `passportId` )
__sql;
		}
	
		// 执行 SQL 语句
		SQLHelper::executeNonQueryArray($pdo, $sqlArr);
	}
}

