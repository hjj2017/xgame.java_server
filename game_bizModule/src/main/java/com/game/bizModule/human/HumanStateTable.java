package com.game.bizModule.human;

/**
 * 玩家角色状态表,
 * 注意: 使用状态表而不是状态变量, 这样使一个一维的线性数值变成了一个二维的数值!
 * 其好处有两个:
 * <ul>
 * <li>第一, 可以比较方便的回查 "过去的" 或者 "其它条目" 的状态;</li>
 * <li>第二, 可以做到线程修改与线程查看的分离;</li>
 * </ul>
 *
 * 假设我们用一个变量 V 来表示所有状态,
 * 关于第一点, 这个我还没有想到比较有说服力的例子;
 * 关于第二点, 假设现在有两个线程: 登陆线程、场景线程, 再假设现在有两个状态: 登陆中, 进场中.
 * 我们可以看看下面这张表:
 *
 * <pre>
 * ===========================
 * 　　　　 | 登陆中 | 进场中
 * ---------------------------
 * 登陆线程 | 读写　 | 读
 * ---------------------------
 * 场景线程 | 读　　 | 读写
 * ===========================
 * </pre>
 *
 * 如果使用的是二维表, 那么我们可以把读写分开! 即,
 * "登陆线程" 对于 "登陆中" 这个变量是可 读写" 的, 而对于 "进场中" 这个状态是 "只读" 的
 *
 * 所以如果只使用一个变量 V 来表示所有状态,
 * 则无法达到这样的效果...
 *
 * @author hjj2019
 * @since 2015/7/11
 *
 */
public class HumanStateTable {
    /** 服务器名称 */
    public String _serverName = null;
    /** 正在执行查询角色入口列表的过程? */
    public boolean _execQueryHumanEntryList = false;
    /** 查询角色入口列表次数 */
    public int _queryHumanEntryListTimes = 0;
    /** 是否有角色? */
    public boolean _hasHuman = false;
    /** 正在执行创建角色过程? */
    public boolean _execCreateHuman = false;
    /** 正在执行进入角色过程? */
    public boolean _execEnterHuman = false;
    /** 是不是第一次登陆?, 注意 : 只有新创建的角色该值 = true */
    public boolean _firstLogin = false;
    /** 角色加载完成 */
    public boolean _humanLoadFromDbOk = false;
    /** 已经进入游戏? */
    public boolean _inGame = false;
}
