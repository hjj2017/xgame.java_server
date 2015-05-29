<?php
/**
 * 自定义工作参数
 *
 * @author jinhaijiang
 * @since 2014/12/18
 *
 */
class MyParam {
    /**
     * 游戏服 PDO
     *
     * @var PDO
     *
     */
    public $_gamePDO = null;

    /**
     * 玩家角色 UUId
     *
     * @var long
     *
     */
    public $_humanUUId = null;

    /**
     * 新的用户原始名称
     *
     * @var String
     *
     */
    public $_newOrigName = null;

    /**
     * 新的用户全名
     *
     * @var String
     *
     */
    public $_newFullName = null;

    /**
     * 旧的用户全名
     *
     * @var String
     *
     */
    public $_oldFullName = null;
}
