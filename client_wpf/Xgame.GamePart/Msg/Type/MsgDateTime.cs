using System;
using System.IO;

namespace Xgame.GamePart.Msg.Type
{
    /// <summary>
    /// 日期时间类型的消息字段
    /// </summary>
    public class MsgDateTime : PrimitiveTypeField<DateTime>
    {
        /// <summary>
        /// 1970 年 1 月 1 日 0 点时间
        /// </summary>
        internal static readonly DateTime DATE_1970 = new DateTime(1970, 1, 1, 0, 0, 0);

        #region 类构造器
        /// <summary>
        /// 类默认构造器
        /// </summary>
        public MsgDateTime()
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="dateTime"></param>
        public MsgDateTime(DateTime dateTime) : base(dateTime)
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="ms"></param>
        public MsgDateTime(long ms)
        {
            // 创建新的日期对象并增加毫秒数
            DateTime newDateTime = new DateTime(DATE_1970.Ticks);
            newDateTime.AddMilliseconds(ms);
            // 设置新的日期对象
            this.Val = newDateTime;
        }
        #endregion

        // @Override
        public override void ReadFrom(BinaryReader br)
        {
            if (br != null)
            {
                // 获取时间毫秒数
                long ms = br.ReadInt64();
                // 创建新的日期对象并增加毫秒数
                DateTime newDateTime = new DateTime(DATE_1970.Ticks);
                newDateTime.AddMilliseconds(ms);
                // 设置新的日期对象
                this.Val = newDateTime;
            }
        }

        // @Override
        public override void WriteTo(BinaryWriter bw)
        {
            if (bw != null)
            {
                // 获取时间毫秒数
                TimeSpan TS = new TimeSpan(this.Val.Ticks - DATE_1970.Ticks);
                long ms = (long)TS.TotalMilliseconds;
                // 写出到二进制流
                bw.Write(ms);
            }
        }
    }
}
