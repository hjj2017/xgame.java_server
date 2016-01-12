using System;

using Xgame.GameClient.Global;
using Xgame.GameClient.Msg;
using Xgame.GamePart.Msg.Type;

namespace Xgame.GameClient.Human.Msg
{
    /// <summary>
    /// 进入角色
    /// </summary>
    public partial class GCEnterHuman : BaseGCMsg
    {
        /** 已就绪 */
        public MsgBool _ready;

        // @Override
        public override short MsgSerialUId
        {
            get
            {
                return AllMsgSerialUId.GC_ENTER_HUMAN;
            }
        }
    }
}
