package cn.e3mall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("{itemId}")
	public String showItemDetail(@PathVariable Long itemId,Model model){
		TbItem item = itemService.findItemById(itemId);
		TbItemDesc desc = itemService.findItemDescById(itemId);
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", desc);
		return "item";
	}
}
