package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.utils.AdItem;
import cn.e3mall.utils.JsonUtils;

@Controller
public class PageController {
	@Autowired
	private ContentService contentService;
	@Value("${BIG_AD_CID}")
	private Long BigAdCid;
	
	@RequestMapping("index")
	public String showIndex(Model model){
		//在去首页之前加载广告数据
		//加载大广告数据
		//查询数据
		List<AdItem> adItems = contentService.getContentListByCategoryId(BigAdCid);
		//将数据转成json数据
		String json = JsonUtils.objectToJson(adItems);
		//将数据放入model域中--key值ad1是页面写好的
		model.addAttribute("ad1", json);
		//加载小广告数据
		//加载楼层广告数据
		
		return "index";
	}
}
