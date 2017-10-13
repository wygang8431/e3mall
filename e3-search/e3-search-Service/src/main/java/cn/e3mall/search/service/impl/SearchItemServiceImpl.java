package cn.e3mall.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.search.dao.SearchItemDao;
import cn.e3mall.search.mapper.SearchItemMapper;
import cn.e3mall.search.pojo.ResultModel;
import cn.e3mall.search.pojo.SearchItem;
import cn.e3mall.search.service.SearchItemService;
import cn.e3mall.utils.E3Result;
@Service
public class SearchItemServiceImpl implements SearchItemService {
	@Autowired
	private SearchItemMapper mapper;
	@Autowired
	private SolrServer solrServer;
	@Autowired
	private SearchItemDao dao;
	@Override
	public E3Result getSearchItems4SolrIndex() {
		try {
			List<SearchItem> searchItems = mapper.getSearchItems4SolrIndex();
			for (SearchItem Item : searchItems) {
				SolrInputDocument doc = new SolrInputDocument();
				doc.addField("id", Item.getId());
				doc.addField("item_title", Item.getTitle());
				doc.addField("item_sell_point", Item.getSell_point());
				doc.addField("item_price", Item.getPrice());
				doc.addField("item_image", Item.getImage());
				doc.addField("item_category_name", Item.getCategory_name());
				doc.addField("item_desc", Item.getItem_desc());
				solrServer.add(doc);
				
			}
			solrServer.commit();
			return E3Result.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public ResultModel findItemListByIndex(String queryString, Integer page, Integer rows) {
		SolrQuery query = new SolrQuery();
		//判断条件是否为空
		if (queryString != null && !"".equals(queryString)) {
			query.setQuery(queryString);
		} else {
			query.setQuery("*:*");
		}

		//设置分页条件
		int startNo = (page - 1) * rows;
		query.setStart(startNo);
		query.setRows(rows);
		//开启高亮
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<font color='red'>");
		query.setHighlightSimplePost("</font>");
		//设置默认搜索域
		query.set("df", "item_keywords");
		
		ResultModel resultModel = dao.findItemListByIndex(query);
		//设置当前页面
		resultModel.setCurPage(page);
		//设置总页码
		Long totalPages = resultModel.getRecordCount()/rows;
		if(resultModel.getRecordCount()%rows>0){
			totalPages++;
		}
		resultModel.setTotalPages(totalPages);
		
		return resultModel;	
	}
}
