using System;

using Xgame.GameBizModule.Global;
using Xgame.GameClient.Msg;
using Xgame.GamePart.Msg.Type;

namespace Xgame.GameBizModule.Human.Msg
{
    /// <summary>
    /// 查询角色入口列表
    /// </summary>
    public partial class CGQueryHumanEntryList : BaseCGMsg
    {
        /** 服务器名称 */
        public MsgStr _serverName = null;

        // @Override
        public override short MsgSerialUId
        {
            get
            {
                return AllMsgSerialUId.CG_QUERY_HUMAN_ENTRY_LIST;
            }
        }
    }
}
