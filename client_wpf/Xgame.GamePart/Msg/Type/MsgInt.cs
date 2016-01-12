using System;
using System.IO;

namespace Xgame.GamePart.Msg.Type
{
    /// <summary>
    /// Int 类型的消息字段
    /// </summary>
    public sealed class MsgInt : PrimitiveTypeField<int>
    {
        /// <summary>
        /// 空值
        /// </summary>
        public static readonly MsgInt Empty = new MsgInt(0);

        #region 类构造器
        /// <summary>
        /// 类默认构造器
        /// </summary>
        public MsgInt() : base(0)
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="val"></param>
        public MsgInt(int val) : base(val)
        {
        }
        #endregion

        // @Override
        public override void ReadFrom(BinaryReader br)
        {
            if (br != null)
            {
                // 读取 Int 值
                this.Val = br.ReadInt32();
            }
        }

        // @Override
        public override void WriteTo(BinaryWriter bw)
        {
            if (bw != null)
            {
                // 写出 Int 值
                bw.Write(this.Val);
            }
        }
    }
}
