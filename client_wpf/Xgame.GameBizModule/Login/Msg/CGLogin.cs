using System;

using Xgame.GameBizModule.Global;
using Xgame.GameClient.Msg;
using Xgame.GamePart.Msg.Type;

namespace Xgame.GameBizModule.Login.Msg
{
    /// <summary>
    /// 登陆 CG 消息
    /// </summary>
    public partial class CGLogin : BaseCGMsg
    {
        /** 平台 UId */
        public MsgStr _platformUId;
        /** 登陆字符串, JSON 格式 */
        public MsgStr _loginStr;

        // @Override
        public override short MsgSerialUId
        {
            get
            {
                return AllMsgSerialUId.CG_LOGIN;
            }
        }
    }
}
