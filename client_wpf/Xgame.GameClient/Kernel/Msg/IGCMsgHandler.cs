using System;

namespace Xgame.GameClient.Kernel.Msg
{
    /// <summary>
    /// GC 消息处理器
    /// </summary>
    public interface IGCMsgHandler<TGCMsg> where TGCMsg : class
    {
        /// <summary>
        /// 处理 GC 消息
        /// </summary>
        /// <param name="gcMSG"></param>
        void Handle(TGCMsg gcMSG);
    }
}
