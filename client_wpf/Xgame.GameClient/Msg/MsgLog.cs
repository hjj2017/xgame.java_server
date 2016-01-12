using System;

using log4net;

namespace Xgame.GameClient.Msg
{
    /// <summary>
    /// 消息日志
    /// </summary>
    sealed class MsgLog
    {
        /** 单例对象 */
        public static readonly ILog LOG = LogManager.GetLogger("Msg");

        /// <summary>
        /// 类默认构造器
        /// </summary>
        private MsgLog()
        {
        }
    }
}
