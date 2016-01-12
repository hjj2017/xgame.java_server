using System;
using System.IO;

namespace Xgame.GamePart.Msg.Type
{
    /// <summary>
    /// Short 类型的消息字段
    /// </summary>
    public sealed class MsgShort : PrimitiveTypeField<short>
    {
        /// <summary>
        /// 空值
        /// </summary>
        public static readonly MsgShort Empty = new MsgShort(0);

        #region 类构造器
        /// <summary>
        /// 类默认构造器
        /// </summary>
        public MsgShort() : base(0)
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="val"></param>
        public MsgShort(short val) : base(val)
        {
        }
        #endregion

        // @Override
        public override void ReadFrom(BinaryReader br)
        {
            if (br != null)
            {
                // 读取 Short 值
                this.Val = br.ReadInt16();
            }
        }

        // @Override
        public override void WriteTo(BinaryWriter bw)
        {
            if (bw != null)
            {
                // 写出 Short 值
                bw.Write(this.Val);
            }
        }
    }
}
