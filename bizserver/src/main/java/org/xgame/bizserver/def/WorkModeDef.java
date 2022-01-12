package org.xgame.bizserver.def;

/**
 * 工作模式定义
 */
public final class WorkModeDef {
    /**
     * 系统环境变量关键字
     */
    private static final String SYSTEM_ENV_KEY = "XGAME_WORK_MODE";

    /**
     * 开发模式
     */
    private static final String MODE_DEV = "DEV";

    /**
     * 正式模式
     */
    private static final String MODE_OFFICIAL = "OFFICIAL";

    /**
     * 获取当前工作模式
     *
     * @return 当前工作模式
     */
    public static String currWorkMode() {
        String currEnv = System.getenv(SYSTEM_ENV_KEY);
        return (null == currEnv) ? MODE_OFFICIAL : currEnv;
    }

    /**
     * 当前是否是开发模式
     *
     * @return true = 开发模式, false = 其它模式
     */
    public static boolean currIsDevMode() {
        return MODE_DEV.equalsIgnoreCase(System.getenv(SYSTEM_ENV_KEY));
    }
}
