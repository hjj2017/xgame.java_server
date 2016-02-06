using System;
using System.Collections;
using System.Collections.Generic;

namespace Xgame.GamePart.Tmpl.Type
{
    /// <summary>
    /// Xlsx 数组列表
    /// </summary>
    /// <typeparam name="T"></typeparam>
    public class XlsxArrayList<T> : BaseXlsxCol, IList<T> where T : BaseXlsxCol, new()
    {
        /** 内置列表 */
        private IList<T> _innerList = null;

        #region 类构造器
        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="num">数组元素个数, 默认值 = 1</param>
        public XlsxArrayList(int num = 1)
        {
            num = (num < 1) ? 1 : num;
            this._innerList = new List<T>(num);

            for (int i = 0; i < num; i++)
            {
                this._innerList.Add(new T());
            }
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="coll"></param>
        public XlsxArrayList(IEnumerable<T> coll)
        {
            if (coll == null)
            {
                // 如果参数对象为空, 
                // 则抛出异常!
                throw new ArgumentNullException("coll");
            }

            // 创建内置列表
            this._innerList = new List<T>();

            foreach (T t in coll)
            {
                if (t != null)
                {
                    this._innerList.Add(t);
                }
            }

            if (this._innerList.Count <= 0)
            {
                // 如果内置列表为空, 
                // 则抛出异常!
                throw new ArgumentException("参数列表中没有任何非空对象");
            }
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
    }
}
