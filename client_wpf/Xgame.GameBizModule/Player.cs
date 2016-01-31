using System;

namespace Xgame.GameBizModule
{
    /// <summary>
    /// 游戏玩家
    /// </summary>
    public class Player
    {
        /** 单例对象 */
        public static readonly Player OBJ = new Player();

        /** 用户名称 */
        public string _userName;
        /** 所选择的服务器名称 */
        public string _selectedServerName;

        /// <summary>
        /// 类默认构造器
        /// </summary>
        private Player()
        {
        }
    }
}
