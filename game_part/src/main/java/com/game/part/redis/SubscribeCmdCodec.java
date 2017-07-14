package com.game.part.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.game.part.util.Assert;
import com.game.part.util.ClazzUtil;
import com.game.part.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * 订阅命令编解码器
 */
public class SubscribeCmdCodec {
    /**
     * 获取日志对象
     */
    static private final Logger LOGGER = LoggerFactory.getLogger(SubscribeCmdCodec.class);

    /**
     * 类名称
     */
    static private final String CLAZZ_NAME = "C";

    /**
     * 对象
     */
    static private final String OBJECT = "O";

    /**
     * 将订阅命令编码为字符串
     *
     * @param cmdObj 命令对象
     */
    String encode(AbstractSubscribeCmd cmdObj) {
        // 断言参数不为空
        Assert.notNull(cmdObj, "null cmdObj");

        // 设置私有频道
        cmdObj._fromChannel = "";

        // 将消息序列化为 JSON 对象
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(CLAZZ_NAME, cmdObj.getClass().getCanonicalName());
        jsonObj.put(OBJECT, cmdObj);

        return jsonObj.toJSONString();
    }

    /**
     * 将字符串解码为订阅命令
     *
     * @param jsonStr
     * @return
     */
    AbstractSubscribeCmd decode(String jsonStr) {
        // 断言参数不为空
        Assert.notNullOrEmpty(jsonStr, "null or empty jsonStr");

        // 解析为 JSON 对象
        JSONObject jsonObj = JSON.parseObject(jsonStr);
        if (!jsonObj.containsKey(CLAZZ_NAME) ||
            !jsonObj.containsKey(OBJECT)) {
            LOGGER.error("JSON 命令字符串格式错误");
            return null;
        }

        // 获取命令类名称
        final String clazzName = jsonObj.getString(CLAZZ_NAME);
        if (StringUtil.isNullOrEmpty(clazzName)) {
            // 如果类名称为空, 直接退出!
            LOGGER.error("命令类名称为空");
            return null;
        }

        try {
            Class<?> cmdClazz = Class.forName(clazzName);
            if (!ClazzUtil.isConcreteDrivedClass(cmdClazz, AbstractSubscribeCmd.class)) {
                // 记录错误日志
                LOGGER.error(MessageFormat.format(
                    "类 {0} 不是 {1} 的具体派生类",
                    cmdClazz.getCanonicalName(),
                    AbstractSubscribeCmd.class.getCanonicalName()
                ));
                return null;
            }

            // 创建对象实例并返回
            Object anObj = jsonObj.getObject(OBJECT, cmdClazz);
            return (AbstractSubscribeCmd) anObj;
        } catch (Exception ex) {
            // 记录错误日志
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }
}
