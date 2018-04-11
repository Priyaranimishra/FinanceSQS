package org.springframework.samples.petclinic.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

public class TestMessageProducer {

	public static void main(String[] args) throws IOException {

		SQSConnection connection = null; 
		
		Properties prop = new Properties();
		InputStream input = null;
		
		try{
			String filename = "config.properties";
			input = TestMessageProducer.class.getClassLoader().getResourceAsStream(filename);
			
			if(input == null){
				System.out.println("Sorry, unable to find " + filename);
				return;
			}
			
			//load a properties file from class path, inside static method
			prop.load(input);
			
			String accessKey = prop.getProperty("accessKey");
			String secretKey = prop.getProperty("secretKey");
			
			final AWSCredentials cred = new BasicAWSCredentials(accessKey, secretKey);
			
			connection = new SQSConnectionFactory.Builder(Region.getRegion(Regions.US_EAST_1))
					.build().createConnection(cred);
			
			System.out.println("Connected successfully...");
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(prop.getProperty("sqs-queue-name"));
			
			//Create a producer for the sample queue
			MessageProducer producer = session.createProducer(queue);
			
			//Create the text message
			TextMessage message = session.createTextMessage("Hi Lovish");
			
			//Send the message
			producer.send(message);
			System.out.println("Message sent" + message.getJMSMessageID());
			
			connection.close();
			System.out.println("Connection closed successfully..");
			
		}catch(JMSException e){
			e.printStackTrace();
			try{
				connection.close();
				System.out.println("Connection closed..");
			}catch(JMSException e1){
				e1.printStackTrace();
			}
		}
	}

}