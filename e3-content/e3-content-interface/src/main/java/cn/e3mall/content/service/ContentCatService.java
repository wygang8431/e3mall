package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.pojo.TreeNode;
import cn.e3mall.utils.E3Result;

public interface ContentCatService {

	List<TreeNode> findItemCatList(long parentId);

	E3Result saveContentCat(long parentId, String name);

}
