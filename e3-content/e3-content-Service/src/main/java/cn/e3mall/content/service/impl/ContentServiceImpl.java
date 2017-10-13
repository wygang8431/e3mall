package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.DataGridResult;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;
import cn.e3mall.redis.impl.JedisClientCluster;
import cn.e3mall.utils.AdItem;
import cn.e3mall.utils.E3Result;
import cn.e3mall.utils.JsonUtils;
@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper mapper;
	@Autowired
	private JedisClientCluster jedisClientCluster;
	//JedisClientCluster实际是一个jedis的工具类，被加上@Repository标签后，可以直接被注入。当然也可以new
	@Override
	public DataGridResult findAllContentByPage(Long categoryId, Integer page, Integer rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> contents = mapper.selectByExample(example);
		//获取分页信息
		PageInfo<TbContent> pageInfo = new PageInfo<>(contents);
		//数据封装成DataGridResult
		DataGridResult result = new DataGridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(contents);
		return result;
	}

	@Override
	public E3Result saveContent(TbContent content) {
		//在对广告内容进行增、删、改时，需要将缓存中的数据删除，保证缓存的数据的实时更新
		jedisClientCluster.hdel("CONTENT_KEY", content.getCategoryId()+"");
		
		mapper.insert(content);
		return E3Result.ok();
	}
	
	public List<AdItem> getContentListByCategoryId(Long categoryId) {
		/*
		 * 业务逻辑：
		 * 先去jedis缓存中拿数据，如果有数据，直接返回数据
		 * 如果没有数据，那么去数据中拿，拿完数据返回并且存入缓存中
		 */
		//在缓存中的数据结构是
		String jsonStr = jedisClientCluster.hget("CONTENT_KEY",categoryId+"");
		if(StringUtils.isNotBlank(jsonStr)){
			List<AdItem> AdItems = JsonUtils.jsonToList(jsonStr,AdItem.class);
			System.out.println("从缓存中读数据");
			return AdItems;
		}
		
		/*去数据库中查询数据
		 */
		//根据查询条件查询出内容
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = mapper.selectByExample(example);
		//将内容封装成想要的结果
		List<AdItem> result = new ArrayList<>();
		AdItem adItem = null;
		for (TbContent content : list) {
			adItem = new AdItem();
			adItem.setAlt(content.getSubTitle());
			adItem.setHref(content.getUrl());
			adItem.setSrc(content.getPic());
			adItem.setSrcB(content.getPic2());
			//设置广告图片的宽高---实际中可将这个常量放在配置文件中，加载过来,如下
			//@Value("${HEIGHT}")
			//private Integer HEIGHT;
			//ad.setHeight(HEIGHT);
			adItem.setWidth(670);
			adItem.setHeight(240);
			
			adItem.setWidthB(550);
			adItem.setHeightB(240);
			result.add(adItem);
		}
		System.out.println("从数据中读数据");
		//将数据存入缓存中
		String json = JsonUtils.objectToJson(result);
		jedisClientCluster.hset("CONTENT_KEY",categoryId+"",json);
		return result;
	}

}
