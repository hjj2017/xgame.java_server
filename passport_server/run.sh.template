#!/bin/bash

# 变量定义
# /////////////////////////////

# 当前目录
curr_dir=`pwd`
# 服务器指纹
finger_print=`echo ${curr_dir}+passportServer+Qwer0987# | md5sum | cut -d ' ' -f1`
# 应用程序类
app_clazz=com.game.passportServer.CLI_Server
# 配置文件目录
config_dir=../all_config/etc
# 依赖包目录
code_lib_dir=../all_config/lib
# 依赖包
all_lib=("${code_lib_dir}/common/*.jar ${code_lib_dir}/jetty/*.jar ${curr_dir}/lib/*.jar")

cp=.

# 添加依赖包
for c in ${all_lib}
do
    for j in ${c}
    do
        cp=${cp}:${j}
    done
done

# 启动服务器
# //////////////////////////////

# 定义日志文件
log_file=log/startUp.log

# 事先杀掉进程
# jps -m | grep "${finger_print}" | grep -v 'grep' | awk '{print $1}' | xargs kill -s 9
# 启动服务器并显示日志
nohup java -cp ${cp} \
    -Ddir=${curr_dir} \
    -Dfinger_print=${finger_print} \
    ${app_clazz} \
    -c ${config_dir}/all_config.json \
    -l ${config_dir}/passport_server.log4j.properties \
    > ${log_file} &
tail -f ${log_file}

