using System;
using System.IO;

namespace Xgame.GamePart.Msg.Type
{
    /// <summary>
    /// Byte 类型的消息字段
    /// </summary>
    public sealed class MsgByte : PrimitiveTypeField<byte>
    {
        /// <summary>
        /// 空值
        /// </summary>
        public static readonly MsgByte Empty = new MsgByte(0);

        #region 类构造器
        public MsgByte() : base()
        {
        }

        public MsgByte(byte val) : base(val)
        {
        }
        #endregion

        // @Override
        public override void ReadFrom(BinaryReader br)
        {
            if (br != null)
            {
                // 读取数值
                this.Val = br.ReadByte();
            }
        }

        // @Override
        public override void WriteTo(BinaryWriter bw)
        {
            if (bw != null)
            {
                // 写出数值
                bw.Write(this.Val);
            }
        }
    }
}
