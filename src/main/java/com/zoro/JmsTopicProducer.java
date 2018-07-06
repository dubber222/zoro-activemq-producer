package com.zoro;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created on 2018/7/5.
 *
 * @author dubber
 *
 * pub/sub模式 发布/订阅 topic
 *
 * ② 发布订阅 publish/ subscribe
 *   可以有多个消费者，有时间上的相关性，订阅者只能接收订阅之后发布的消息。
 *   但是，JMS允许客户创建持久订阅，一定程度上降低了时间相关性的要求。
 *
 */
public class JmsTopicProducer {

    private static ConnectionFactory factory;
    private static final String brokeUrl = "tcp://192.168.116.12:61616";
    static{
        factory = new ActiveMQConnectionFactory(brokeUrl);
    }

    public static void main(String[] args) {
        new JmsTopicProducer().process();
    }

    public void process(){
        Connection conn = null;
        try {
            conn = factory.createConnection();
            // Boolean.FALSE 非事务操作，  Session.AUTO_ACKNOWLEDGE
            Session session = conn.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

            // 创建目的地 topic（广播）
            Destination destination = session.createTopic("zoro_topic01");
            // 创建发送者
            MessageProducer producer = session.createProducer(destination);
            // 设置传递模式， defalut 持久模式(DeliveryMode.PERSISTENT / 2)
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            // 创建发送的信息
            TextMessage message = session.createTextMessage("大家好，我是ActiveMQ发送者！");

            producer.send(message);

            System.out.println("广播已发出！");

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
