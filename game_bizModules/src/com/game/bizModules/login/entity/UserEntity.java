package com.game.bizModules.login.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.game.core.entity.GenericEntity;

/**
 * 用户实体
 * 
 * @author hjj2019
 * @since 2014/9/15
 * 
 */
@Entity(name = "t_user")
public class UserEntity extends GenericEntity<Long> {
	/** 账号 Id */
	@Id @Column(name = "passport_id", updatable = false)
	public Long _passportId = null;
	/** 用户名 */
	@Column(name = "user_name", length = 256)
	public String _userName = null;
	/** 密码 */
	@Column(name = "passwd", length = 256)
	public String _passwd = null;
	/** 创建时间 */
	@Column(name = "create_time")
	public Long _createTime = null;
	/** 最后登陆时间 */
	@Column(name = "last_login_time")
	public Long _lastLoginTime = null;
	/** 最后登陆 IP 地址 */
	@Column(name = "last_login_ip_addr", length = 64)
	public String _lastLoginIpAddr = null;
}
