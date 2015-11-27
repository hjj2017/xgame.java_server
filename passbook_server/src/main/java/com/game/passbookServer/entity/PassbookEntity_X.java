package com.game.passbookServer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import net.sf.json.JSONObject;

/**
 * Passbook 数据
 * 
 * @author hjj2019
 * @since 2015/2/9
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
public class PassbookEntity_X {
	/** 平台 UUId */
	@Id @Column(name = "platform_uid_str", length = 64)
	public String _platformUIdStr = null;

	/** 创建时间 */
	@Column(name = "create_time", updatable = false)
	public Long _createTime = 0L;

	/** 平台名称 */
	@Column(name = "pf", length = 16, updatable = false)
	public String _pf = null;

	/** 最后登录时间 */
	@Column(name = "last_login_time")
	public Long _lastLoginTime = 0L;

	/** 最后登录服务器 Id */
	@Column(name = "last_game_server_id")
	public Integer _lastGameServerId = 0;

	/**
	 * 类默认构造器
	 * 
	 */
	public PassbookEntity_X() {
	}

	/**
	 * 获取平台 UId 字符串
	 * 
	 * @return
	 * 
	 */
	@Transient
	public String getPlatfromUIdStr() {
		return this._platformUIdStr;
	}

	/**
	 * 将属性写入 JSON 对象
	 * 
	 * @param jsonObj 
	 * 
	 */
	@Transient
	public void writeJsonObj(JSONObject jsonObj) {
		if (jsonObj == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		jsonObj.put("platformUUId", this._platformUIdStr);
		jsonObj.put("createTime", this._createTime);
		jsonObj.put("pf", this._pf);
		jsonObj.put("lastLoginTime", this._lastLoginTime);
		jsonObj.put("lastGameServerId", this._lastGameServerId);
	}

	/**
	 * 获取分表数据实体
	 * 
	 * @return
	 * 
	 */
	public PassbookEntity_X getSplitEntityObj() {
		if (this._platformUIdStr == null ||
			this._platformUIdStr.isEmpty()) {
			// 如果平台 UUId 为空, 
			// 则直接退出!
			return null;
		}

		// 获取最后一个字符
		char lastChar = this._platformUIdStr.charAt(this._platformUIdStr.length() - 1);
		lastChar = Character.toLowerCase(lastChar);
		// 获取 passbook 数据
		PassbookEntity_X pe = null;

		// 创建 passbook 数据
		switch (lastChar) {
		// 10 进制数
		case '0': pe = new PassbookEntity_0(); break;
		case '1': pe = new PassbookEntity_1(); break;
		case '2': pe = new PassbookEntity_2(); break;
		case '3': pe = new PassbookEntity_3(); break;
		case '4': pe = new PassbookEntity_4(); break;
		case '5': pe = new PassbookEntity_5(); break;
		case '6': pe = new PassbookEntity_6(); break;
		case '7': pe = new PassbookEntity_7(); break;
		case '8': pe = new PassbookEntity_8(); break;
		case '9': pe = new PassbookEntity_9(); break;
		// 16 进制数
		case 'a': pe = new PassbookEntity_0(); break;
		case 'b': pe = new PassbookEntity_1(); break;
		case 'c': pe = new PassbookEntity_2(); break;
		case 'd': pe = new PassbookEntity_3(); break;
		case 'e': pe = new PassbookEntity_4(); break;
		case 'f': pe = new PassbookEntity_5(); break;
		default: return null;
		}

		pe._createTime = this._createTime;
		pe._lastGameServerId = this._lastGameServerId;
		pe._lastLoginTime = this._lastLoginTime;
		pe._pf = this._pf;
		pe._platformUIdStr = this._platformUIdStr;

		return pe;
	}

	/**
	 * 获取分表实体类
	 * 
	 * @param platformUUId
	 * @return
	 * 
	 */
	public static Class<? extends PassbookEntity_X> getSplitEntityClazz(String platformUUId) {
		if (platformUUId == null || 
			platformUUId.isEmpty()) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		// 获取最后的字符
		char lastChar = platformUUId.charAt(platformUUId.length() - 1);
		lastChar = Character.toLowerCase(lastChar);

		switch (lastChar) {
		// 10 进制数
		case '0': return PassbookEntity_0.class;
		case '1': return PassbookEntity_1.class;
		case '2': return PassbookEntity_2.class;
		case '3': return PassbookEntity_3.class;
		case '4': return PassbookEntity_4.class;
		case '5': return PassbookEntity_5.class;
		case '6': return PassbookEntity_6.class;
		case '7': return PassbookEntity_7.class;
		case '8': return PassbookEntity_8.class;
		case '9': return PassbookEntity_9.class;
		// 16 进制数
		case 'a': return PassbookEntity_0.class;
		case 'b': return PassbookEntity_1.class;
		case 'c': return PassbookEntity_2.class;
		case 'd': return PassbookEntity_3.class;
		case 'e': return PassbookEntity_4.class;
		case 'f': return PassbookEntity_5.class;
		default: return null;
		}
	}
}
