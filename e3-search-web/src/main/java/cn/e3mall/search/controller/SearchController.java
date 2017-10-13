package cn.e3mall.search.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.search.pojo.ResultModel;
import cn.e3mall.search.service.SearchItemService;

@Controller
public class SearchController {
	@Autowired
	private SearchItemService itemService;
	
	@RequestMapping("/search")
	public String findItemListByIndex(@RequestParam(value="q")String queryString,
			@RequestParam(defaultValue="1")Integer page,
			@RequestParam(defaultValue="60")Integer rows,Model model) throws Exception{
		//由于这个queryString是get方法传过来的，需要处理乱码。post框架处理过了
		queryString = new String(queryString.getBytes("ISO8859-1"), "UTF-8"); 
		//调用服务查询
		ResultModel resultModel = itemService.findItemListByIndex(queryString,page,rows);
	
		model.addAttribute("query", queryString);
		model.addAttribute("totalPages", resultModel.getTotalPages());
		model.addAttribute("itemList", resultModel.getItemList());
		model.addAttribute("page", resultModel.getCurPage());
		return "search";	
	} 
}
