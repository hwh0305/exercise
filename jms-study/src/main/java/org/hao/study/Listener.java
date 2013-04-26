package org.hao.study;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

public class Listener implements MessageListener {

    private final static ApplicationContext context = new ClassPathXmlApplicationContext(
                                                                                         "classpath*:**/application.xml");

    private final ExecutorService pool = Executors.newFixedThreadPool(10);
    
    @Override
    public void onMessage(final Message message) {
        pool.execute(new Runnable() {
            @Override
            public void run() {
                TextMessage msg = (TextMessage) message;
                try {
                    System.out.println("receive: " + msg.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        context.getBeansOfType(DefaultMessageListenerContainer.class);
        JmsTemplate template = (JmsTemplate) context.getBean("JmsTemplate");
        
        template.send(new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage tm = session.createTextMessage();
                tm.setText("send by spring.");
                tm.setIntProperty("ID", 10);
                return tm;
            }
        });
    }
}
