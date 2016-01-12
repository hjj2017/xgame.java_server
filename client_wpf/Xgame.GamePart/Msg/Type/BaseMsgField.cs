using System;
using System.IO;
using Xgame.GamePart.Util;

namespace Xgame.GamePart.Msg.Type
{
    /// <summary>
    /// 消息字段, 
    /// 这个类是整个消息系统的基石
    /// </summary>
    public abstract class BaseMsgField
    {
        /// <summary>
        /// 从二进制中读取消息所需要的数据
        /// </summary>
        /// <param name="br"></param>
        public abstract void ReadFrom(BinaryReader br);

        /// <summary>
        /// 将消息数据写出到二进制
        /// </summary>
        /// <param name="bw"></param>
        public abstract void WriteTo(BinaryWriter bw);

        /// <summary>
        /// 从二进制流中读取消息对象, 并返回
        /// </summary>
        /// <typeparam name="TMsg"></typeparam>
        /// <param name="msgObj"></param>
        /// <param name="fromBR"></param>
        /// <returns></returns>
        public static TMsg ReadMsgObjFrom<TMsg>(TMsg msgObj, BinaryReader fromBR) where TMsg : BaseMsgField, new()
        {
            if (msgObj == null)
            {
                // 如果消息对象为空, 
                // 则直接新建!
                msgObj = new TMsg();
            }

            // 从二进制流中读取数据
            msgObj.ReadFrom(fromBR);
            // 返回消息对象
            return msgObj;
        }

        /// <summary>
        /// 写出消息对象到二进制流
        /// </summary>
        /// <typeparam name="TMsg"></typeparam>
        /// <param name="msgObj"></param>
        /// <param name="toBW"></param>
        public static void WriteMsgObjTo<TMsg>(TMsg msgObj, BinaryWriter toBW) where TMsg : BaseMsgField
        {
            // 断言参数不为空
            Assert.NotNull(msgObj, "msgObj");
            // 写出消息对象
            msgObj.WriteTo(toBW);
        }
    }
}
