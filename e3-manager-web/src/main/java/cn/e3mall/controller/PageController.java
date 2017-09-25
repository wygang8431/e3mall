package cn.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	@RequestMapping("/")
	public String toIndex(){
		//跳转到首页
		return "index";
	}
	@RequestMapping("{page}")
	public String toPage(@PathVariable String page){
		//从URL中获取参数，然后再访问对应的页面 restful风格的巧妙应用！
		return page;
	}
}
