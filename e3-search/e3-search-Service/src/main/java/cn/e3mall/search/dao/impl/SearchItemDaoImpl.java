package cn.e3mall.search.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.search.dao.SearchItemDao;
import cn.e3mall.search.pojo.ResultModel;
import cn.e3mall.search.pojo.SearchItem;

@Repository
public class SearchItemDaoImpl implements SearchItemDao {
	@Autowired
	private SolrServer solrServer;
	@Override
	public ResultModel findItemListByIndex(SolrQuery solrQuery) {
		ResultModel resultModel = new ResultModel();
		try {
			
			QueryResponse response = solrServer.query(solrQuery);
			SolrDocumentList results = response.getResults();
			//设置分页总参数
			resultModel.setRecordCount(results.getNumFound());
			List<SearchItem> itemList = new ArrayList<SearchItem>();
			
			for (SolrDocument doc : results) {
				SearchItem item = new SearchItem();
				//取高亮后的结果
				Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
				List<String> list = highlighting.get(doc.get("id")).get("item_title");
				String title= "";
				if (list != null && list.size() > 0) {
					//取高亮后的结果
					title = list.get(0);
				} else {
					title = (String) doc.get("item_title");
				}
				
				
				//设置searchItem的参数
				//在索引库中id是String型，需要解析一下
				//而price在索引库时long型，不需要解析，可以直接强制转换
				item.setId(Long.parseLong((String)doc.getFieldValue("id")));
				
				item.setTitle(title);//经过高亮处理后才设置
				
				item.setSell_point((String)doc.getFieldValue("item_sell_point"));
				item.setPrice((Long)doc.getFieldValue("item_price"));
				item.setImage((String)doc.getFieldValue("item_image"));
				item.setCategory_name((String)doc.getFieldValue("item_category_name"));
				item.setItem_desc((String)doc.getFieldValue("item_desc"));
				itemList.add(item);
			}
			resultModel.setItemList(itemList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultModel;
	}
	
}
