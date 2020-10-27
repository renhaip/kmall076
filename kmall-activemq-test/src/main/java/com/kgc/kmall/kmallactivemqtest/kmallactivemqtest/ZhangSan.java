package com.kgc.kmall.kmallactivemqtest.kmallactivemqtest;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author shkstart
 * @create 2020-10-26 14:21
 */
public class ZhangSan {

    public static void main(String[] args) {
        ConnectionFactory connect = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,ActiveMQConnection.DEFAULT_PASSWORD,"tcp://192.168.205.138:61616");
        try {
            Connection connection = connect.createConnection();
            connection.start();
            //第一个值表示是否使用事务，如果选择true，第二个值相当于选择0
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic testtopic = session.createTopic("speaking");

            // 将话题的消费者持久化
            MessageConsumer consumer = session.createConsumer(testtopic);

            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    if(message instanceof TextMessage){
                        try {
                            String text = ((TextMessage) message).getText();
                            System.err.println(text+"，张三收到");

                            // session.commit();
                            // session.rollback();
                        } catch (JMSException e) {
                            // TODO Auto-generated catch block
                            // session.rollback();
                            e.printStackTrace();
                        }
                    }
                }
            });


        }catch (Exception e){
            e.printStackTrace();;
        }
    }
}
