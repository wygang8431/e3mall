package cn.e3mall.service;

import cn.e3mall.pojo.DataGridResult;
import cn.e3mall.pojo.TbItem;

public interface ItemService {
	public TbItem findItemById(Long ItemId);

	public DataGridResult findAllitems(int page, int rows);
}
