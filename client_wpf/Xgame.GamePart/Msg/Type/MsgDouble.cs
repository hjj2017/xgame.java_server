using System;
using System.IO;

namespace Xgame.GamePart.Msg.Type
{
    /// <summary>
    /// Double 类型的消息字段
    /// </summary>
    public sealed class MsgDouble : PrimitiveTypeField<double>
    {
        /// <summary>
        /// 空值
        /// </summary>
        public static readonly MsgDouble Empty = new MsgDouble(0.0);

        #region 类构造器
        public MsgDouble() : base()
        {
        }

        public MsgDouble(double val) : base(val)
        {
        }
        #endregion

        // @Override
        public override void ReadFrom(BinaryReader br)
        {
            if (br != null)
            {
                // 读取数值
                this.Val = br.ReadDouble();
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
