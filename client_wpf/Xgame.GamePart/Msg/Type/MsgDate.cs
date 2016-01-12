using System;
using System.IO;

namespace Xgame.GamePart.Msg.Type
{
    /// <summary>
    /// 日期类型的消息字段
    /// </summary>
    public class MsgDate : PrimitiveTypeField<DateTime>
    {
        #region 类构造器
        /// <summary>
        /// 类默认构造器
        /// </summary>
        public MsgDate()
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="date"></param>
        public MsgDate(DateTime date) : base(date.Date)
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="ms"></param>
        public MsgDate(long ms)
        {
            // 创建新的日期对象并增加毫秒数
            DateTime newDate = new DateTime(MsgDateTime.DATE_1970.Ticks);
            newDate.AddMilliseconds(ms);
            // 设置新的日期对象
            this.Val = newDate.Date;
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
                DateTime newDate = new DateTime(MsgDateTime.DATE_1970.Ticks);
                newDate.AddMilliseconds(ms);
                // 设置新的日期对象
                this.Val = newDate.Date;
            }
        }

        // @Override
        public override void WriteTo(BinaryWriter bw)
        {
            if (bw != null)
            {
                // 获取时间毫秒数
                TimeSpan TS = new TimeSpan(this.Val.Date.Ticks - MsgDateTime.DATE_1970.Ticks);
                long ms = (long)TS.TotalMilliseconds;
                // 写出到二进制流
                bw.Write(ms);
            }
        }
    }
}
