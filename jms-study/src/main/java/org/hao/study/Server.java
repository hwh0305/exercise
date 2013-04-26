package org.hao.study;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.hao.study.entry.Person;

import com.alibaba.fastjson.JSON;

public class Server {

    public static void main(String[] args) throws Exception {

         Person[] persons = { new Person("tom", 25, "f", "1231241"), new Person("lily", 24, "m", "6543646") };
        
         ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://10.16.26.57:61616");
         Connection connection = connectionFactory.createConnection();
         Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
         Destination queue = session.createQueue("org.hao.queue");
         Destination topic = session.createTopic("org.hao.topic");
         MessageProducer sender = session.createProducer(queue);
         MessageProducer publisher = session.createProducer(topic);
         for (int i = 0; i < 2; ++i) {
         String msg = JSON.toJSONString(persons[i]);
         TextMessage message = session.createTextMessage(msg);
         message.setIntProperty("ID", i);
         sender.send(message);
         publisher.send(message);
         }
         session.close();
         connection.close();
    }
}
