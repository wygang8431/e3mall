package cn.e3mall.activeMQ.test;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpringActiveMQ {
	@Test
	public void receiveMessage() throws Exception{
		ApplicationContext app = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-MQ_consumer.xml");
		//System.in.read();
	}
}
