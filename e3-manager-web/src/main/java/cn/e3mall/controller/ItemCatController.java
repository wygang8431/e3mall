package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.pojo.TreeNode;
import cn.e3mall.service.ItemCatService;

@Controller
public class ItemCatController {
	@Autowired
	private ItemCatService catService;
	@RequestMapping(value="/item/cat/list",produces="text/plain;charset=UTF-8")
	@ResponseBody
	public List<TreeNode> findItemCatList(@RequestParam(value="id",defaultValue="0") long parentId){
		
		return catService.findItemCatList(parentId);
	}

}
