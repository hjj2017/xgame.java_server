using System;

namespace Xgame.GameClient.Msg
{
    /// <summary>
    /// GC 消息处理适配器
    /// </summary>
    /// <typeparam name="TGCMsg"></typeparam>
    public class GCMsgHandleAdapter<TGCMsg> : IGCMsgHandler<TGCMsg> where TGCMsg : class
    {
        /// <summary>
        /// 类默认构造器
        /// </summary>
        public GCMsgHandleAdapter() : this(true)
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="onlyOnce"></param>
        public GCMsgHandleAdapter(bool onlyOnce)
        {
            this.OnlyOnce = onlyOnce;
        }

        // @Override
        public bool OnlyOnce
        {
            private set;
            get;
        }

        // @Override
        public virtual void Handle(TGCMsg gcMSG)
        {
        }
    }
}
