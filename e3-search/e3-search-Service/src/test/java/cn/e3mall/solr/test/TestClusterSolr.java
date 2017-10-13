package cn.e3mall.solr.test;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

public class TestClusterSolr {
	@Test
	public void searchIndex() throws Exception{
		String zkHost = "192.168.25.128:2182,192.168.25.128:2183,192.168.25.128:2184";
		//创建solr集群服务对象
		CloudSolrServer cloudSolrServer = new CloudSolrServer(zkHost);
		cloudSolrServer.setDefaultCollection("item");
		//创建查询条件
		SolrQuery query = new SolrQuery();
		query.setQuery("item_sell_point:北京");
		//执行查询
		QueryResponse response = cloudSolrServer.query(query);
		//解析结果
		SolrDocumentList results = response.getResults();
		System.out.println(results.getNumFound());
	}
}
