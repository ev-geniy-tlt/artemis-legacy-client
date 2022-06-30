package com.haulmont;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQDestination;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.*;

public class Main {
    public static void main(String[] args) throws JMSException, InterruptedException {
        try (ActiveMQConnectionFactory factory = new ActiveMQJMSConnectionFactory("tcp://localhost:61616", "admin", "admin")) {
            Destination destination = ActiveMQDestination.fromPrefixedName("queue://crm-users");

            try (Connection conn = factory.createConnection()) {
                Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageConsumer consumer = session.createConsumer(destination);
                consumer.setMessageListener(message -> {
                    try {
                        System.out.println("Received Message: " + message.getBody(String.class));
                    } catch (JMSException e) {
                        throw new RuntimeException(e);
                    }
                });
                conn.start();


                Thread.sleep(1000000);
            }
        }
    }
}