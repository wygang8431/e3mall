package cn.e3mall.activeMQ.producer.test;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class TestSpringActiveMQ {
	//发送消息：点对点模式
	@Test
	public void sendMessageByPTP(){
		//加载配置文件
		ApplicationContext app = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-MQ_producer.xml");
		JmsTemplate jmsTemplate=(JmsTemplate) app.getBean("jmsTemplate");
		Destination destination = (Destination) app.getBean("myQueue");
		
		jmsTemplate.send(destination,new MessageCreator(){
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				
				return session.createTextMessage("hello,springJMS");
			}
		});
	}
	//发送消息：订阅模式
	@Test
	public void sendMessageByPS(){
		//加载配置文件
		ApplicationContext app = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-MQ_producer.xml");
		JmsTemplate jmsTemplate=(JmsTemplate) app.getBean("jmsTemplate");
		Destination destination = (Destination) app.getBean("myTopic");
		
		jmsTemplate.send(destination,new MessageCreator(){
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				
				return session.createTextMessage("hello,springJMS");
			}
		});
	}
}
