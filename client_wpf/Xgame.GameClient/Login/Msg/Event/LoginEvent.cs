using System;

using Xgame.GameClient.Kernel.Msg.Event;

namespace Xgame.GameClient.Login.Msg.Event
{
    /// <summary>
    /// 登陆事件
    /// </summary>
    public sealed class LoginEvent
    {
        /** 单例对象 */
        public static readonly LoginEvent OBJ = new LoginEvent();
        /** GC 登陆消息 */
        public event GCMsgEvent<GCLogin> _gcLogin;

        /// <summary>
        /// 类默认构造器
        /// </summary>
        private LoginEvent()
        {
        }

        /// <summary>
        /// 处理 GC 登陆消息
        /// </summary>
        /// <param name="gcMSG"></param>
        public void OnGCLogin(GCLogin gcMSG)
        {
            this._gcLogin.Invoke(gcMSG);
        }
    }
}
