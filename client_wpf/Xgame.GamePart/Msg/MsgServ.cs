using System;
using System.Collections.Generic;

namespace Xgame.GamePart.Msg
{
    /// <summary>
    /// 消息服务
    /// </summary>
    public sealed partial class MsgServ
    {
        /// <summary>
        /// 单例对象
        /// </summary>
        public static readonly MsgServ OBJ = new MsgServ();

        /** 消息类型字典 */
        private readonly Dictionary<short, System.Type> _msgTypeDict = new Dictionary<short, System.Type>();

        /// <summary>
        /// 类默认构造器
        /// </summary>
        private MsgServ()
        {
        }

        /// <summary>
        /// 根据消息 UId 获取消息类定义
        /// </summary>
        /// <param name="msgSerialUId"></param>
        /// <returns></returns>
        private System.Type GetMsgTypeBySerialUId(short msgSerialUId)
        {
            if (this._msgTypeDict.ContainsKey(msgSerialUId))
            {
                return this._msgTypeDict[msgSerialUId];
            }
            else
            {
                return null;
            }
        }

        /// <summary>
        /// 根据消息 UId 添加消息类定义
        /// </summary>
        /// <param name="msgSerialUId"></param>
        /// <param name="msgType"></param>
        private void PutMsgTypeBySerialUId(short msgSerialUId, System.Type msgType)
        {
            if (msgSerialUId <= 0
             || msgType == null)
            {
                // 如果参数对象为空, 
                // 则直接退出!
                return;
            }
            else
            {
                // 将消息类型添加到字典
                this._msgTypeDict[msgSerialUId] = msgType;
            }
        }
    }
}
