package com.test.callback;

import java.util.Random;

public class Store {

	private String name;
	
	public Store(String name){
		this.name=name;
	}
	
	
	public String returnOrderGoodsInfo(CallBack call){
		String[] s = {"订购中...", "订购失败", "即将发货!", "运输途中...", "已在投递"};
        Random random = new Random();
        int temp = random.nextInt(5);
        String s1 = s[temp];
        return call.getOrderResult(s1);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
