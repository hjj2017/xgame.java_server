using System;
using System.IO;

namespace Xgame.GamePart.Msg.Type
{
    /// <summary>
    /// Long 类型的消息字段
    /// </summary>
    public sealed class MsgLong : PrimitiveTypeField<long>
    {
        /// <summary>
        /// 空值
        /// </summary>
        public static readonly MsgLong Empty = new MsgLong(0L);

        #region 类构造器
        /// <summary>
        /// 类默认构造器
        /// </summary>
        public MsgLong() : base(0L)
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="val"></param>
        public MsgLong(long val) : base(val)
        {
        }
        #endregion

        // @Override
        public override void ReadFrom(BinaryReader br)
        {
            if (br != null)
            {
                // 读取 Long 值
                this.Val = br.ReadInt64();
            }
        }

        // @Override
        public override void WriteTo(BinaryWriter bw)
        {
            if (bw != null)
            {
                // 写出 Long 值
                bw.Write(this.Val);
            }
        }
    }
}
