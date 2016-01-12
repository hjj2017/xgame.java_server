using System;

namespace Xgame.GameClient.Msg
{
    /// <summary>
    /// GC 消息处理器
    /// </summary>
    interface IGCMsgHandler
    {
        /// <summary>
        /// 处理 GC 消息
        /// </summary>
        /// <param name="gcMSG"></param>
        void Handle(BaseGCMsg gcMSG);
    }
}
