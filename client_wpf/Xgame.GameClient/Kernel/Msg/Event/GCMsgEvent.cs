using System;

namespace Xgame.GameClient.Kernel.Msg.Event
{
    /// <summary>
    /// GC 消息事件
    /// </summary>
    /// <typeparam name="TGCMsg"></typeparam>
    /// <param name="gcMSG"></param>
    public delegate void GCMsgEvent<TGCMsg>(TGCMsg gcMSG) where TGCMsg : BaseGCMsg;
}
