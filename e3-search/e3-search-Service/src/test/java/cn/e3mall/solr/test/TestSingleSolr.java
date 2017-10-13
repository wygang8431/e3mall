package cn.e3mall.solr.test;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

public class TestSingleSolr {
	@Test
	public void searchIndex() throws Exception{
		//创建solr服务对象
		HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr/item");
		//创建查询条件
		SolrQuery query = new SolrQuery();
		query.setQuery("item_sell_point:北京");
		//执行查询
		QueryResponse response = solrServer.query(query);
		//解析结果
		SolrDocumentList results = response.getResults();
		System.out.println(results.getNumFound());
	}
}
