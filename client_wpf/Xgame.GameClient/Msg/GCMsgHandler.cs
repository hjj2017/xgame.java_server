using System;

namespace Xgame.GameClient.Msg
{
    /// <summary>
    /// 处理 GC 消息
    /// </summary>
    /// <typeparam name="TGCMsg"></typeparam>
    /// <param name="gcMSG"></param>
    public delegate void GCMsgHandler<TGCMsg>(TGCMsg gcMSG) where TGCMsg : BaseGCMsg;
}
