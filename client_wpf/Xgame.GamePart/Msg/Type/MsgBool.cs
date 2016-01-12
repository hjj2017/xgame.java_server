using System;
using System.IO;

namespace Xgame.GamePart.Msg.Type
{
    /// <summary>
    /// Bool 类型的消息字段
    /// </summary>
    public sealed class MsgBool : PrimitiveTypeField<bool>
    {
        /// <summary>
        /// 空值
        /// </summary>
        public static readonly MsgBool Empty = new MsgBool(false);

        #region 类构造器
        public MsgBool() : base()
        {
        }

        public MsgBool(bool val) : base(val)
        {
        }
        #endregion

        // @Override
        public override void ReadFrom(BinaryReader br)
        {
            if (br != null)
            {
                // 读取数值
                this.Val = br.ReadBoolean();
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
