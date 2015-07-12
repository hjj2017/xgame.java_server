package com.game.bizModule.player.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 用户实体
 * 
 * @author hjj2019
 * @since 2014/9/15
 * 
 */
@Entity(name = "t_player")
public class PlayerEntity {
	/** 账号 Id */
	@Id @Column(name = "platform_uid", length = 64, updatable = false)
	public String _platformUId = null;
	/** 用户名 */
	@Column(name = "user_name", length = 32, updatable = false)
	public String _userName = null;
	/** 密码 */
	@Column(name = "user_pass", length = 128)
	public String _userPass = null;
	/** Pf 值 */
	@Column(name = "pf", length = 32, updatable = false)
	public String _pf = null;
	/** 创建时间 */
	@Column(name = "create_time", updatable = false)
	public Long _createTime = null;
	/** 最后登陆时间 */
	@Column(name = "last_login_time")
	public Long _lastLoginTime = null;
	/** 最后登陆 IP 地址 */
	@Column(name = "last_login_ip_addr", length = 64)
	public String _lastLoginIpAddr = null;
}
