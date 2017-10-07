package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.pojo.DataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;
import cn.e3mall.utils.E3Result;


@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("item/{ItemId}")
	@ResponseBody
	public TbItem findItemById(@PathVariable Long ItemId){
		TbItem item = itemService.findItemById(ItemId);
		return item;
	}
	@RequestMapping("item/list")
	@ResponseBody
	public DataGridResult findAllitems(int page,int rows){
		DataGridResult result= itemService.findAllitems(page,rows);
		return result;	
	}
	@RequestMapping("/item/save")
	@ResponseBody
	public E3Result saveitem(TbItem item,String desc){
		return itemService.saveitem(item, desc);
	}
}
