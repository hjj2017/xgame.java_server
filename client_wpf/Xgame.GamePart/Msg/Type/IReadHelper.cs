using System;
using System.IO;

namespace Xgame.GamePart.Msg.Type
{
    /// <summary>
    /// 读取帮助器
    /// </summary>
    public interface IReadHelper
    {
        /// <summary>
        /// 从二进制流中读取消息对象
        /// </summary>
        /// <param name="msgObj"></param>
        /// <param name="fromBR"></param>
        void ReadMsgObjFrom(BaseMsgObj msgObj, BinaryReader fromBR);
    }
}
