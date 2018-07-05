package com.zoro;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.management.remote.JMXConnectorFactory;

/**
 * Created on 2018/7/5.
 *
 * @author dubber
 *
 *
 */
public class JmsActivemqProducer {

    private static ConnectionFactory factory;
    private static final String brokeUrl = "tcp://192.168.116.12:61616";
    static{
        factory = new ActiveMQConnectionFactory(brokeUrl);
    }

    public static void main(String[] args) {
        new JmsActivemqProducer().process();
    }

    public void process(){
        Connection conn = null;
        try {
            conn = factory.createConnection();
            // Boolean.FALSE 非事务操作，  Session.AUTO_ACKNOWLEDGE
            Session session = conn.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);

            // 创建目的地 queue（队列）
            Destination destination = session.createQueue("zoro_Queue01");
            // 创建发送者
            MessageProducer producer = session.createProducer(destination);
            // 创建发送的信息
            TextMessage message = session.createTextMessage("您好，我是ActiveMQ发送者！");

            producer.send(message);

            System.out.println("消息已发送");

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
