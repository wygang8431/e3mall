package cn.e3mall.search.mapper;

import java.util.List;

import cn.e3mall.search.pojo.SearchItem;

public interface SearchItemMapper {
   List<SearchItem> getSearchItems4SolrIndex();
   SearchItem getSearchItems4SolrIndexById(Long itemId);
}