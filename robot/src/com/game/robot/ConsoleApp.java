package com.game.robot;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.log4j.PropertyConfigurator;

import com.game.robot.kernal.RobotMain;

/**
 * 机器人的 Console 程序
 * 
 * @author hjj2019
 * @since 2015/5/14
 * 
 */
public final class ConsoleApp {
	/**
	 * 类默认构造器
	 * 
	 */
	private ConsoleApp() {
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
		//
		// 命令行参数数组,
		// 一般是来自命令行输入的参数!
		// 在 Eclipse IDE 中可以通过添加程序参数的方式来制定!
		// 具体方法是:
		// 在该文件上点击鼠标右键显示弹出菜单,
		// 在菜单中选择 "Run As" --> "Run Configurations"
		// 在弹出的对话框中找到 "Arguments" 选项,
		// 在 "Program Arguments" 文本框里添加参数文本, 例如:
		// 
		// -c ./etc/robot.json -l ./etc/log4j.properties
		// 
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
		op.addOption("l", true, "log4j.properties 日志配置文件");

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
