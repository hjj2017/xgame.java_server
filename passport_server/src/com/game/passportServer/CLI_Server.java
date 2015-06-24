package com.game.passportServer;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Persistence;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.log4j.PropertyConfigurator;

import com.game.part.dao.CommDao;
import com.game.passportServer.http.JettyHttpProc;
import com.game.passportServer.jsonConf.PassportServerConf;

/**
 * Passport 服务器,
 * 请使用如下命令行启动该服务器 : 
 * <pre>
 * java -cp *.jar com.game.passportServer.CLI_Server -c ../all_confg/etc/all_config.json -l ../all_config/etc/passport_server.log4j.properties
 * </pre>
 * <font color='#990000'>
 * 注意 : 命令行中必须给出配置文件 config.json 所在位置!</font><br />
 * 
 * 若希望在 Eclipse 开发环境中正常启动该服务器, 
 * 请设置启动参数! 具体做法如下 :
 * <ol>
 * <li>在该文件上点击鼠标右键;</li>
 * <li>在弹出的菜单中选择 "Run As" --&gt; "Run Configurations ...";</li>
 * <li>在弹出的对话框中的右侧区域找到 "Arguments" 页签;</li>
 * <li>在 "Program Arguments" 下面的文本框里增加 : "-c ../all_config/etc/all_config.json -l ../all_config/etc/passport_server.log4j.properties" ( 注意 : 只要双引号里面的值 );</li>
 * <li>点击 "Apply" 按钮, 最后点击 "Run" 按钮;</li>
 * </ol>
 * 
 * @author hjj2019
 * @since 2015/2/9
 * 
 */
public class CLI_Server {
	/** 配置对象 */
	private PassportServerConf _confObj = null;

	/**
	 * 启动服务器
	 * 
	 */
	private void startUp() {
		// 显示启动信息
		ServerLog.LOG.info("启动 passportServer");
		// 获取当前时间
		long t0 = System.currentTimeMillis();

		if (this._confObj == null) {
			// 如果服务器配置对象为空, 
			// 则设置为默认配置
			this._confObj = PassportServerConf.DEFAULT_CONF;
		}

		/* 初始化 Dao */ {
			// 自定义字典
			Map<String, String> myMap = new HashMap<>();
			myMap.put("javax.persistence.jdbc.url", this._confObj._dbConn);
			myMap.put("javax.persistence.jdbc.user", this._confObj._dbUser);
			myMap.put("javax.persistence.jdbc.password", this._confObj._dbPass);
			// 设置实体管理器工厂
			CommDao.OBJ.putEMF(Persistence.createEntityManagerFactory("llz-passportServer", myMap));
		}
		
		/* 启动 HTTP 服务器 */ {
			// 设置服务器 IP 和端口
			JettyHttpProc.OBJ._bindIpAddr0 = this._confObj._bindIpAddr0;
			JettyHttpProc.OBJ._port0 = this._confObj._port0;
			JettyHttpProc.OBJ._bindIpAddr1 = this._confObj._bindIpAddr1;
			JettyHttpProc.OBJ._port1 = this._confObj._port1;
			
			// 启动服务器
			JettyHttpProc.OBJ.startUp();
		}

		// 强制 GC 一次
		System.gc();
		long t1 = System.currentTimeMillis();

		// 输出启动信息
		ServerLog.LOG.info("passportServer 启动完成");
		ServerLog.LOG.info("passportServer 启动时间 : " + (t1 - t0) + " 毫秒");
	}

	/**
	 * 应用程序主入口
	 * 
	 * @param argArr
	 * 
	 */
	public static void main(String[] argArr) {
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

		// 定义应用对象
		final CLI_Server theApp;
		// 创建应用对象并启动
		theApp = new CLI_Server();
		// 加载配置文件
		theApp._confObj = PassportServerConf.createFromFile(cmdLn.getOptionValue("c"));
		// 启动服务器
		theApp.startUp();
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
