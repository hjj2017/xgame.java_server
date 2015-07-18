package com.game.bizModule.guid.bizServ;

import com.game.bizModule.human.entity.HumanEntity;
import com.game.part.dao.CommDao;
import com.game.part.util.EnumHelper;
import com.game.part.util.NullUtil;

/**
 * Guid 类型枚举
 *
 * @author hjj2017
 * @since 2015/7/18
 *
 */
public enum Guid64TypeEnum implements EnumHelper.ICustomEnum {
    /** 角色 */
    human("human", 1) {
        @Override
        public long queryMaxIdFromDb() {
            return NullUtil.optVal(CommDao.OBJ.getMaxId(HumanEntity.class), 0L);
        }
    },

    /** 军团 */
    armyGroup("armyGroup", 2) {
        @Override
        public long queryMaxIdFromDb() {
            return 0L;
        }
    }
;

    /** 整数值 */
    private int _intVal = 0;
    /** 字符串值 */
    private String _strVal = null;

    /**
     * 枚举参数构造器
     *
     * @param strVal
     * @param intVal
     *
     */
    Guid64TypeEnum(String strVal, int intVal) {
        this._strVal = strVal;
        this._intVal = intVal;
    }

    @Override
    public String getStrVal() {
        return this._strVal;
    }

    @Override
    public int getIntVal() {
        return this._intVal;
    }

    /**
     * 从数据库里查询最大 Id
     *
     * @return
     *
     */
    abstract long queryMaxIdFromDb();
}
