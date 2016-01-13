package com.game.robot.kernal;

import java.util.Set;

/**
 * 模块结构
 *
 * @author hjj2017
 * @since 2016/1/13
 *
 */
final class ModuleStruct {
    /** 模块准备类 */
    public Class<?> _readyClazz = null;
    /** 消息处理类 */
    public Set<Class<?>> _handlerClazzSet = null;
}
