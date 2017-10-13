package cn.e3mall.jedis.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class TestJedis {
	@Test
	public void singleRedis(){//单机单个连接版
		Jedis jedis = new Jedis("192.168.25.128", 6379);
		jedis.set("name", "张三丰，jedis");
		System.out.println(jedis.get("name"));
	}
	@Test
	public void singleRedisPool(){//单机连接池版
		//创建连接池配置对象
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		//设置最大连接数
		poolConfig.setMaxTotal(2000);
		poolConfig.setMaxIdle(20);
		//创建连接池
		JedisPool jedisPool = new JedisPool(poolConfig, "192.168.25.128", 6379);
		//从连接池中获取连接
		Jedis jedis = jedisPool.getResource();
		jedis.set("name", "张三丰，jedisPool");
		System.out.println(jedis.get("name"));
	}
	@Test
	public void clusterRedis(){//集群版
		//创建连接池配置对象
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		//设置最大连接数
		poolConfig.setMaxTotal(2000);
		poolConfig.setMaxIdle(20);
		//创建节点
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.128", 7001));
		nodes.add(new HostAndPort("192.168.25.128", 7002));
		nodes.add(new HostAndPort("192.168.25.128", 7003));
		nodes.add(new HostAndPort("192.168.25.128", 7004));
		nodes.add(new HostAndPort("192.168.25.128", 7005));
		nodes.add(new HostAndPort("192.168.25.128", 7006));
		JedisCluster jedisCluster = new JedisCluster(nodes, poolConfig);
		jedisCluster.set("name", "张三丰，jedisCluster");
		System.out.println(jedisCluster.get("name"));
	}
	@Test
	public void TestXmlJedis(){
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-jedis.xml");
		JedisPool jedisPool = (JedisPool) applicationContext.getBean("jedisPool");
		//从连接池中获取连接
		Jedis jedis = jedisPool.getResource();
		jedis.set("name", "张三丰，jedisPoolXml");
		System.out.println(jedis.get("name"));
	}
	@Test
	public void TestXmlClusterJedis(){
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-jedis.xml");
		JedisCluster jedisCluster = (JedisCluster) applicationContext.getBean("jedisCluster");
		//从连接池中获取连接
		jedisCluster.set("name", "张三丰，jedisClusterXml");
		System.out.println(jedisCluster.get("name"));
	}
}
