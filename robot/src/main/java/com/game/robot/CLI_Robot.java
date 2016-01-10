package com.game.robot;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.log4j.PropertyConfigurator;

import com.game.robot.kernal.RobotMain;

/**
 * 机器人程序, 运行于命令行终端!
 *
 * 可以使用以下命令启动:
 * <pre>
 * java -cp *.jar com.game.robot.CLI_Robot \
 *      -c ./etc/robot.json \
 *      -l ./etc/robot.log4j.properties
 * </pre>
 * <font color='#990000'>
 * 注意 : 命令行中必须给出配置文件 robot.json 所在位置!</font><br />
 *
 * 若希望在 IntelliJ IDEA 开发环境中正常启动该服务器,
 * 请设置启动参数! 具体做法如下:
 * <ol>
 *     <li>在主菜单中选择 "Run" -> "Edit Configurations ...";</li>
 *     <li>在 "Application" 中选择 "CLI_Robot" ( 如果没有, 先直接运行一次 CLI_Robot );</li>
 *     <li>在 "Program arguments" 文本框里增加: "-c ./etc/robot.json -l ./etc/robot.log4j.properties" ( 注意 : 只要双引号里面的值 );</li>
 *     <li>注意, 需要修改 "Working directory", 在原字符串后面增加 "/robot";</li>
 *     <li>点击 "Apply" 按钮, 最后点击 "Run" 按钮;</li>
 * </ol>
 * 
 * @author hjj2019
 * @since 2015/5/14
 * 
 */
public final class CLI_Robot {
    /**
     * 类默认构造器
     * 
     */
    private CLI_Robot() {
    }

    /**
     * 应用程序主函数
     * 
     * @param argArr 命令行参数数组
     * 
     */
    public static void main(String[] argArr) {
        // 
        // 解析命令行参数
        CommandLine cmdLn = createCmdLn(argArr);
        // 创建应用对象
        RobotMain mainObj = new RobotMain();

        if (cmdLn.hasOption("l")) {
            // 设置 log4j 配置文件, 每 20 秒检查一次变化
            PropertyConfigurator.configureAndWatch(cmdLn.getOptionValue("l"), 20000);
        }

        if (cmdLn.hasOption("c")) {
            // 设置 JSON 配置文件
            mainObj._jsonConfFileName = cmdLn.getOptionValue("c");
        }

        // 开始执行
        mainObj.start();
    }

    /**
     * 创建命令行对象
     * 
     * @param argArr
     * @return
     * 
     */
    private static CommandLine createCmdLn(String[] argArr) {
        // 创建参数选项
        Options op = new Options();
        // -c 选项
        op.addOption("c", true, "配置文件名称");
        // -l 选项
        op.addOption("l", true, "robot.log4j.properties 日志配置文件");

        try {
            // 创建默认解析器
            DefaultParser dp = new DefaultParser();
            // 解析命令行参数
            CommandLine cmdLn = dp.parse(op, argArr);

            return cmdLn;
        } catch (Exception ex) {
            // 输出错误日志
            ex.printStackTrace();
        }

        return null;
    }
}
