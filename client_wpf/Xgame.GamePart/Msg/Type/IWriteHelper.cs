using System;
using System.IO;

namespace Xgame.GamePart.Msg.Type
{
    /// <summary>
    /// 写出帮助器
    /// </summary>
    public interface IWriteHelper
    {
        /// <summary>
        /// 将消息对象写出到二进制流中
        /// </summary>
        /// <param name="msgObj"></param>
        /// <param name="toBW"></param>
        void WriteMsgObjTo(BaseMsgObj msgObj, BinaryWriter toBW);
    }
}
