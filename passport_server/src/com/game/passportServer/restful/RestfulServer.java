package com.game.passportServer.restful;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;

import com.game.passportServer.restful.servlet.Servlet_GetPassportInfo;

/**
 * Restful 服务器, 
 * 启动后, 可以使用如下地址进行测试 :
 * http://127.0.0.1:8007/get_passport_info?qid=1001&pf=wan&game_server_id=1
 * 
 * @author jinhaijiang
 * @since 2015/2/9
 * 
 */
public class RestfulServer {
	/** 资源类列表 */
	private static final List<Class<?>> RES_CLAZZ_LIST = Arrays.asList(
		Servlet_GetPassportInfo.class
	);

	/** 单例对象 */
	public static final RestfulServer OBJ = new RestfulServer();
	/** 绑定服务器 Ip 地址 */
	public String _bindIp = "127.0.0.1";
	/** 服务器端口号 */
	public int _port = 8002;
	/** Netty 对象 */
	private NettyJaxrsServer _netty;

	/**
	 * 类默认构造器
	 * 
	 */
	private RestfulServer() {
	}

	/**
	 * 启动服务器
	 * 
	 */
	public void start() {
		// 获取资源名称列表
		List<String> resNameList = RES_CLAZZ_LIST.stream()
			.map(c -> c.getName())
			.collect(Collectors.toList());

		ResteasyDeployment deployment = new ResteasyDeployment();
		deployment.setSecurityEnabled(true);
		deployment.setResourceClasses(resNameList);
		
		_netty = new NettyJaxrsServer();
		_netty.setRootResourcePath(_bindIp);
		_netty.setPort(_port);
		_netty.setSecurityDomain(null);
		_netty.setDeployment(deployment);
		// 不要保持连接, 否则文件连接会被占满 ...
		_netty.setKeepAlive(false);
		_netty.start();
		// 记录日志信息
		RestfulLog.LOG.info(MessageFormat.format(
			"Restful 服务器已经启动, IP 地址 = {0}, 监听端口 = {1}", 
			_bindIp, String.valueOf(_port)
		));
	}

	/**
	 * 关闭服务器
	 * 
	 */
	public void stop() {
		if (_netty == null) {
			return;
		}

		try {
			_netty.stop();
		} catch (Exception ex) {
			// 记录错误日志
			RestfulLog.LOG.error(ex.getMessage(), ex);
		}

		_netty = null;
	}
}
