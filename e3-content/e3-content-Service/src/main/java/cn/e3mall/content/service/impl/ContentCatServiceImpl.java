package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.content.service.ContentCatService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;
import cn.e3mall.pojo.TreeNode;
import cn.e3mall.utils.E3Result;
@Service
public class ContentCatServiceImpl implements ContentCatService {

	@Autowired
	private TbContentCategoryMapper mapper;
	//根据父id查询所有子节点
	public List<TreeNode> findItemCatList(long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = mapper.selectByExample(example);
		
		//将上诉查询的数据封装成List<TreeNode>
		List<TreeNode> result = new ArrayList();
		TreeNode node = null;
		for (TbContentCategory cat : list) {
			node = new TreeNode();
			node.setId(cat.getId());
			node.setText(cat.getName());
			node.setState(cat.getIsParent()?"closed":"open");
			result.add(node);
		}
		
		return result;
	}
	@Override
	public E3Result saveContentCat(long parentId, String name) {
		TbContentCategory contentCat = new TbContentCategory();
		contentCat.setParentId(parentId);
		contentCat.setName(name);
		contentCat.setStatus(1);
		contentCat.setSortOrder(1);
		//当添加时，当前id一定是儿子
		contentCat.setIsParent(false);
		contentCat.setCreated(new Date());
		contentCat.setUpdated(new Date());
		//将数据保存到数据库
		//这个insert方法一定要有主键返回功能，因为页面需要返回的数据格式中需要用到当前插入节点的id
		mapper.insert(contentCat);
		
		//这个新建的节点可能插入在一个父亲节点或儿子节点上，
		//如果是儿子节点，再插入这个节点之后，他就变成了父亲节点，此时需要更改它的IsParent状态
		//业务逻辑：通过上面的parentId查询id等于parentId的节点，再判断这个节点的isparent是否为true，如果不是给它给成ture
		TbContentCategory parentCat = mapper.selectByPrimaryKey(parentId);
		//如果不是父亲节点，那么更改它为父亲节点
		if(!parentCat.getIsParent()){
			parentCat.setIsParent(true);
			//更新到数据库
			mapper.updateByPrimaryKey(parentCat);
		}
		return E3Result.ok(contentCat);
	}

}
