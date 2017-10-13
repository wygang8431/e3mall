package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.DataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.redis.impl.JedisClientCluster;
import cn.e3mall.service.ItemService;
import cn.e3mall.utils.E3Result;
import cn.e3mall.utils.IDUtils;
import cn.e3mall.utils.JsonUtils;
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JedisClientCluster jedisDao;
	@Value("${ITEM_EXPIRE_TIME}")
	private int ITEM_EXPIRE_TIME;
	 
	@Override
	public TbItem findItemById(Long ItemId) {
		//先去缓存中查询数据，如果有直接返回
		String itemStr = jedisDao.get("ITEM_DETAIL:BASE:"+ItemId);
		if(StringUtils.isNotBlank(itemStr)){
			System.out.println("从缓存中获取商品详情数据item");
			TbItem item = JsonUtils.jsonToPojo(itemStr, TbItem.class);
			return item;
		}
		//如果没有数据，那么从数据库中获取数据，并且存入redis中
		TbItem item = itemMapper.selectByPrimaryKey(ItemId);
		System.out.println("商品详情缓存数据已过期，从数据库获取数据item");
		String itemStr2 = JsonUtils.objectToJson(item);
		jedisDao.set("ITEM_DETAIL:BASE:"+ItemId, itemStr2);
		//设置过期时间
		jedisDao.expire("ITEM_DETAIL:BASE:"+ItemId, ITEM_EXPIRE_TIME);
		return item;
	}
	
	public TbItemDesc findItemDescById(Long ItemId) {
		//先去缓存中查询数据，如果有直接返回
		String itemDescStr = jedisDao.get("ITEM_DETAIL:DESC:"+ItemId);
		if(StringUtils.isNotBlank(itemDescStr)){
			System.out.println("从缓存中获取商品详情数据desc");
			TbItemDesc itemDesc = JsonUtils.jsonToPojo(itemDescStr, TbItemDesc.class);
			return itemDesc;
		}	
		//如果没有数据，那么从数据库中获取数据，并且存入redis中
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(ItemId);
		
		System.out.println("商品详情缓存数据已过期，从数据库获取数据desc");
		String itemDescStr2 = JsonUtils.objectToJson(itemDesc);
		jedisDao.set("ITEM_DETAIL:DESC:"+ItemId, itemDescStr2);
		//设置过期时间
		jedisDao.expire("ITEM_DETAIL:DESC:"+ItemId, ITEM_EXPIRE_TIME);
		return itemDesc;
	}
	@Override
	public DataGridResult findAllitems(int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		
		//取分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		//返回结果
		DataGridResult result = new DataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}
	//注入jms模板，用于发送MQ消息
	@Autowired
	private JmsTemplate jmsTemplate;
	//注入ActiveMQTopic的父类destination
	@Autowired
	private Destination destination;
	
	@Override
	public E3Result saveitem(TbItem item, String desc) {
		//补全商品信息并保存
		final long itemId = IDUtils.genItemId();
		item.setId(itemId);
		//'商品状态，1-正常，2-下架，3-删除',
		item.setStatus((byte)1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		itemMapper.insert(item);
		//补全描述信息并保存
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		itemDescMapper.insert(itemDesc);
		
		//数据保存成功后，给MQ发送消息，提醒更新索引库
		//发送的内容是itemId
		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				return session.createTextMessage(itemId+"");
			}
		});
		
		return E3Result.ok();
	}
}
