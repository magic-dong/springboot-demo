package com.lzd.learn.table;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果返回
 * @author lzd
 * @date 2019年7月3日
 * @version
 */
public class PageTableResponse implements Serializable {

	private static final long serialVersionUID = 620421858510718076L;

	private Integer code;
	private String msg;
	private Integer count;
	private List<?> data;
	
	public PageTableResponse(Integer count,List<?> data){
		this.code=0;//成功
		this.msg="成功";
		this.count=count;
		this.data=data;
	}
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public List<?> getData() {
		return data;
	}
	public void setData(List<?> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "PageTableResponse [code=" + code + ", msg=" + msg + ", count=" + count + ", data=" + data + "]";
	}
}