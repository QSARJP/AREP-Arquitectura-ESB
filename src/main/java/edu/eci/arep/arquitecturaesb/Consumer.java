/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arep.arquitecturaesb;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;




/**
 *
 * @author 2100448
 */
public class Consumer implements Runnable, ExceptionListener {
        public static void main(String[] args) 
        {
            Consumer ex = new Consumer();
            ex.run();
        }
    
        public void run() {
            try {
 
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://ec2-3-80-212-217.compute-1.amazonaws.com:61616?jms.useAsyncSend=true");
 
                // Create a Connection
                Connection connection = connectionFactory.createConnection("smx","smx");
                connection.start();
 
                connection.setExceptionListener(this);
 
                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 
                // Create the destination (Topic or Queue)
                Destination destination = session.createQueue("test.foo");
 
                // Create a MessageConsumer from the Session to the Topic or Queue
                MessageConsumer consumer = session.createConsumer(destination);
 
                // Wait for a message
                Message message = consumer.receive(1000);
 
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                    System.out.println("Received: " + text);
                } else {
                    System.out.println("Received: " + message);
                }
 
                consumer.close();
                session.close();
                connection.close();
            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
        }
 
        public synchronized void onException(JMSException ex) {
            System.out.println("JMS Exception occured.  Shutting down client.");
        }
}
    

