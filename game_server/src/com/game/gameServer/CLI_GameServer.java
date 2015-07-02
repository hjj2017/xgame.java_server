package com.game.gameServer;

import com.game.gameServer.framework.App_GameServer;

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
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		System.out.println("GameServer X");
		System.out.println("+-------\n");

		// 创建内核程序
		App_GameServer k = App_GameServer.OBJ;

		// 初始化并启动服务器
		k.init();
		k.startUp();
	}
}
