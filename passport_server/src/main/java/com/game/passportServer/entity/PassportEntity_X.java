package com.game.passportServer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import net.sf.json.JSONObject;

/**
 * Passport 数据
 * 
 * @author hjj2019
 * @since 2015/2/9
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
public class PassportEntity_X {
	/** 平台 UUId */
	@Id @Column(name = "platform_uuid")
	public String _platformUUId = null;

	/** 创建时间 */
	@Column(name = "create_time", updatable = false)
	public Long _createTime = 0L;

	/** 平台名称 */
	@Column(name = "pf", updatable = false)
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
	public PassportEntity_X() {
	}

	/**
	 * 获取 passportId, 其实 passportId 就是 qId
	 * 
	 * @return
	 * 
	 */
	@Transient
	public String getPlatfromUUId() {
		return this._platformUUId;
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

		jsonObj.put("platformUUId", this._platformUUId);
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
	public PassportEntity_X getSplitEntityObj() {
		if (this._platformUUId == null || 
			this._platformUUId.isEmpty()) {
			// 如果平台 UUId 为空, 
			// 则直接退出!
			return null;
		}

		// 获取最后一个字符
		char lastChar = this._platformUUId.charAt(this._platformUUId.length() - 1);
		lastChar = Character.toLowerCase(lastChar);
		// 获取 passport 数据
		PassportEntity_X pe = null;

		// 创建 passport
		switch (lastChar) {
		// 10 进制数
		case '0': pe = new PassportEntity_0(); break;
		case '1': pe = new PassportEntity_1(); break;
		case '2': pe = new PassportEntity_2(); break;
		case '3': pe = new PassportEntity_3(); break;
		case '4': pe = new PassportEntity_4(); break;
		case '5': pe = new PassportEntity_5(); break;
		case '6': pe = new PassportEntity_6(); break;
		case '7': pe = new PassportEntity_7(); break;
		case '8': pe = new PassportEntity_8(); break;
		case '9': pe = new PassportEntity_9(); break;
		// 16 进制数
		case 'a': pe = new PassportEntity_0(); break;
		case 'b': pe = new PassportEntity_1(); break;
		case 'c': pe = new PassportEntity_2(); break;
		case 'd': pe = new PassportEntity_3(); break;
		case 'e': pe = new PassportEntity_4(); break;
		case 'f': pe = new PassportEntity_5(); break;
		default: return null;
		}

		pe._createTime = this._createTime;
		pe._lastGameServerId = this._lastGameServerId;
		pe._lastLoginTime = this._lastLoginTime;
		pe._pf = this._pf;
		pe._platformUUId = this._platformUUId;

		return pe;
	}

	/**
	 * 获取分表实体类
	 * 
	 * @param platformUUId
	 * @return
	 * 
	 */
	public static Class<? extends PassportEntity_X> getSplitEntityClazz(String platformUUId) {
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
		case '0': return PassportEntity_0.class;
		case '1': return PassportEntity_1.class;
		case '2': return PassportEntity_2.class;
		case '3': return PassportEntity_3.class;
		case '4': return PassportEntity_4.class;
		case '5': return PassportEntity_5.class;
		case '6': return PassportEntity_6.class;
		case '7': return PassportEntity_7.class;
		case '8': return PassportEntity_8.class;
		case '9': return PassportEntity_9.class;
		// 16 进制数
		case 'a': return PassportEntity_0.class;
		case 'b': return PassportEntity_1.class;
		case 'c': return PassportEntity_2.class;
		case 'd': return PassportEntity_3.class;
		case 'e': return PassportEntity_4.class;
		case 'f': return PassportEntity_5.class;
		default: return null;
		}
	}
}
