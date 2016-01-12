using System;

using Xgame.GameBizModule.Global;
using Xgame.GameClient.Msg;
using Xgame.GamePart.Msg.Type;

namespace Xgame.GameBizModule.Login.Msg
{
    /// <summary>
    /// 登陆 GC 消息
    /// </summary>
    public partial class GCLogin : BaseGCMsg
    {
        /** 登陆成功? */
        public MsgBool _success;

        // @Override
        public override short MsgSerialUId
        {
            get
            {
                return AllMsgSerialUId.GC_LOGIN;
            }
        }
    }
}
