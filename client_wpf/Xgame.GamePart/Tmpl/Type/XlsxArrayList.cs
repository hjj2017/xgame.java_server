using System;
using System.Collections;
using System.Collections.Generic;

namespace Xgame.GamePart.Tmpl.Type
{
    /// <summary>
    /// Xlsx 数组列表
    /// </summary>
    /// <typeparam name="T"></typeparam>
    public class XlsxArrayList<T> : BaseXlsxCol, IList<T> where T : BaseXlsxCol
    {
        /** 内置列表 */
        private IList<T> _innerList = null;

        #region 类构造器
        /// <summary>
        /// 类默认构造器
        /// </summary>
        public XlsxArrayList()
        {
            this._innerList = new List<T>();
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="capacity"></param>
        public XlsxArrayList(int capacity)
        {
            this._innerList = new List<T>(capacity);
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="coll"></param>
        public XlsxArrayList(IEnumerable<T> coll)
        {
            this._innerList = new List<T>(coll);
        }
        #endregion

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
                return this._innerList.IsReadOnly;
            }
        }

        // @Override
        public void Add(T newItem)
        {
            this._innerList.Add(newItem);
        }

        // @Override
        public void Clear()
        {
            this._innerList.Clear();
        }

        // @Override
        public bool Contains(T oldItem)
        {
            return this._innerList.Contains(oldItem);
        }

        // @Override
        public void CopyTo(T[] array, int arrayIndex)
        {
            this._innerList.CopyTo(array, arrayIndex);
        }

        // @Override
        public IEnumerator<T> GetEnumerator()
        {
            return this._innerList.GetEnumerator();
        }

        // @Override
        public int IndexOf(T oldItem)
        {
            return this._innerList.IndexOf(oldItem);
        }

        // @Override
        public void Insert(int index, T newItem)
        {
            this._innerList.Insert(index, newItem);
        }

        // @Override
        public bool Remove(T oldItem)
        {
            return this._innerList.Remove(oldItem);
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

        // @Override
        protected override void ReadImpl(XlsxRowReadStream fromStream)
        {
            if (fromStream == null 
             || this._innerList == null 
             || this._innerList.Count <= 0)
            {
                // 如果参数对象为空或者内置列表为空, 
                // 则直接退出!
                return;
            }

            foreach (T obj in this._innerList)
            {
                obj.ReadFrom(fromStream);
            }
        }

        /// <summary>
        /// 从 Xlsx 行数据流中读取模版对象, 并返回
        /// </summary>
        /// <typeparam name="TXlsxCol"></typeparam>
        /// <param name="arrList"></param>
        /// <param name="fromStream"></param>
        /// <param name="count"></param>
        /// <returns></returns>
        public static XlsxArrayList<TXlsxCol> ReadXlsxColFrom<TXlsxCol>(XlsxArrayList<TXlsxCol> arrList, XlsxRowReadStream fromStream, int count)
        where TXlsxCol : BaseXlsxCol, new()
        {
            if (arrList == null)
            {
                // 如果消息对象为空, 
                // 则直接新建!
                arrList = new XlsxArrayList<TXlsxCol>(count);
            }

            for (int i = 0; i < count; i++)
            {
                if (i >= arrList.Count)
                {
                    arrList.Add(new TXlsxCol());
                    continue;
                }

                if (arrList[i] == null)
                {
                    arrList[i] = new TXlsxCol();
                    continue;
                }
            }

            // 从二进制流中读取数据
            arrList.ReadFrom(fromStream);
            // 返回消息对象
            return arrList;
        }
    }
}
