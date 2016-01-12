using System;

using Xgame.GameClient.Global;
using Xgame.GameClient.Msg;
using Xgame.GamePart.Msg.Type;

namespace Xgame.GameClient.Chat.Msg
{
    /// <summary>
    /// 通用聊天 GC 消息
    /// </summary>
    public partial class GCCommChat : BaseGCMsg
    {
        /** 聊天信息来自角色 UId */
        public MsgLong _fromHumanUId;
        /** 聊天信息来自角色 UId 字符串 */
        public MsgStr _fromHumanUIdStr;
        /** 聊天信息来自角色名称 */
        public MsgStr _fromHumanName;
        /** 聊天信息文本 */
        public MsgStr _text;

        // @Override
        public override short MsgSerialUId
        {
            get
            {
                return AllMsgSerialUId.GC_COMM_CHAT;
            }
        }
    }
}
