package org.xgame.dbfarmer.base;

/**
 * 数据库农民经纪人,
 * 主要任务就是监听 RabbitMQ 队列中的消息, 然后交给农民领袖 ( {@link DBFarmerLeader} ) 去执行!
 * 说白了, 就是个帮着拉活儿接单的...
 */
public class DBFarmerBroker {
}
