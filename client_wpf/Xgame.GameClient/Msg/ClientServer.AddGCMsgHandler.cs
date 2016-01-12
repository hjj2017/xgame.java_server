using System;
using System.Collections.Generic;

namespace Xgame.GameClient.Msg
{
    partial class ClientServer
    {
        /** 消息处理器字典 */
        private IDictionary<System.Type, IDictionary<string, WrappedGCMsgH>> _handlerDict = new Dictionary<System.Type, IDictionary<string, WrappedGCMsgH>>();

        /// <summary>
        /// 添加消息处理器
        /// </summary>
        /// <typeparam name="M"></typeparam>
        /// <typeparam name="H"></typeparam>
        /// <param name="handlerName"></param>
        public void AddGCMsgHandler<M, H>(string handlerName) 
        where M : BaseGCMsg 
        where H : BaseGCMsgHandler<M>, new()
        {
            this.AddGCMsgHandler<M, H>(handlerName, false);
        }

        /// <summary>
        /// 添加消息处理器
        /// </summary>
        /// <typeparam name="M"></typeparam>
        /// <typeparam name="H"></typeparam>
        /// <param name="handlerName"></param>
        /// <param name="reusable"></param>
        public void AddGCMsgHandler<M, H>(string handlerName, bool reusable) 
        where M : BaseGCMsg
        where H : BaseGCMsgHandler<M>, new()
        {
            if (string.IsNullOrEmpty(handlerName))
            {
                // 如果参数对象为空, 
                // 则直接退出!
                return;
            }

            // 获取类型关键字
            System.Type K = typeof(M);
            // 获取内置字典
            IDictionary<string, WrappedGCMsgH> innerDict = this._handlerDict.ContainsKey(K) ? this._handlerDict[K] : null;

            if (innerDict == null)
            {
                // 如果内置字典为空, 
                // 则新建字典!
                innerDict = new Dictionary<string, WrappedGCMsgH>();
                this._handlerDict[K] = innerDict;
            }

            // 设置处理器
            innerDict[handlerName] = new WrappedGCMsgH(
                new H(), reusable
            );
        }

        /// <summary>
        /// 内置的 GC 消息处理器
        /// </summary>
        private class WrappedGCMsgH
        {
            /** GC 消息处理器引用 */
            public IGCMsgHandler _hRef = null;
            /** 是否可以重用? */
            public bool _reusable = false;

            /// <summary>
            /// 类参数构造器
            /// </summary>
            /// <param name="hRef"></param>
            /// <param name="reusable"></param>
            public WrappedGCMsgH(IGCMsgHandler hRef, bool reusable)
            {
                this._hRef = hRef;
                this._reusable = reusable;
            }
        }
    }
}
