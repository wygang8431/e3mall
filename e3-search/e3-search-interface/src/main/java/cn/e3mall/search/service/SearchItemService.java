package cn.e3mall.search.service;

import cn.e3mall.search.pojo.ResultModel;
import cn.e3mall.utils.E3Result;

public interface SearchItemService {
	
	public E3Result getSearchItems4SolrIndex();

	public ResultModel findItemListByIndex(String queryString, Integer page, Integer rows);
}
