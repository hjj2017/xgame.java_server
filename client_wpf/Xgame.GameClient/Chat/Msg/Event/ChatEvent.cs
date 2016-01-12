using System;

using Xgame.GameClient.Kernel.Msg.Event;

namespace Xgame.GameClient.Chat.Msg.Event
{
    /// <summary>
    /// 聊天事件
    /// </summary>
    public sealed class ChatEvent
    {
        /** 单例对象 */
        public static readonly ChatEvent OBJ = new ChatEvent();
        /** GC 查询角色列表 */
        public event GCMsgEvent<GCCommChat> _gcCommChat;

        /// <summary>
        /// 类默认构造器
        /// </summary>
        private ChatEvent()
        {
        }

        /// <summary>
        /// 处理 GC 消息
        /// </summary>
        /// <param name="gcMSG"></param>
        public void OnGCCommChat(GCCommChat gcMSG)
        {
            this._gcCommChat.Invoke(gcMSG);
        }
    }
}
