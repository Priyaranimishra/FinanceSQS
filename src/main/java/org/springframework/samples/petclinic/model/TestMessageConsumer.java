package org.springframework.samples.petclinic.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

public class TestMessageConsumer {

	public static void main(String[] args) throws IOException {
		SQSConnection connection = null; 
		
		Properties prop = new Properties();
		InputStream input = null;
		
		try{
			String filename = "config.properties";
			input = TestMessageConsumer.class.getClassLoader().getResourceAsStream(filename);
			
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
			
			//Create a consumer for the sample queue
			MessageConsumer consumer = session.createConsumer(queue);
			
			consumer.setMessageListener(new TestListener());
			
			//Start receiving messages
			connection.start();
			
		}catch(JMSException e){
			e.printStackTrace();
		}
		
	}

}