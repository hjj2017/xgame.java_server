package com.game.gameServer;

import com.game.gameServer.framework.App_GameServer;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.log4j.PropertyConfigurator;

/**
 * 游戏服务器
 * 
 * @author hjj2017
 *
 */
public class CLI_GameServer {
	/**
	 * 应用程序主函数
	 * 
	 * @param argArr
	 * 
	 */
	public static void main(String[] argArr) {
		System.out.println("GameServer X");
		System.out.println("+-------\n");

		// 创建命令行对象
		CommandLine cmdLn = createCmdLn(argArr);

		if (cmdLn == null) {
			// 如果命令行对象为空,
			// 则直接退出!
			System.err.println("命令行对象为空");
			System.exit(-1);
			return;
		}

		// 设置 log4j 配置文件
		PropertyConfigurator.configureAndWatch(
			// 读取配置文件
			cmdLn.getOptionValue("l")
		);

		// 创建内核程序
		App_GameServer k = App_GameServer.OBJ;

		// 初始化并启动服务器
		k.init();
		k.startUp();
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
