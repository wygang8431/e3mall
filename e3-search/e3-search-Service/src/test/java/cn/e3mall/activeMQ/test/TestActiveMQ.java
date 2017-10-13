package cn.e3mall.activeMQ.test;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.swing.text.DefaultStyledDocument.AttributeUndoableEdit;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class TestActiveMQ {
	@Test
	public void sendMessageOneToOne() throws Exception{
		//创建mq连接工厂
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//获取连接对象
		Connection conn = connectionFactory.createConnection();
		//开启连接
		conn.start();
		//获取session对象
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建消息发送目的地
		Queue queue = session.createQueue("myQueue");
		//创建消息体
		ActiveMQTextMessage message = new ActiveMQTextMessage();
		message.setText("hello,activeMQ异步");
		//创建消息制造者
		MessageProducer producer = session.createProducer(queue);
		//发送消息
		producer.send(message);
		//释放资源
		producer.close();
		session.close();
		conn.close();
	}
	//同步接收消息:点对点
	@Test
	public void receiveMessageSync() throws Exception{
		//创建连接工厂
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//获取连接
		Connection conn = connectionFactory.createConnection();
		//开启连接--不要忘记，否则报错空指针
		conn.start();
		//获取会话对象
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建接收消息的目的地
		Queue queue = session.createQueue("myQueue");
		//创建消息的消费者
		MessageConsumer consumer = session.createConsumer(queue);
		//接收消息

		Message message = consumer.receive(20000);
		//强制转成子类，调用getText方法
		TextMessage textMessage =(TextMessage) message;
		System.out.println(textMessage.getText());	
		
		//释放资源
		consumer.close();
		session.close();
		conn.close();
	}
	//异步监听模式接收消息：点对点
	@Test
	public void receiveMessageAsync() throws Exception{
		//创建连接工厂
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//获取连接
		Connection conn = connectionFactory.createConnection();
		//开启连接
		conn.start();
		//创建session
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建接收消息区域
		Queue queue = session.createQueue("myQueue");
		//创建消息消费者
		MessageConsumer consumer = session.createConsumer(queue);
		//消息监听
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				if(message instanceof TextMessage){
					TextMessage textMessage =(TextMessage)message;
					try {
						System.out.println(textMessage.getText());
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		//等待输入，使程序处于等待消息的状态
		System.in.read();
		//释放资源
		consumer.close();
		session.close();
		conn.close();
	}
}
