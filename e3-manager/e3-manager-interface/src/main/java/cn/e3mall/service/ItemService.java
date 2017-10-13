package cn.e3mall.service;

import cn.e3mall.pojo.DataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.utils.E3Result;

public interface ItemService {
	public TbItem findItemById(Long ItemId);
	
	public TbItemDesc findItemDescById(Long ItemId);

	public DataGridResult findAllitems(int page, int rows);
	
	public E3Result saveitem(TbItem item,String desc);
}
