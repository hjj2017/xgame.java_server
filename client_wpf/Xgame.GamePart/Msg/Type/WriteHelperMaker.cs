using System;
using System.CodeDom;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.IO;
using System.Reflection;

using Xgame.GamePart.Util;

namespace Xgame.GamePart.Msg.Type
{
    /// <summary>
    /// 写出帮助器构建者
    /// </summary>
    internal sealed class WriteHelperMaker
    {
        /** 帮助器字典 */
        private static readonly Dictionary<System.Type, IWriteHelper> _helperObjDict = new Dictionary<System.Type, IWriteHelper>();

        /// <summary>
        /// 构建写出帮助器
        /// </summary>
        /// <param name="byMsgType"></param>
        /// <returns></returns>
        public static IWriteHelper Make(System.Type byMsgType)
        {
            if (byMsgType == null)
            {
                // 如果参数对象为空,
                // 则直接退出!
                return null;
            }

            // 获取写出器
            IWriteHelper helperObj = GetHelperObj(byMsgType);

            if (helperObj == null)
            {
                // 如果写出器为空, 
                // 则需要构建!
                lock (byMsgType)
                {
                    // 注意: 在这里进行二次验证
                    helperObj = GetHelperObj(byMsgType);

                    if (helperObj == null)
                    {
                        // 构建帮助器类定义
                        System.Type helperType = BuildHelperType(byMsgType);
                        // 创建对象实例
                        helperObj = (IWriteHelper)Activator.CreateInstance(helperType);
                        // 添加到帮助器字典
                        PutHelperObj(byMsgType, helperObj);
                    }
                }
            }

            return helperObj;
        }

        /// <summary>
        /// 获取帮助器对象
        /// </summary>
        /// <param name="byMsgType"></param>
        /// <returns></returns>
        private static IWriteHelper GetHelperObj(System.Type byMsgType)
        {
            // 断言参数不为空
            Assert.NotNull(byMsgType, "byMsgObj");

            if (_helperObjDict.ContainsKey(byMsgType))
            {
                return _helperObjDict[byMsgType];
            }
            else
            {
                return null;
            }
        }

        /// <summary>
        /// 保存帮助器到字典
        /// </summary>
        /// <param name="byMsgType"></param>
        /// <param name="helperObj"></param>
        private static void PutHelperObj(System.Type byMsgType, IWriteHelper helperObj)
        {
            // 断言参数不为空
            Assert.NotNull(byMsgType, "byMsgType");
            Assert.NotNull(helperObj, "helperObj");
            // 保存帮助器到字典
            _helperObjDict[byMsgType] = helperObj;
        }

        /// <summary>
        /// 构建帮助器类定义
        /// </summary>
        /// <param name="byMsgType"></param>
        /// <returns></returns>
        private static System.Type BuildHelperType(System.Type byMsgType)
        {
            // 断言参数不为空
            Assert.NotNull(byMsgType, "byMsgType");

            // 获取编译器对象
            CodeCompileUnit CC = new CodeCompileUnit();
            // 创建并添加名称空间对象
            CodeNamespace NS = new CodeNamespace(byMsgType.Namespace);
            CC.Namespaces.Add(NS);
            // 创建并添加 Helper 类
            CodeTypeDeclaration helperTypeDef = new CodeTypeDeclaration("WriteHelper_" + byMsgType.Name);
            NS.Types.Add(helperTypeDef);

            // 令 helper 实现 IWriteHelper 接口
            helperTypeDef.BaseTypes.Add(typeof(IWriteHelper));
            // 构建函数文本
            BuildFuncText(helperTypeDef, byMsgType);

            // 创建 C# 编译器
            CodeDomProvider provider = CodeDomProvider.CreateProvider("CSharp");

#if DEBUG
            string srcFilePath = null;
            // C# 源代码文件
            srcFilePath = @"D:\{0}.cs";
            srcFilePath = string.Format(srcFilePath, helperTypeDef.Name);

            // 源文件输出流
            StreamWriter sw = new StreamWriter(srcFilePath, false);
            // 写出源文件
            provider.GenerateCodeFromCompileUnit(CC, sw, new CodeGeneratorOptions());

            sw.Flush();
            sw.Close();
#endif

            // 创建编译参数
            CompilerParameters cp = new CompilerParameters();

            // 添加对 DLL 的引用
            cp.ReferencedAssemblies.Add("System.dll");
            cp.ReferencedAssemblies.Add(typeof(IWriteHelper).Assembly.Location);
            cp.ReferencedAssemblies.Add(byMsgType.Assembly.Location);

            for (int i = 0; i < 64; i++)
            {
                if (byMsgType.BaseType == null 
                 || byMsgType.BaseType == typeof(BaseMsgObj))
                {
                    // 如果父类为空, 
                    // 或者父类已经是 BaseMsgObj 类型, 
                    // 则跳出循环!
                    break;
                }

                // 获取父类定义并添加到引用集
                System.Type baseType = byMsgType.BaseType;
                cp.ReferencedAssemblies.Add(baseType.Assembly.Location);
            }

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
                    throw new MsgError("编译失败! " + compileResult.Errors[0].ErrorText);
                }
                else
                {
                    // 抛出异常!
                    throw new MsgError("编译失败! 原因未知...");
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
        /// <param name="byMsgType"></param>
        private static void BuildFuncText(CodeTypeDeclaration helperTypeDef, System.Type byMsgType)
        {
            // 断言参数不为空
            Assert.NotNull(helperTypeDef, "helperTypeDef");

            CodeMemberMethod func = new CodeMemberMethod();
            helperTypeDef.Members.Add(func);

            // 
            // 添加 ReadFrom 函数, 将生成如下代码: 
            // public void WriteMsgObjTo(T msgObj, BinaryWriter toBW) { ... }
            // 
            func.Attributes = MemberAttributes.Public;
            func.ReturnType = new CodeTypeReference(typeof(void));
            func.Name = "WriteMsgObjTo";
            func.Parameters.Add(new CodeParameterDeclarationExpression(typeof(BaseMsgObj), "msgObj"));
            func.Parameters.Add(new CodeParameterDeclarationExpression(typeof(BinaryWriter), "toBW"));

            // 获取所有字段数组
            FieldInfo[] fiArr = byMsgType.GetFields();

            if (fiArr == null
             || fiArr.Length <= 0)
            {
                // 如果字段数组为空, 
                // 则直接退出!
                return;
            }

            // 定义临时变量, 并强制转型! 将生成如下代码: 
            // TMsg O;
            // O = (TMsg)msgObj
            func.Statements.Add(new CodeVariableDeclarationStatement(byMsgType, "O"));
            func.Statements.Add(new CodeAssignStatement(
               new CodeVariableReferenceExpression("O"),
               new CodeCastExpression(byMsgType, new CodeVariableReferenceExpression("msgObj"))
            ));

            foreach (FieldInfo fi in fiArr)
            {
                if (fi == null
                 || fi.FieldType.IsSubclassOf(typeof(BaseMsgField)) == false)
                {
                    // 如果字段为空, 
                    // 或者字段类型不是 BaseMsgField 类,
                    // 则直接跳过!
                    continue;
                }

                // 
                // 调用 BaseMsgField 类的 WriteMsgObjTo 函数, 将生成如下代码: 
                // BaseMsgField.WriteMsgObjTo(msgObj._Id, toBW)
                // 
                func.Statements.Add(new CodeMethodInvokeExpression(
                    new CodeTypeReferenceExpression(typeof(BaseMsgField)),
                    "WriteMsgObjTo",
                    new CodeVariableReferenceExpression("O." + fi.Name),
                    new CodeVariableReferenceExpression("toBW")
                ));
            }
        }
    }
}
