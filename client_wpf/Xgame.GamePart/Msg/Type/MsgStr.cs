using System;
using System.IO;
using System.Text;

namespace Xgame.GamePart.Msg.Type
{
    /// <summary>
    /// Str 类型的消息字段
    /// </summary>
    public sealed class MsgStr : PrimitiveTypeField<string>
    {
        /// <summary>
        /// 空值
        /// </summary>
        public static readonly MsgStr Empty = new MsgStr(null);

        #region 类构造器
        public MsgStr() : base()
        {
        }

        public MsgStr(string val) : base(val)
        {
        }
        #endregion

        // @Override
        public override void ReadFrom(BinaryReader br)
        {
            if (br == null)
            {
                // 如果参数对象为空, 
                // 则直接退出!
                return;
            }

            // 先读取出字符串长度
            int len = br.ReadInt16();

            if (len <= 0)
            {
                // 如果字符串长度 <= 0
                // 则直接退出!
                this.Val = null;
                return;
            }

            // 读取字节数组
            byte[] byteArr = br.ReadBytes(len);
            // 设置到 Val 属性
            this.Val = Encoding.UTF8.GetString(byteArr);
        }

        // @Override
        public override void WriteTo(BinaryWriter bw)
        {
            if (bw == null)
            {
                // 如果参数对象为空, 
                // 则直接退出!
                return;
            }

            if (string.IsNullOrEmpty(this.Val))
            {
                // 如果字符串为空, 
                // 则长度 = 0
                bw.Write((short)0);
                return;
            }

            // 获取字节数组
            byte[] byteArr = Encoding.UTF8.GetBytes(this.Val);
            // 首先写出数组长度, 
            bw.Write((short)byteArr.Length);
            // 之后写出字节数组
            bw.Write(byteArr);
        }
    }
}
