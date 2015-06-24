package com.game.passportServer.http;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.game.passportServer.http.servlet.Servlet_GetPassportInfo;

/**
 * HTTP 服务器, 
 * 启动后, 可以使用如下地址进行测试 :
 * http://127.0.0.1:8007/get_passport_info?platform_uuid=qihu360-1001&pf=wan&game_server_id=1
 * 
 * @author jinhaijiang
 * @since 2015/2/9
 * 
 */
public class JettyHttpProc {
	/** 单例对象 */
	public static final JettyHttpProc OBJ = new JettyHttpProc();
	/** 绑定服务器 Ip 地址 0 */
	public String _bindIpAddr0 = "127.0.0.1";
	/** 服务器端口号 0 */
	public int _port0 = 8001;
	/** 绑定服务器 Ip 地址 1 */
	public String _bindIpAddr1 = null;
	/** 服务器端口号 1 */
	public int _port1 = -1;

	/** 服务器对象 */
	private Server _serverObj = null;

	/**
	 * 类默认构造器
	 * 
	 */
	private JettyHttpProc() {
	}

	/**
	 * 获取处理器字典
	 * 
	 * @return
	 * 
	 */
	private static Map<String, HttpServlet> getServletMap() {
		// 创建 Servlet 字典
		Map<String, HttpServlet> map = new HashMap<>();
		// 添加 GetPassportInfo 请求
		map.put("/get_passport_info", new Servlet_GetPassportInfo());
		// 返回字典
		return map;
	}

	/**
	 * 启动服务器
	 * 
	 */
	public void startUp() {
		try {
			// 创建服务器对象
			Server serverObj = new Server(this._port0);

			// 连接器数组
			Connector[] connArr = null;
			// 创建服务器连接
			ServerConnector conn0 = new ServerConnector(serverObj);
			conn0.setName("passport_server_0");
			conn0.setHost(this._bindIpAddr0);
			conn0.setPort(this._port0);
			conn0.setIdleTimeout(30000L);
			conn0.setReuseAddress(true);

			if (this._bindIpAddr1 != null && 
				this._port1 > 0) {
				// 创建服务器连接
				ServerConnector conn1 = new ServerConnector(serverObj);
				conn1.setName("passport_server_1");
				conn1.setHost(this._bindIpAddr1);
				conn1.setPort(this._port1);
				conn1.setIdleTimeout(30000L);
				conn1.setReuseAddress(true);

				// 如果有两个连接, 
				connArr = new Connector[] { conn0, conn1 };
			} else {
				// 如果只有一个连接
				connArr = new Connector[] { conn0 };
			}

			// 设置连接
			serverObj.setConnectors(connArr);

			// Servlet 处理器
			ServletContextHandler ctxHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
			ctxHandler.setContextPath("/");
			// 获取处理器字典
			Map<String, HttpServlet> hMap = getServletMap();
			
			hMap.forEach((K, V) -> {
				// 增加 Servlet
				ctxHandler.addServlet(new ServletHolder(V), K);
			});

			// 设置处理器
			serverObj.setHandler(ctxHandler);
			// 启动服务器
			serverObj.start();

			// 记录日志信息
			JettyHttpLog.LOG.info(MessageFormat.format(
				"Jetty HTTP 服务器已经启动, IP 地址 = {0}, 监听端口 = {1}", 
				this._bindIpAddr0, 
				String.valueOf(this._port0)
			));
		} catch (Exception ex) {
			// 记录错误日志
			JettyHttpLog.LOG.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 关闭服务器
	 * 
	 */
	public void stop() {
		if (this._serverObj != null) {
			try {
				// 停止服务器
				this._serverObj.stop();
			} catch (Exception ex) {
				// 记录错误日志
				JettyHttpLog.LOG.error(ex.getMessage(), ex);
			}
		}
	}
}
