using System;

using Xgame.GameClient.Global;
using Xgame.GameClient.Msg;
using Xgame.GamePart.Msg.Type;

namespace Xgame.GameClient.Chat.Msg
{
    /// <summary>
    /// 世界聊天 CG 消息
    /// </summary>
    public partial class CGWorldChat : BaseCGMsg
    {
        /** 聊天内容 */
        public MsgStr _text;

        // @Override
        public override short MsgSerialUId
        {
            get
            {
                return AllMsgSerialUId.CG_WORLD_CHAT;
            }
        }
    }
}
