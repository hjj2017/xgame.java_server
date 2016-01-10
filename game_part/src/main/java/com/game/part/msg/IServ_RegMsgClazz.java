package com.game.part.msg;

import java.text.MessageFormat;

import com.game.part.msg.type.AbstractMsgObj;
import com.game.part.msg.type.ReadHelperMaker;
import com.game.part.msg.type.WriteHelperMaker;
import com.game.part.util.ClazzUtil;

/**
 * 注册消息类
 * 
 * @author hjj2017
 * @since 2015/3/14
 * 
 */
interface IServ_RegMsgClazz {
    /**
     * 注册消息类, 注意: 
     * 相同 msgSerialUId 不能注册不同的消息类!
     * 
     * @param msgSerialUId 
     * @param newMsgClazz
     * 
     */
    default void regMsgClazz(
        short msgSerialUId, 
        Class<? extends AbstractMsgObj> newMsgClazz) {
        if (newMsgClazz == null) {
            // 如果参数对象为空, 
            // 则直接退出!
            return;
        }

        if (ClazzUtil.isConcreteDrivedClass(newMsgClazz, AbstractMsgObj.class) == false) {
            // 如果消息类不是 
            // AbstractMsgObj 的具体实现类,
            // 则抛出异常!
            MsgLog.LOG.error(MessageFormat.format(
                "{0} 类不是 {1} 的具体实现类, 不能注册到消息服务!", 
                newMsgClazz.getName(), 
                AbstractMsgObj.class.getName()
            ));
            return;
        }

        try {
            // 获取已经注册的消息类
            Class<?> oldMsgClazz = MsgServ.OBJ._msgClazzMap.get(msgSerialUId);
    
            if (oldMsgClazz == null) {
                // 添加消息类到字典
                MsgServ.OBJ._msgClazzMap.put(
                    msgSerialUId, 
                    newMsgClazz
                );

                // 在生成读写代码之前先验证类
                ClazzDefValidator.validate(newMsgClazz);

                // 事先构建好读取帮助器和写入帮助器
                ReadHelperMaker.make(newMsgClazz);
                WriteHelperMaker.make(newMsgClazz);
                return;
            }
    
            if (oldMsgClazz.equals(newMsgClazz)) {
                // 要是两个类相同, 
                // 那就算了...
                return;
            } else {
                // 如果两个类不想同, 
                // 则直接抛出异常!
                throw new MsgError(MessageFormat.format(
                    "注册消息类 {0} 失败, 已经使用 msgSerialUId = {1} 的数值注册过消息类 {2}",
                    newMsgClazz.getName(),
                    String.valueOf(msgSerialUId), 
                    oldMsgClazz.getName()
                ));
            }
        } catch (MsgError err) {
            // 直接抛出异常信息
            throw err;
        } catch (Exception ex) {
            // 记录错误日志并抛出异常信息
            MsgLog.LOG.error(ex.getMessage(), ex);
            throw new MsgError(ex);
        }
    }
}
