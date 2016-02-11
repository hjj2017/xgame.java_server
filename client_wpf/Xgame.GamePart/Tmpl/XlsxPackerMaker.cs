using System;
using System.CodeDom;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using System.Threading;

using Xgame.GamePart.Tmpl.Type;
using Xgame.GamePart.Util;

namespace Xgame.GamePart.Tmpl
{
    /// <summary>
    /// 打包器构建者
    /// </summary>
    internal class XlsxPackerMaker
    {
        /** 打包器字典 */
        private static readonly Dictionary<System.Type, IXlsxPacker> _packerDict = new Dictionary<System.Type, IXlsxPacker>();
        /** 计数器 */
        private static int _counter = 0;

        #region 类构造器
        /// <summary>
        /// 类默认构造器
        /// </summary>
        private XlsxPackerMaker()
        {
        }
        #endregion

        /// <summary>
        /// 构建打包器
        /// </summary>
        /// <param name="byTmplType"></param>
        /// <returns></returns>
        public static IXlsxPacker Make(System.Type byTmplType)
        {
            if (byTmplType == null)
            {
                // 如果参数对象为空, 
                // 则抛出异常!
                throw new ArgumentNullException("byTmplType");
            }
            
            // 获取打包器
            IXlsxPacker packerObj = _packerDict[byTmplType];

            if (packerObj == null)
            {
                try
                {
                    // 构建打包器类定义
                    System.Type packerType = BuildPackerType(byTmplType);
                    // 创建对象
                    packerObj = Activator.CreateInstance(packerType) as IXlsxPacker;
                    // 将打包器添加到字典
                    _packerDict[byTmplType] = packerObj;
                }
                catch (Exception ex)
                {
                    // 包装并抛出异常
                    throw new XlsxTmplError(ex);
                }
            }

            return packerObj;
        }

        /// <summary>
        /// 构建打包器代码
        /// </summary>
        /// <param name="byTmplType"></param>
        /// <returns></returns>
        private static System.Type BuildPackerType(System.Type byTmplType)
        {
            // 断言参数不为空
            Assert.NotNull(byTmplType, "byTmplType");

            // 获取编译器对象
            CodeCompileUnit CC = new CodeCompileUnit();
            // 创建并添加名称空间对象
            CodeNamespace NS = new CodeNamespace(byTmplType.Namespace);
            CC.Namespaces.Add(NS);

            // 计数器 +1
            Interlocked.Increment(ref _counter);
            // 设置类名称
            string packerClazzName = string.Format(
                "XlsxPacker_{0}_{1}", byTmplType.Name, _counter
            );

            // 创建并添加 Packer 类
            CodeTypeDeclaration packerTypeDef = new CodeTypeDeclaration(packerClazzName);
            NS.Types.Add(packerTypeDef);

            // 令 helper 实现 IReadHelper 接口
            packerTypeDef.BaseTypes.Add(typeof(IXlsxPacker));
            // 构建函数文本
            BuildFuncText(packerTypeDef, byTmplType);

            // 创建 C# 编译器
            CodeDomProvider provider = CodeDomProvider.CreateProvider("CSharp");

#if DEBUG
            if (string.IsNullOrEmpty(XlsxTmplServ.OBJ.DebugClazzToDir) == false)
            {
                // C# 源文件绝对目录
                string srcFilePath = string.Format(
                    @"{0}\{1}.cs",
                    XlsxTmplServ.OBJ.DebugClazzToDir,
                    packerClazzName
                );

                // 源文件输出流
                StreamWriter sw = new StreamWriter(srcFilePath, false);
                // 写出源文件
                provider.GenerateCodeFromCompileUnit(CC, sw, new CodeGeneratorOptions());

                sw.Flush();
                sw.Close();
            }
#endif

            // 创建编译参数
            CompilerParameters cp = new CompilerParameters();

            #region 添加对 DLL 的引用
            cp.ReferencedAssemblies.Add("System.dll");
            cp.ReferencedAssemblies.Add(typeof(IXlsxPacker).Assembly.Location);
            cp.ReferencedAssemblies.Add(byTmplType.Assembly.Location);

            for (int i = 0; i < 64; i++)
            {
                if (byTmplType.BaseType == null
                 || byTmplType.BaseType == typeof(BaseXlsxTmpl))
                {
                    // 如果父类为空, 
                    // 或者父类已经是 BaseMsgObj 类型, 
                    // 则跳出循环!
                    break;
                }

                // 获取父类定义并添加到引用集
                System.Type baseType = byTmplType.BaseType;
                cp.ReferencedAssemblies.Add(baseType.Assembly.Location);
            }
            #endregion

            // 只在内存中编译
            cp.GenerateInMemory = true;

            // 编译并获取编译结果
            CompilerResults compileResult = provider.CompileAssemblyFromDom(cp, CC);

            // 编译失败则抛出异常
            if (compileResult.NativeCompilerReturnValue != 0)
            {
                if (compileResult.Errors.HasErrors)
                {
                    // 抛出异常并告知原因
                    throw new XlsxTmplError("编译失败! " + compileResult.Errors[0].ErrorText);
                }
                else
                {
                    // 抛出异常!
                    throw new XlsxTmplError("编译失败! 原因未知...");
                }
            }

            // 获取类定义
            return compileResult.CompiledAssembly.GetType(
                string.Format("{0}.{1}",
                NS.Name,
                packerTypeDef.Name
            ));
        }

        /// <summary>
        /// 构建函数文本
        /// </summary>
        /// <param name="packerTypeDef"></param>
        /// <param name="byTmplType"></param>
        private static void BuildFuncText(CodeTypeDeclaration packerTypeDef, System.Type byTmplType)
        {
            // 断言参数不为空
            Assert.NotNull(packerTypeDef, "packerTypeDef");
            Assert.NotNull(byTmplType, "byTmplType");

            CodeMemberMethod func = new CodeMemberMethod();
            packerTypeDef.Members.Add(func);

            // 
            // 添加 PackUp 函数, 将生成如下代码: 
            // public void PackUp(BaseXlsxTmpl tmplObj) { ... }
            // 
            func.Attributes = MemberAttributes.Public;
            func.ReturnType = new CodeTypeReference(typeof(void));
            func.Name = "PackUp";
            func.Parameters.Add(new CodeParameterDeclarationExpression(typeof(BaseXlsxTmpl), "tmplObj"));

            // 构建函数体
            BuildMapText(func, byTmplType);
        }

        /// <summary>
        /// 构建字典代码
        /// </summary>
        /// <param name="func"></param>
        /// <param name="byTmplType"></param>
        private static void BuildMapText(CodeMemberMethod func, System.Type byTmplType)
        {
            // 断言参数不为空
            Assert.NotNull(func, "func");
            Assert.NotNull(byTmplType, "byTmplType");

            // 获取 '一对一' 或 '一对多' 键值对
            IList<OneToXDefPair> pl = OneToXDefPair.ListAll(byTmplType);

            if (pl == null 
             || pl.Count <= 0)
            {
                // 如果键值对列表为空,
                // 则直接退出!
                return;
            }

            // import ...

            foreach (OneToXDefPair p in pl)
            {
                if (p == null)
                {
                    // 如果键值对为空, 
                    // 则直接跳过!
                    continue;
                }

                // 函数名, 一对一或者一对多
                string funcName = p.OneToOne ? "PackOneToOne" : "PackOneToMany";

                // 获取主键和字典
                string keyObj = (p.KeyDef.MemberType == MemberTypes.Method) ? p.KeyDef.Name + "()" : p.KeyDef.Name;
                string mapObj = (p.MapDef.MemberType == MemberTypes.Method) ? p.MapDef.Name + "()" : p.MapDef.Name;
                //
                // 注意: 主键和字典都有两种情况,
                // 一个是字段类型;
                // 一个是函数类型;
                //

                // 生成如下代码 :
                // AbstractXlsxTmpl.packOneToOne(Boxer.box(O._Id), O, O._IdMap);
                func.Statements.Add(new CodeMethodInvokeExpression(
                    new CodeTypeReferenceExpression(typeof(BaseXlsxCol)), 
                    funcName,
                    new CodeVariableReferenceExpression("O." + keyObj),
                    new CodeVariableReferenceExpression("O"),
                    new CodeVariableReferenceExpression("O." + mapObj)
                ));
            }
        }
    }
}
