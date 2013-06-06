package com.lt.loadtest.loadtestagent.infrastructure.integration.rabbitmq;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class RabbitMQTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		ConnectionFactory connectionFactory = new CachingConnectionFactory();
//		AmqpAdmin admin = new RabbitAdmin(connectionFactory);
//		admin.declareQueue(new Queue("myqueue"));
//		AmqpTemplate template = new RabbitTemplate(connectionFactory);
//		template.convertAndSend("myqueue", "foo1111");
//		System.out.println("================");
//		String foo = (String) template.receiveAndConvert("myqueue");
//		System.out.println("================"+foo);
		
		ApplicationContext context = new GenericXmlApplicationContext("classpath:/rabbit-context.xml");
		AmqpTemplate template = context.getBean(AmqpTemplate.class);
		System.out.println("================");
		//template.convertAndSend("myqueue", "foo1111");
		String foo = (String) template.receiveAndConvert("myqueue");
		System.out.println("================"+foo);
	}

}
