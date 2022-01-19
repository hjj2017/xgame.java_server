package org.xgame.dbfarmer;

import org.apache.log4j.PropertyConfigurator;

/**
 * 应用程序类
 */
public final class MainApp {
    /**
     * 应用程序主函数
     *
     * @param argvArray 参数数组
     */
    public static void main(String[] argvArray) {
        // 设置 log4j 属性文件
        PropertyConfigurator.configure(MainApp.class.getClassLoader().getResourceAsStream("log4j.properties"));
    }
}
