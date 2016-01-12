using System;

using Xgame.GamePart.Msg.Type;

namespace Xgame.GamePart.Msg
{
    partial class MsgServ
    {
        /// <summary>
        /// 新建消息对象
        /// </summary>
        /// <param name="msgSerialUId"></param>
        /// <typeparam name="TMsg"></typeparam>
        /// <returns></returns>
        public TMsg NewMsgObj<TMsg>(short msgSerialUId) where TMsg : BaseMsgObj
        {
            // 获取消息类型
            System.Type msgObjType = this.GetMsgTypeBySerialUId(msgSerialUId);

            if (msgObjType == null)
            {
                // 如果消息类型定义为空, 
                // 则直接退出!
                return null;
            }

            // 创建消息对象
            object msgObj = Activator.CreateInstance(msgObjType);
            // 强制转型并返回...
            return (TMsg)msgObj;
        }
    }
}
