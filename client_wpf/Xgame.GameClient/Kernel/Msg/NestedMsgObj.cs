using System;
using System.IO;
using Xgame.GamePart.Msg.Type;

namespace Xgame.GameClient.Kernel.Msg
{
    /// <summary>
    /// 内嵌消息对象
    /// </summary>
    public class NestedMsgObj : BaseMsgObj
    {
        // @Override
        public sealed override void ReadFrom(BinaryReader br)
        {
            this.ReadFromImpl(br);
        }

        /// <summary>
        /// 读取实现函数
        /// </summary>
        /// <param name="br"></param>
        protected virtual void ReadFromImpl(BinaryReader br)
        {
            throw new NotImplementedException();
        }

        // @Override
        public sealed override void WriteTo(BinaryWriter bw)
        {
            this.WriteToImpl(bw);
        }

        /// <summary>
        /// 写出实现函数
        /// </summary>
        /// <param name="bw"></param>
        protected virtual void WriteToImpl(BinaryWriter bw)
        {
            throw new NotImplementedException();
        }
    }
}
