using System;
using System.IO;

namespace Xgame.GamePart.Msg.Type
{
    /// <summary>
    /// Float 类型的消息字段
    /// </summary>
    public sealed class MsgFloat : PrimitiveTypeField<float>
    {
        /// <summary>
        /// 空值
        /// </summary>
        public static readonly MsgFloat Empty = new MsgFloat(0.0f);

        #region 类构造器
        public MsgFloat() : base()
        {
        }

        public MsgFloat(float val) : base(val)
        {
        }
        #endregion

        // @Override
        public override void ReadFrom(BinaryReader br)
        {
            if (br != null)
            {
                // 读取数值
                this.Val = br.ReadSingle();
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
