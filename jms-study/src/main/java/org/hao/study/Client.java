package org.hao.study;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.hao.study.entry.Person;

import com.alibaba.fastjson.JSON;

public class Client {

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://10.16.26.57:61616");
        Connection connection = connectionFactory.createConnection();
        connection.setClientID("study-1");
        connection.start();
        Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
        Destination queue = session.createQueue("org.hao.queue");
        Destination topic = session.createTopic("org.hao.topic");
        MessageConsumer receiver = session.createConsumer(queue);
        MessageConsumer subscriber = session.createDurableSubscriber((Topic) topic, "subscriber-1");
        
        receiver.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                TextMessage msg = (TextMessage) message;
                try {
                    System.out.println("Queue receive msg. id: " + msg.getIntProperty("ID") + ", message: " + msg.getText());
                    Person person = JSON.parseObject(msg.getText(), Person.class);
                    System.out.println(person.toString());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        subscriber.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                TextMessage msg = (TextMessage) message;
                try {
                    System.out.println("Topic receive msg. id: " + msg.getIntProperty("ID") + ", message: " + msg.getText());
                    Person person = JSON.parseObject(msg.getText(), Person.class);
                    System.out.println(person.toString());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        synchronized (session) {
            session.wait();
        }
        session.close();
        connection.stop();
        connection.close();
    }
}
