package org.xgame.bizserver.base;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Internal;
import com.google.protobuf.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xgame.bizserver.def.ServerJobTypeEnum;
import org.xgame.bizserver.msg.CommProtocol;
import org.xgame.bizserver.msg.LoginServerProtocol;

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
     * 单例对象
     */
    private static final MsgRecognizer INSTANCE = new MsgRecognizer();

    /**
     * 消息代号和消息对象字典
     */
    private final Map<String, Integer> _msgNameAndMsgCodeMap = new ConcurrentHashMap<>();

    /**
     * 消息代号和消息对象字典
     */
    private final Map<Integer, GeneratedMessageV3> _msgCodeAndMsgObjMap = new ConcurrentHashMap<>();

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
     * 获取单例对象
     *
     * @return 单例对象
     */
    public static MsgRecognizer getInstance() {
        return INSTANCE;
    }

    /**
     * 尝试初始化
     */
    private void tryInit() {
        if (_initOK) {
            return;
        }

        synchronized (MsgRecognizer.class) {
            if (_initOK) {
                return;
            }

            LOGGER.debug("=== 完成消息编号与消息体的映射 ===");

            for (ServerJobTypeEnum sjt : ServerJobTypeEnum.values()) {
                tryInit(
                    sjt, // 服务器工作类型
                    CommProtocol.CommMsgCodeDef.values(),
                    CommProtocol.class
                );
            }

            tryInit(
                ServerJobTypeEnum.LOGIN,
                LoginServerProtocol.LoginServerMsgCodeDef.values(),
                LoginServerProtocol.class
            );

            _initOK = true;
        }
    }

    /**
     * 尝试初始化
     *
     * @param sjt           服务器工作类型
     * @param enumValArray  消息枚举类型
     * @param protocolClazz 消息协议类
     */
    private void tryInit(
        ServerJobTypeEnum sjt, Enum<?>[] enumValArray, Class<?> protocolClazz) {
        if (null == sjt ||
            null == enumValArray ||
            enumValArray.length <= 0 ||
            null == protocolClazz) {
            return;
        }

        for (Enum<?> enumVal : enumValArray) {
            if (!(enumVal instanceof Internal.EnumLite)) {
                continue;
            }

            String msgName = enumVal.name()
                .replaceAll("_", "")
                .toLowerCase();

            if ("dummy".equals(msgName) ||
                "unrecognized".equals(msgName)) {
                continue;
            }

            int msgCode = ((Internal.EnumLite) enumVal).getNumber();

            _msgNameAndMsgCodeMap.putIfAbsent(
                msgName, msgCode
            );

            _msgCodeAndServerJobTypeMap.put(
                msgCode, sjt
            );
        }

        // 获取内置类数组
        Class<?>[] innerClazzArray = protocolClazz.getDeclaredClasses();

        for (Class<?> innerClazz : innerClazzArray) {
            if (!GeneratedMessageV3.class.isAssignableFrom(innerClazz)) {
                // 如果不是消息,
                continue;
            }

            // 创建消息对象
            Object newMsg = null;

            try {
                newMsg = innerClazz.getDeclaredMethod("getDefaultInstance").invoke(innerClazz);
            } catch (Exception ex) {
                continue;
            }

            // 消息名称
            final String msgName = innerClazz.getSimpleName()
                .replaceAll("_", "")
                .toLowerCase();

            // 获取消息代号
            Integer msgCode = _msgNameAndMsgCodeMap.get(msgName);

            if (null == msgCode) {
                continue;
            }

            _msgCodeAndMsgObjMap.putIfAbsent(
                msgCode,
                (GeneratedMessageV3) newMsg
            );

            LOGGER.info(
                "关联 {} <==> {}, serverJobType = {}",
                innerClazz.getSimpleName(),
                msgCode,
                sjt.getStrVal()
            );
        }
    }

    /**
     * 根据消息编号获取服务器工作类型
     *
     * @param msgCode 指定的消息编号
     * @return 服务器工作类型
     */
    public ServerJobTypeEnum getServerJobTypeByMsgCode(int msgCode) {
        tryInit();
        return _msgCodeAndServerJobTypeMap.get(msgCode);
    }

    /**
     * 根据消息编号获取构建器
     *
     * @param msgCode 消息编号
     * @return 消息构建器
     */
    public Parser<? extends GeneratedMessageV3> getMsgParserByMsgCode(int msgCode) {
        if (msgCode < 0) {
            return null;
        }

        tryInit();
        GeneratedMessageV3 msgObj = _msgCodeAndMsgObjMap.get(msgCode);

        if (null == msgObj) {
            return null;
        } else {
            return msgObj.getParserForType();
        }
    }

    /**
     * 获取消息代号
     *
     * @param msgObj 消息对象
     * @return 消息代号
     */
    public int getMsgCode(GeneratedMessageV3 msgObj) {
        if (null == msgObj) {
            return -1;
        }

        return getMsgCode(msgObj.getClass());
    }

    /**
     * 获取消息代号
     *
     * @param msgClazz 消息类
     * @return 消息代号
     */
    public int getMsgCode(Class<? extends GeneratedMessageV3> msgClazz) {
        if (null == msgClazz) {
            return -1;
        }

        tryInit();

        final String msgName = msgClazz.getSimpleName()
            .replaceAll("_", "")
            .toLowerCase();

        return _msgNameAndMsgCodeMap.get(msgName);
    }
}
