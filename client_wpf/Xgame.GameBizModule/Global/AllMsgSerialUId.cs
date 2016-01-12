using System;

namespace Xgame.GameClient.Global
{
    /// <summary>
    /// 所有的消息序列化 Id
    /// </summary>
    public sealed class AllMsgSerialUId
    {
// 登陆
///////////////////////////////////////////////////////////////////////

        private static short MSG_BASE_LOGIN = 1000;
        public static readonly short CG_LOGIN = ++MSG_BASE_LOGIN;
        public static readonly short GC_LOGIN = ++MSG_BASE_LOGIN;

// 角色
///////////////////////////////////////////////////////////////////////

        private static short MSG_BASE_HUMAN = 1100;
        public static readonly short CG_QUERY_HUMAN_ENTRY_LIST = ++MSG_BASE_HUMAN;
        public static readonly short GC_QUERY_HUMAN_ENTRY_LIST = ++MSG_BASE_HUMAN;
        public static readonly short CG_RAND_HUMAN_NAME = ++MSG_BASE_HUMAN;
        public static readonly short GC_RAND_HUMAN_NAME = ++MSG_BASE_HUMAN;
        public static readonly short CG_CREATE_HUMAN = ++MSG_BASE_HUMAN;
        public static readonly short GC_CREATE_HUMAN = ++MSG_BASE_HUMAN;
        public static readonly short CG_ENTER_HUMAN = ++MSG_BASE_HUMAN;
        public static readonly short GC_ENTER_HUMAN = ++MSG_BASE_HUMAN;

// 聊天
///////////////////////////////////////////////////////////////////////

        private static short MSG_BASE_CHAT = 1400;
        public static readonly short CG_WORLD_CHAT = ++MSG_BASE_CHAT;
        public static readonly short GC_COMM_CHAT = ++MSG_BASE_CHAT;

        /// <summary>
        /// 类默认构造器
        /// </summary>
        private AllMsgSerialUId()
        {
        }
    }
}
