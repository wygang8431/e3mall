package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.pojo.DataGridResult;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.utils.AdItem;
import cn.e3mall.utils.E3Result;

public interface ContentService {


	DataGridResult findAllContentByPage(Long categoryId, Integer page, Integer rows);

	E3Result saveContent(TbContent content);
	
	List<AdItem> getContentListByCategoryId(Long categoryId);
}
