using System;

using Xgame.GameClient.Global;
using Xgame.GameClient.Msg;
using Xgame.GamePart.Msg.Type;

namespace Xgame.GameClient.Human.Msg
{
    /// <summary>
    /// 查询角色入口列表
    /// </summary>
    public partial class GCQueryHumanEntryList : BaseGCMsg
    {
        /** 角色入口列表 */
        public MsgArrayList<HumanEntryMO> _humanEntryList;

        // @Override
        public override short MsgSerialUId
        {
            get
            {
                return AllMsgSerialUId.GC_QUERY_HUMAN_ENTRY_LIST;
            }
        }
    }
}
