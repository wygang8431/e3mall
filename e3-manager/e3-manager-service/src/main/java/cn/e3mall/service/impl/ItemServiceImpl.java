package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.DataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
import cn.e3mall.utils.E3Result;
import cn.e3mall.utils.IDUtils;
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Override
	public TbItem findItemById(Long ItemId) {
		TbItem item = itemMapper.selectByPrimaryKey(ItemId);
		return item;
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
	@Override
	public E3Result saveitem(TbItem item, String desc) {
		//补全商品信息并保存
		long itemId = IDUtils.genItemId();
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
		
		
		return E3Result.ok();
	}

}
