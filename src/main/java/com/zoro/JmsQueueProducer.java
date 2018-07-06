package com.zoro;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created on 2018/7/5.
 *
 * @author dubber
 *
 * p2p模式 点对点 queue
 *
 * ① 点对点：point-point；
 *只有一个消费者，没有时间上的相关性，无论生产者运行状态如何。
 *
 */
public class JmsQueueProducer {

    private static ConnectionFactory factory;
    private static final String brokeUrl = "tcp://192.168.116.12:61616";
    static{
        factory = new ActiveMQConnectionFactory(brokeUrl);
    }

    public static void main(String[] args) {
        new JmsQueueProducer().process();
    }

    public void process(){
        Connection conn = null;
        try {
            conn = factory.createConnection();
            // Boolean.FALSE 非事务操作，  Session.AUTO_ACKNOWLEDGE
            Session session = conn.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

            // 创建目的地 queue（队列）
            Destination destination = session.createQueue("zoro_Queue01");
            // 创建发送者
            MessageProducer producer = session.createProducer(destination);
            // 设置传递模式， defalut 持久模式(DeliveryMode.PERSISTENT / 2)
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // 创建发送的信息
            TextMessage message = session.createTextMessage("您好，我是ActiveMQ发送者！");

            producer.send(message);

            System.out.println("消息已发送");

            session.commit();
            session.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
