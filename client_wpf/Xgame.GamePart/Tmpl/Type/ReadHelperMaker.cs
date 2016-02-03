using System;
using System.CodeDom;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.IO;
using System.Reflection;

using Xgame.GamePart.Util;

namespace Xgame.GamePart.Tmpl.Type
{
    /// <summary>
    /// 读取帮助构建者
    /// </summary>
    sealed class ReadHelperMaker
    {
        /** 帮助器字典 */
        private static readonly Dictionary<System.Type, IReadHelper> _helperObjDict = new Dictionary<System.Type, IReadHelper>();

        #region 类构造器
        /// <summary>
        /// 类默认构造器
        /// </summary>
        private ReadHelperMaker()
        {
        }
        #endregion

        /// <summary>
        /// 构建读取帮助者
        /// </summary>
        /// <param name="byTmplType"></param>
        /// <returns></returns>
        public static IReadHelper Make(System.Type byTmplType)
        {
            if (byTmplType == null)
            {
                // 如果参数对象为空,
                // 则直接退出!
                return null;
            }

            // 获取读取器
            IReadHelper helperObj = GetHelperObj(byTmplType);

            if (helperObj == null)
            {
                // 如果读取器为空, 
                // 则需要构建!
                lock (byTmplType)
                {
                    // 注意: 在这里进行二次验证
                    helperObj = GetHelperObj(byTmplType);

                    if (helperObj == null)
                    {
                        // 构建帮助器类定义
                        System.Type helperType = BuildHelperType(byTmplType);
                        // 创建对象实例
                        helperObj = (IReadHelper)Activator.CreateInstance(helperType);
                        // 添加到帮助器字典
                        PutHelperObj(byTmplType, helperObj);
                    }
                }
            }

            return helperObj;
        }

        /// <summary>
        /// 获取帮助器对象
        /// </summary>
        /// <param name="byTmplType"></param>
        /// <returns></returns>
        private static IReadHelper GetHelperObj(System.Type byTmplType)
        {
            // 断言参数不为空
            Assert.NotNull(byTmplType, "byTmplType");

            if (_helperObjDict.ContainsKey(byTmplType))
            {
                return _helperObjDict[byTmplType];
            }
            else
            {
                return null;
            }
        }

        /// <summary>
        /// 保存帮助器到字典
        /// </summary>
        /// <param name="byTmplType"></param>
        /// <param name="helperObj"></param>
        private static void PutHelperObj(System.Type byTmplType, IReadHelper helperObj)
        {
            // 断言参数不为空
            Assert.NotNull(byTmplType, "byTmplType");
            Assert.NotNull(helperObj, "helperObj");
            // 保存帮助器到字典
            _helperObjDict[byTmplType] = helperObj;
        }

        /// <summary>
        /// 构建帮助器类定义
        /// </summary>
        /// <param name="byTmplType"></param>
        /// <returns></returns>
        private static System.Type BuildHelperType(System.Type byTmplType)
        {
            // 断言参数不为空
            Assert.NotNull(byTmplType, "byTmplType");

            // 获取编译器对象
            CodeCompileUnit CC = new CodeCompileUnit();
            // 创建并添加名称空间对象
            CodeNamespace NS = new CodeNamespace(byTmplType.Namespace);
            CC.Namespaces.Add(NS);
            // 创建并添加 Helper 类
            CodeTypeDeclaration helperTypeDef = new CodeTypeDeclaration("ReadHelper_" + byTmplType.Name);
            NS.Types.Add(helperTypeDef);

            // 令 helper 实现 IReadHelper 接口
            helperTypeDef.BaseTypes.Add(typeof(IReadHelper));
            // 构建函数文本
            BuildFuncText(helperTypeDef, byTmplType);

            // 创建 C# 编译器
            CodeDomProvider provider = CodeDomProvider.CreateProvider("CSharp");

#if DEBUG
            if (string.IsNullOrEmpty(XlsxTmplServ.OBJ.DebugClazzToDir) == false)
            {
                // C# 源文件绝对目录
                string srcFilePath = string.Format(
                    @"{0}\{1}.cs",
                    XlsxTmplServ.OBJ.DebugClazzToDir, 
                    byTmplType.Name
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
            cp.ReferencedAssemblies.Add(typeof(IReadHelper).Assembly.Location);
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
                    throw new XlsxError("编译失败! " + compileResult.Errors[0].ErrorText);
                }
                else
                {
                    // 抛出异常!
                    throw new XlsxError("编译失败! 原因未知...");
                }
            }

            // 获取类定义
            return compileResult.CompiledAssembly.GetType(
                string.Format("{0}.{1}",
                NS.Name,
                helperTypeDef.Name
            ));
        }

        /// <summary>
        /// 构建函数文本
        /// </summary>
        /// <param name="helperTypeDef"></param>
        /// <param name="byXlsxType"></param>
        private static void BuildFuncText(CodeTypeDeclaration helperTypeDef, System.Type byXlsxType)
        {
            // 断言参数不为空
            Assert.NotNull(helperTypeDef, "helperTypeDef");

            CodeMemberMethod func = new CodeMemberMethod();
            helperTypeDef.Members.Add(func);

            // 
            // 添加 ReadTmplObjFrom 函数, 将生成如下代码: 
            // public void ReadTmplObjFrom(BaseXlsxTmpl toTmplObj, XlsxRowReadStream fromStream) { ... }
            // 
            func.Attributes = MemberAttributes.Public;
            func.ReturnType = new CodeTypeReference(typeof(void));
            func.Name = "ReadTmplObjFrom";
            func.Parameters.Add(new CodeParameterDeclarationExpression(typeof(BaseXlsxTmpl), "toTmplObj"));
            func.Parameters.Add(new CodeParameterDeclarationExpression(typeof(XlsxRowReadStream), "fromStream"));

            // 获取所有字段数组
            FieldInfo[] fiArr = byXlsxType.GetFields();

            if (fiArr == null
             || fiArr.Length <= 0)
            {
                // 如果字段数组为空, 
                // 则直接退出!
                return;
            }

            // 定义临时变量, 并强制转型! 将生成如下代码: 
            // BuildingTmpl O;
            // O = (BuildingTmpl)toTmplObj
            func.Statements.Add(new CodeVariableDeclarationStatement(byXlsxType, "O"));
            func.Statements.Add(new CodeAssignStatement(
               new CodeVariableReferenceExpression("O"),
               new CodeCastExpression(byXlsxType, new CodeVariableReferenceExpression("toTmplObj"))
            ));

            foreach (FieldInfo fi in fiArr)
            {
                if (fi == null
                 || fi.FieldType.IsSubclassOf(typeof(BaseXlsxCol)) == false)
                {
                    // 如果字段为空, 
                    // 或者字段类型不是 BaseXlsxCol 类,
                    // 则直接跳过!
                    continue;
                }

                // 
                // 调用 BaseXlsxCol 类的 ReadTmplObjFrom 函数, 
                // 将生成如下代码: 
                // O._Id = BaseXlsxCol.ReadXlsxColFrom(O._Id, fromStream)
                // 
                func.Statements.Add(new CodeAssignStatement(
                    new CodeVariableReferenceExpression("O." + fi.Name),
                    new CodeMethodInvokeExpression(
                        new CodeTypeReferenceExpression(typeof(BaseXlsxCol)),
                        "ReadXlsxColFrom",
                        new CodeVariableReferenceExpression("O." + fi.Name),
                        new CodeVariableReferenceExpression("fromStream")
                    )
                ));
            }
        }
    }
}
