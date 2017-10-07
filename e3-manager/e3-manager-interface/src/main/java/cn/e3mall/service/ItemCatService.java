package cn.e3mall.service;

import java.util.List;

import cn.e3mall.pojo.TreeNode;

public interface ItemCatService {
	public List<TreeNode> findItemCatList(long parentId);
}
