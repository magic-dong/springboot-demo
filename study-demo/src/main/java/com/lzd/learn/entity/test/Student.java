package com.lzd.learn.entity.test;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Student implements Serializable{
	private Integer sId;
	private String sName;
	
	public Student() {
		
	}
	
	public Student(Integer sId, String sName) {
		super();
		this.sId = sId;
		this.sName = sName;
	}
	
	public Integer getsId() {
		return sId;
	}

	public void setsId(Integer sId) {
		this.sId = sId;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}
 
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		//非空性：对于任意非空引用x，x.equals(null)应该返回false
		if(obj==null) return false;
		
		//自反性
		if(this==obj) return true;
				
		//比较是否为同一类型
		if(!(obj instanceof Student)) return false;
		
		Student stu=(Student) obj;
		return sId.equals(stu.getsId()) && sName.equals(stu.getsName());
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		int hashCode=1;
		hashCode=hashCode*31+(sId==null ? 0:sId.hashCode());
		hashCode=hashCode*31+(sName==null ? 0:sName.hashCode());
		return hashCode;
	}
	
	@Override
	public String toString() {
		return "Student [sId=" + sId + ", sName=" + sName + "]";
	}
	
}
