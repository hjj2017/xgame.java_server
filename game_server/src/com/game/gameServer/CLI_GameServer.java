package com.game.gameServer;

import com.game.gameServer.conf.ConfFacade;
import com.game.gameServer.framework.App_GameServer;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.log4j.PropertyConfigurator;

/**
 * 游戏服务器, 运行于命令行终端!
 *
 * 可以使用以下命令启动:
 * <pre>
 * java -cp *.jar com.game.gameServer.CLI_GameServer -c ../all_config/etc/all_config.json -l ../all_config/etc/game_server.log4j.properties
 * </pre>
 * <font color='#990000'>
 * 注意 : 命令行中必须给出配置文件 all_config.json 所在位置!</font><br />
 *
 * 若希望在 IntelliJ IDEA 开发环境中正常启动该服务器,
 * 请设置启动参数! 具体做法如下:
 * <ol>
 *     <li>在主菜单中选择 "Run" -> "Edit Configurations ...";</li>
 *     <li>在 "Application" 中选择 "CLI_GameServer" ( 如果没有, 先直接运行一次 CLI_GameServer );</li>
 *     <li>在 "Program arguments" 文本框里增加: "-c ../all_config/etc/all_config.json -l ../all_config/etc/passport_server.log4j.properties" ( 注意 : 只要双引号里面的值 );</li>
 *     <li>注意, 需要修改 "Working directory", 在原字符串后面增加 "/game_server";</li>
 *     <li>点击 "Apply" 按钮, 最后点击 "Run" 按钮;</li>
 * </ol>
 * 
 * @author hjj2017
 * @see <a href="https://git.oschina.net/afrxprojs/xgame-code_server">xgame-code_server</a>
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
			return;
		}

		if (cmdLn.hasOption("l")) {
			// 如果有 -l 参数,
			// 设置 log4j 配置文件
			PropertyConfigurator.configureAndWatch(
				// 读取配置文件
				cmdLn.getOptionValue("l")
			);
		}

		if (cmdLn.hasOption("c") == false) {
			// 如果没有 -c 参数,
			// 则直接退出!
			System.err.print("未指定配置文件, 请使用 -c 参数指定配置文件");
			return;
		}

		// 加载指定的配置文件
		ConfFacade.OBJ.readFromFile(cmdLn.getOptionValue("c"));
		// 初始化并启动服务器
		App_GameServer.OBJ.init();
		App_GameServer.OBJ.startUp();
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
		op.addOption("c", true, "配置文件");
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
