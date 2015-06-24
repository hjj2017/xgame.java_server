#!/bin/bash

# 变量定义
# /////////////////////////////

# 当前目录
curr_dir=`pwd`
# 服务器指纹
finger_print=`echo ${curr_dir}+App_PassportServer+Qwer0987# | md5sum | cut -d ' ' -f1`
# 应用程序类
app_clazz=com.game.passportServer.App_PassportServer
# 依赖包目录
code_libs_dir=../libs/java
# 依赖包
all_libs=("${code_libs_dir}/*.jar ${code_libs_dir}/restful/*.jar ${curr_dir}/libs/*.jar")

cp=.

# 添加依赖包
for c in ${all_libs}
do
    for j in ${c}
    do
        cp=${cp}:${j}
    done
done

# 启动服务器
# //////////////////////////////

# 定义日志文件
log_file=logs/startUp.log

# 事先杀掉进程
jps -m | grep "${finger_print}" | grep -v 'grep' | awk '{print $1}' | xargs kill -s 9
# 启动服务器并显示日志
nohup java -cp ${cp} ${app_clazz} ${curr_dir}/../all_config.json -Ddir=${curr_dir} -Dfinger_print=${finger_print} > ${log_file} &
tail -f ${log_file}
