package org.springframework.samples.petclinic.model;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class TestListener implements MessageListener{

	public void onMessage(Message message) {
		
		try{
			System.out.println("Received: !!!! " + ((TextMessage) message).getText());
		} catch(JMSException e){
			e.printStackTrace();
		}

	}

}
