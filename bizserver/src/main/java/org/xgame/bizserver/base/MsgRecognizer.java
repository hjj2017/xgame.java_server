package org.xgame.bizserver.base;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Internal;
import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xgame.bizserver.def.ServerJobTypeEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息识别器
 */
public final class MsgRecognizer {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MsgRecognizer.class);

    /**
     * 消息代号和消息对象字典
     */
    private static final Map<Integer, GeneratedMessageV3> _msgCodeAndMsgObjMap = new ConcurrentHashMap<>();

    /**
     * 消息类型和消息编号字典
     */
    private static final Map<Class<?>, Integer> _msgClazzAndMsgCodeMap = new ConcurrentHashMap<>();

    /**
     * 消息编号和服务器工作类型字典
     */
    private static final Map<Integer, ServerJobTypeEnum> _msgCodeAndServerJobTypeMap = new ConcurrentHashMap<>();

    /**
     * 是否初始化完成
     */
    private static volatile boolean _initOK = false;

    /**
     * 私有化类默认构造器
     */
    private MsgRecognizer() {
    }

    /**
     * 尝试初始化
     */
    private static void tryInit() {
        if (_initOK) {
            return;
        }

        synchronized (MsgRecognizer.class) {
            if (_initOK) {
                return;
            }

            LOGGER.info("=== 完成消息编号与消息体的映射 ===");

            for (ServerJobTypeEnum serverJobType : ServerJobTypeEnum.values()) {
            }

            _initOK = true;
        }
    }

    /**
     * 尝试初始化
     *
     * @param protocolClazz     协议类
     * @param enumValArray      枚举值数组
     * @param needServerJobType 需要服务器工作类型
     */
    private static void tryInit(Class<?> protocolClazz, Enum<?>[] enumValArray, ServerJobTypeEnum needServerJobType) {
        if (null == protocolClazz ||
            null == enumValArray ||
            enumValArray.length <= 0) {
            return;
        }

        final Map<String, Integer> enumNameAndEnumValMap = new HashMap<>();

        for (Enum<?> enumVal : enumValArray) {
            if (!(enumVal instanceof Internal.EnumLite) ||
                enumVal.name().equals("_Dummy") ||
                enumVal.name().equals("UNRECOGNIZED")) {
                continue;
            }

            enumNameAndEnumValMap.put(
                enumVal.name(),
                ((Internal.EnumLite) enumVal).getNumber()
            );
        }

        // 获取内置类数组
        Class<?>[] innerClazzArray = protocolClazz.getDeclaredClasses();

        for (Class<?> innerClazz : innerClazzArray) {
            if (!GeneratedMessageV3.class.isAssignableFrom(innerClazz)) {
                // 如果不是消息,
                continue;
            }

            // 消息类
            String clazzName = innerClazz.getSimpleName();
            // 获取消息编号
            Integer msgCode = enumNameAndEnumValMap.get("_" + clazzName);

            if (null == msgCode) {
                continue;
            }

            try {
                // 创建消息对象
                Object newMsg = innerClazz.getDeclaredMethod("getDefaultInstance").invoke(innerClazz);

                LOGGER.info(
                    "关联 {} <==> {}, serverJobType = {}",
                    clazzName,
                    msgCode,
                    needServerJobType.getStrVal()
                );

                _msgCodeAndMsgObjMap.put(
                    msgCode, (GeneratedMessageV3) newMsg
                );

                _msgClazzAndMsgCodeMap.put(
                    innerClazz, msgCode
                );

                _msgCodeAndServerJobTypeMap.put(
                    msgCode,
                    needServerJobType
                );
            } catch (Exception ex) {
                // 记录错误日志
                LOGGER.error(ex.getMessage(), ex);
            }
        }
    }

    /**
     * 根据消息编号获取服务器工作类型
     *
     * @param msgCode 指定的消息编号
     * @return 服务器工作类型
     */
    public static ServerJobTypeEnum getServerJobTypeByMsgCode(int msgCode) {
        tryInit();

        return _msgCodeAndServerJobTypeMap.get(msgCode);
    }

    /**
     * 根据消息编号获取构建器
     *
     * @param msgCode 消息编号
     * @return 消息构建器
     */
    public static Message.Builder getMsgBuilderByMsgCode(int msgCode) {
        if (msgCode < 0) {
            return null;
        }

        tryInit();

        GeneratedMessageV3 msg = _msgCodeAndMsgObjMap.get(msgCode);

        if (null == msg) {
            return null;
        } else {
            return msg.newBuilderForType();
        }
    }

    /**
     * 根据消息类获取消息编号
     *
     * @param msgClazz 消息类
     * @return 消息编号
     */
    public static int getMsgCodeByMsgClazz(Class<?> msgClazz) {
        if (null == msgClazz) {
            return -1;
        }

        tryInit();

        Integer msgCode = _msgClazzAndMsgCodeMap.get(msgClazz);

        if (null != msgCode) {
            return msgCode;
        } else {
            LOGGER.error(
                "未识别出消息编号, msgClazz = {}",
                msgClazz
            );

            return -1;
        }
    }
}
