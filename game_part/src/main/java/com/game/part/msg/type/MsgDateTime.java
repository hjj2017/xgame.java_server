package com.game.part.msg.type;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.game.part.msg.IoBuffUtil;

/**
 * 消息中的 DateTime 类型字段
 * 
 * @author hjj2017
 * @since 2015/3/15
 * 
 */
public final class MsgDateTime extends PrimitiveTypeField<LocalDateTime> {
    /**
     * 类默认构造器
     * 
     */
    public MsgDateTime() {
        this(LocalDateTime.now());
    }

    /**
     * 类参数构造器
     * 
     * @param value
     * 
     */
    public MsgDateTime(LocalDateTime value) {
        this.setObjVal(value);
    }

    @Override
    public void readBuff(ByteBuffer buff) {
        // 创建时间对象
        Instant inst = Instant.ofEpochMilli(
            IoBuffUtil.readLong(buff)
        );

        // 创建本地日期时间
        LocalDateTime ldt = inst.atZone(ZoneId.systemDefault()).toLocalDateTime();
        // 设置数值
        this.setObjVal(ldt);
    }

    @Override
    public void writeBuff(ByteBuffer buff) {
        IoBuffUtil.writeLong(this.getLongVal(), buff);
    }

    /**
     * objVal 不能为 null, 但如果真为 null, 则自动创建并返回
     * 
     * @param objVal
     * @return
     * 
     */
    public static MsgDateTime ifNullThenCreate(MsgDateTime objVal) {
        if (objVal == null) {
            // 创建对象
            objVal = new MsgDateTime();
        }

        return objVal;
    }
}
