package cn.e3mall.service.impl;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.pojo.TreeNode;
import cn.e3mall.service.ItemCatService;
@Service
public class ItemCatServiceImpl implements ItemCatService {
	@Autowired
	private TbItemCatMapper catMapper;

	@Override
	public List<TreeNode> findItemCatList(long parentId) {
		//创建查询条件
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
	
		List<TbItemCat> list = catMapper.selectByExample(example);
		//将itemcat封装厂treeNote形式
		List<TreeNode> results = new ArrayList<TreeNode>();
		TreeNode treeNode = null;
		for (TbItemCat cat : list) {
			treeNode = new TreeNode();
			treeNode.setId(cat.getId());
			treeNode.setText(cat.getName());
			//三元运算符 :如果是父节点，那么关闭状态【当该节点被打开时，会异步加载它下面的子节点】否则是开启状态
			//cat.getIsParent()数据库中的类型是短整型，逆向工程自动转成了boolean，true/false
			treeNode.setState(cat.getIsParent()?"closed":"open");
			results.add(treeNode);
		}
		return results;
	}

}
