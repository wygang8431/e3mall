package cn.e3mall.search.pojo;

import java.io.Serializable;
import java.util.List;

public class ResultModel implements Serializable{
	
	//当前页
	private Integer curPage;
	//总页码
	private Long totalPages;
	//总记录数
	private Long recordCount;
	//总记录
	private List<SearchItem> itemList;
	public Integer getCurPage() {
		return curPage;
	}
	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}
	public Long getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Long totalPages) {
		this.totalPages = totalPages;
	}
	public Long getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(Long recordCount) {
		this.recordCount = recordCount;
	}
	public List<SearchItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}
	
	
	
	

}
