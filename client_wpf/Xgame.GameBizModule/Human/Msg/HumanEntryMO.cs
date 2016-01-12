using System;

using Xgame.GamePart.Msg.Type;

namespace Xgame.GameClient.Human.Msg
{
    /// <summary>
    /// 角色入口
    /// </summary>
    public partial class HumanEntryMO : BaseMsgObj
    {
        /** 玩家角色 UId */
        public MsgLong _humanUId;
        /** 玩家角色 UId 字符串 */
        public MsgStr _humanUIdStr;
        /** 角色等级 */
        public MsgInt _humanLevel;
        /** 角色名称 */
        public MsgStr _humanName;
    }
}
