using System;

namespace Xgame.GamePart.Msg.Type
{
    /// <summary>
    /// 时间类型的消息字段
    /// </summary>
    public class MsgTime : MsgDateTime
    {
        #region 类构造器
        /// <summary>
        /// 类默认构造器
        /// </summary>
        public MsgTime()
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="time"></param>
        public MsgTime(DateTime time) : base(time)
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="ms"></param>
        public MsgTime(long ms) : base(ms)
        {
        }
        #endregion
    }
}
