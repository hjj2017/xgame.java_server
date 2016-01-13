using System;

using Xgame.GameBizModule.Global;
using Xgame.GameClient.Msg;
using Xgame.GamePart.Msg.Type;

namespace Xgame.GameBizModule.Human.Msg
{
    /// <summary>
    /// 创建角色完成消息
    /// </summary>
    public class GCCreateHuman : BaseGCMsg
    {
        /** 建角成功? */
        public MsgBool _success;
        /** 失败消息 */
        public MsgStr _errorMsg;

        // @Override
        public override short MsgSerialUId
        {
            get
            {
                return AllMsgSerialUId.CG_CREATE_HUMAN;
            }
        }
    }
}
