using System;

using log4net;

namespace Xgame.GameClient.Kernel
{
    /// <summary>
    /// 内核日志
    /// </summary>
    sealed class KernelLog
    {
        /** 单例对象 */
        public static readonly ILog LOG = LogManager.GetLogger("Kernal");

        /// <summary>
        /// 类默认构造器
        /// </summary>
        private KernelLog()
        {
        }
    }
}
