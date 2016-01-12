using System;

using log4net;

namespace Xgame.GameClient.Login
{
    /// <summary>
    /// 登陆日志
    /// </summary>
    sealed class LoginLog
    {
        /** 单例对象 */
        public static readonly ILog LOG = LogManager.GetLogger("Login");

        /// <summary>
        /// 类默认构造器
        /// </summary>
        private LoginLog()
        {
        }
    }
}
