package org.xgame.comm.cmdhandler;

import com.google.protobuf.GeneratedMessageV3;
import org.slf4j.Logger;
import org.xgame.comm.CommLog;
import org.xgame.comm.util.PackageUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 命令处理器工厂
 */
public final class CmdHandlerFactory {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = CommLog.LOGGER;

    /**
     * 处理器字典
     */
    private static final Map<Class<?>, ICmdHandler<? extends AbstractCmdHandlerContext, ? extends GeneratedMessageV3>>
        _handlerMap = new ConcurrentHashMap<>();

    /**
     * 私有化类默认构造器
     */
    private CmdHandlerFactory() {
    }

    /**
     * 根据消息创建命令处理器
     *
     * @param cmdClazz 命令类
     * @return 命令处理器
     */
    public static ICmdHandler<? extends AbstractCmdHandlerContext, ? extends GeneratedMessageV3> create(Class<?> cmdClazz) {
        if (null == cmdClazz) {
            return null;
        }

        return _handlerMap.get(cmdClazz);
    }

    /**
     * 初始化
     *
     * @param scanJavaPackage 扫描 Java 包
     */
    public static void init(String scanJavaPackage) {
        if (null == scanJavaPackage) {
            throw new IllegalArgumentException("scanJavaPackage is null");
        }

        LOGGER.info("=== 完成命令与处理器的映射 ===");

        // 获取 ICmdHandler 的所有实现类
        Set<Class<?>> cmdHandlerClazzSet = PackageUtil.listSubClazz(
            scanJavaPackage, true, ICmdHandler.class
        );

        for (Class<?> cmdHandlerClazz : cmdHandlerClazzSet) {
            if (null == cmdHandlerClazz ||
                0 != (cmdHandlerClazz.getModifiers() & Modifier.ABSTRACT)) {
                // 如果是抽象类,
                continue;
            }

            // 获取方法数组
            Method[] methodArray = cmdHandlerClazz.getDeclaredMethods();
            // 命令类
            Class<?> cmdClazz = null;

            for (Method currMethod : methodArray) {
                if (!currMethod.getName().equals("handle")) {
                    continue;
                }

                // 获取函数参数数组
                Class<?>[] paramTypeArray = currMethod.getParameterTypes();

                if (paramTypeArray.length < 4 ||
                    paramTypeArray[3] == GeneratedMessageV3.class || // 如果是 GeneratedMessageV3 消息本身, 则直接跳过!
                    !GeneratedMessageV3.class.isAssignableFrom(paramTypeArray[3])) {
                    continue;
                }

                cmdClazz = paramTypeArray[3];
                break;
            }

            if (null == cmdClazz) {
                continue;
            }

            try {
                // 创建指令处理器
                ICmdHandler<?, ?> cmdHandlerImpl = (ICmdHandler<?, ?>) cmdHandlerClazz.getDeclaredConstructor().newInstance();

                LOGGER.info(
                    "关联 {} <==> {}",
                    cmdClazz.getName(),
                    cmdHandlerClazz.getName()
                );

                _handlerMap.put(
                    cmdClazz, cmdHandlerImpl
                );
            } catch (Exception ex) {
                // 记录错误日志
                LOGGER.error(ex.getMessage(), ex);
            }
        }
    }
}
