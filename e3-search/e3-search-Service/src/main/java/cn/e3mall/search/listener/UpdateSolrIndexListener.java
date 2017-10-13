package cn.e3mall.search.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.search.mapper.SearchItemMapper;
import cn.e3mall.search.pojo.SearchItem;

public class UpdateSolrIndexListener implements MessageListener {
	@Autowired
	private SearchItemMapper searchItemMapper;
	@Autowired
	private SolrServer solrServer;

	@Override
	public void onMessage(Message message) {
		if(message instanceof TextMessage){
			TextMessage textMessage = (TextMessage) message;
			try {
				//获取消息中的商品id
				Long itemId = Long.parseLong(textMessage.getText());
				//通过id查询商品信息表
				SearchItem Item = searchItemMapper.getSearchItems4SolrIndexById(itemId);
				//将新查询的数据更新到索引库 
				SolrInputDocument doc = new SolrInputDocument();
				doc.addField("id", Item.getId());
				doc.addField("item_title", Item.getTitle());
				doc.addField("item_sell_point", Item.getSell_point());
				doc.addField("item_price", Item.getPrice());
				doc.addField("item_image", Item.getImage());
				doc.addField("item_category_name", Item.getCategory_name());
				doc.addField("item_desc", Item.getItem_desc());
				solrServer.add(doc);
				solrServer.commit();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
