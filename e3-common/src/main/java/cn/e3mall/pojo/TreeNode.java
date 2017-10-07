package cn.e3mall.pojo;

import java.io.Serializable;
/*
 *tree的返回json数据格式
 [{    
    "id": 1,    
    "text": "Node 1",    
    "state": "closed"
},{    
    "id": 2,    
    "text": "Node 2",    
    "state": "closed"   
}] 
state：如果节点下有子节点“closed”，如果没有子节点“open”
 */

public class TreeNode implements Serializable {
	private Long id;
	private String text;
	private String state;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
