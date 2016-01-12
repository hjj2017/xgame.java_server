using System;

namespace Xgame.GameClient.Msg
{
    /// <summary>
    /// GC 消息处理器基础类
    /// </summary>
    /// <typeparam name="TGCMsg"></typeparam>
    public abstract class BaseGCMsgHandler<TGCMsg> : IGCMsgHandler where TGCMsg : BaseGCMsg
    {
        /// <summary>
        /// 处理 GC 消息
        /// </summary>
        /// <param name="gcMSG"></param>
        public void Handle(BaseGCMsg gcMSG)
        {
            this.HandleImpl((TGCMsg)gcMSG);
        }

        /// <summary>
        /// 处理 GC 消息
        /// </summary>
        /// <param name="gcMSG"></param>
        public abstract void HandleImpl(TGCMsg gcMSG);
    }
}
