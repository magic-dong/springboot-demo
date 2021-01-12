package com.test;

import org.junit.Test;

import com.test.callback.Store;
import com.test.callback.SyncBuyer;

public class Demo2 {

	@Test
	public void test(){
		Store wallMart=new Store("场中路沃尔玛");
		SyncBuyer buyer=new SyncBuyer("MagicDong", wallMart, "老婆饼");
		System.out.println(buyer.orderGoodsInfo());
	}
}
