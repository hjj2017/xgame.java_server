package org.xgame.dbfarmer.agent.impl;

import org.xgame.dbfarmer.base.DBFarmerLeader;

/**
 * 消息队列 - 消费端
 * 主要任务就是监听 RabbitMQ 队列中的消息, 然后交给农民领袖 ( {@link DBFarmerLeader} ) 去执行!
 * 说白了, 就是个帮着拉活儿接单的...
 */
public final class RabbitMQ_C {
}
