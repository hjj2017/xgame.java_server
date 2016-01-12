using System;
using System.IO;

namespace Xgame.GamePart.Msg.Type
{
    /// <summary>
    /// Char 类型的消息字段
    /// </summary>
    public sealed class MsgChar : PrimitiveTypeField<char>
    {
        /// <summary>
        /// 空值
        /// </summary>
        public static readonly MsgChar Empty = new MsgChar('\0');

        #region 类构造器
        public MsgChar() : base()
        {
        }

        public MsgChar(char val) : base(val)
        {
        }
        #endregion

        // @Override
        public override void ReadFrom(BinaryReader br)
        {
            if (br != null)
            {
                // 读取数值
                this.Val = br.ReadChar();
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
