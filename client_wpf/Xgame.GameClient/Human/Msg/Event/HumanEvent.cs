using System;

using Xgame.GameClient.Kernel.Msg.Event;

namespace Xgame.GameClient.Human.Msg.Event
{
    /// <summary>
    /// 登陆事件
    /// </summary>
    public sealed class HumanEvent
    {
        /** 单例对象 */
        public static readonly HumanEvent OBJ = new HumanEvent();
        /** GC 查询角色列表 */
        public event GCMsgEvent<GCQueryHumanEntryList> _gcQueryHumanEntryList;
        /** GC 进入角色 */
        public event GCMsgEvent<GCEnterHuman> _gcEnterHuman;

        /// <summary>
        /// 类默认构造器
        /// </summary>
        private HumanEvent()
        {
        }

        /// <summary>
        /// 处理 GC 查询角色入口列表
        /// </summary>
        /// <param name="gcMSG"></param>
        public void OnGCQueryHumanEntryList(GCQueryHumanEntryList gcMSG)
        {
            this._gcQueryHumanEntryList.Invoke(gcMSG);
        }

        /// <summary>
        /// 处理 GC 进入角色
        /// </summary>
        /// <param name="gcMSG"></param>
        public void OnGCEnterHuman(GCEnterHuman gcMSG)
        {
            this._gcEnterHuman.Invoke(gcMSG);
        }
    }
}
