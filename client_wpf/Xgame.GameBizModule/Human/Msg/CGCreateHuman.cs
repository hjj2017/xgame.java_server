using System;

using Xgame.GameBizModule.Global;
using Xgame.GameClient.Msg;
using Xgame.GamePart.Msg.Type;

namespace Xgame.GameBizModule.Human.Msg
{
    /// <summary>
    /// 创建角色消息
    /// </summary>
    public class CGCreateHuman : BaseCGMsg
    {
        /** 服务器名称 */
        public MsgStr _serverName;
        /** 角色名称 */
        public MsgStr _humanName;
        /** 所使用的模版 Id */
        public MsgInt _usingTmplId;

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
