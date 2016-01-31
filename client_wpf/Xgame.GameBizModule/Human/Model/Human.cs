using System;

namespace Xgame.GameBizModule.Human.Model
{
    /// <summary>
    /// 游戏角色
    /// </summary>
    public class Human
    {
        /** 单例对象 */
        public static readonly Human OBJ = new Human();

        /** 角色 UId */
        public long _humanUId;
        /** 角色名称 */
        public string _humanName;
        /** 武将模板 Id */
        public int _heroTmplId;

        /// <summary>
        /// 类默认构造器
        /// </summary>
        private Human()
        {
        }

        /// <summary>
        /// 获取游戏玩家
        /// </summary>
        public Player Player
        {
            get
            {
                return Player.OBJ;
            }
        }

        /// <summary>
        /// 获取服务器名称
        /// </summary>
        public string ServerName
        {
            get
            {
                return Player.OBJ._selectedServerName;
            }
        }
    }
}
