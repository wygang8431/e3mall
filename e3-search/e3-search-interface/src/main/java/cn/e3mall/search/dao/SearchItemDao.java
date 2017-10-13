package cn.e3mall.search.dao;

import org.apache.solr.client.solrj.SolrQuery;

import cn.e3mall.search.pojo.ResultModel;

public interface SearchItemDao {
	public ResultModel findItemListByIndex(SolrQuery solrQuery);
}
