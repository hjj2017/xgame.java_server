using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;

namespace Xgame.GamePart.Msg.Type
{
    /// <summary>
    /// 消息列表字段
    /// </summary>
    /// <typeparam name="T"></typeparam>
    public sealed class MsgArrayList<T> : BaseMsgField, IList<T> where T : BaseMsgField, new()
    {
        /// <summary>
        /// 内置列表
        /// </summary>
        private List<T> _innerList = null;

        /// <summary>
        /// 类默认构造器
        /// </summary>
        public MsgArrayList()
        {
            this._innerList = new List<T>();
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="itemArr"></param>
        public MsgArrayList(params T[] itemArr)
        {
            if (itemArr != null 
             && itemArr.Length > 0)
            {
                // 如果参数对象不为空, 
                // 则新建列表!
                this._innerList = new List<T>(itemArr);
            }
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="@enum"></param>
        public MsgArrayList(IEnumerable<T> @enum)
        {
            if (@enum != null)
            {
                // 如果参数对象不为空, 
                // 则新建列表!
                this._innerList = new List<T>(@enum);
            }
        }

        // @Override
        public override void ReadFrom(BinaryReader br)
        {
            if (br == null)
            {
                // 如果参数对象为空, 
                // 则直接退出!
                return;
            }

            // 先读出长度
            int count = br.ReadInt16();

            if (count <= 0)
            {
                // 如果列表长度 <= 0, 
                // 则直接退出!
                return;
            }

            for (int i = 0; i < count; i++)
            {
                // 创建新的新条目
                T newItem = new T();
                // 从字节流中读取数据
                newItem.ReadFrom(br);
                // 添加到列表
                this.Add(newItem);
            }
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

            // 获取并写出列表长度
            short count =  (short)this.Count;
            bw.Write(count);

            if (count <= 0)
            {
                // 如果长度 <= 0, 
                // 则直接退出!
                return;
            }

            for (int i = 0; i < count; i++)
            {
                // 获取当前条目
                T currItem = this[i];
                // 写出数据到字节流
                currItem.WriteTo(bw);
            }
        }

        #region IList 接口实现
        // @Override
        public T this[int index]
        {
            get
            {
                return this._innerList[index];
            }

            set
            {
                this._innerList[index] = value;
            }
        }

        // @Override
        public int Count
        {
            get
            {
                return this._innerList.Count;
            }
        }

        // @Override
        public bool IsReadOnly
        {
            get
            {
                return false;
            }
        }

        // @Override
        public void Add(T newItem)
        {
            if (newItem != null)
            {
                this._innerList.Add(newItem);
            }
        }

        // @Override
        public void Clear()
        {
            this._innerList.Clear();
        }

        // @Override
        public bool Contains(T theItem)
        {
            return this._innerList.Contains(theItem);
        }

        // @Override
        public void CopyTo(T[] itemArr, int arrayIndex)
        {
            this._innerList.CopyTo(itemArr, arrayIndex);
        }

        // @Override
        public IEnumerator<T> GetEnumerator()
        {
            return this._innerList.GetEnumerator();
        }

        // @Override
        public int IndexOf(T theItem)
        {
            return this._innerList.IndexOf(theItem);
        }

        // @Override
        public void Insert(int index, T newItem)
        {
            this._innerList.Insert(index, newItem);
        }

        // @Override
        public bool Remove(T delItem)
        {
            return this._innerList.Remove(delItem);
        }

        // @Override
        public void RemoveAt(int index)
        {
            this._innerList.RemoveAt(index);
        }

        // @Override
        IEnumerator IEnumerable.GetEnumerator()
        {
            return this._innerList.GetEnumerator();
        }
        #endregion
    }
}
