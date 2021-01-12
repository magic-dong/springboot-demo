package com.test.callback;

public class SyncBuyer implements CallBack{
	
	private String buyerName;
	private Store store;
	private String goodsName;
	
	public SyncBuyer(String buyerName,Store store,String goodsName){
		this.buyerName=buyerName;
		this.store=store;
		this.goodsName=goodsName;
	}

	public String orderGoodsInfo(){
		String goodsInfo = store.returnOrderGoodsInfo(this);
		return goodsInfo;
	}
	
	@Override
	public String getOrderResult(String state) {
		// TODO Auto-generated method stub
		String message=String.format("顾客%s:在%s商店订购的%s商品，目前预定的状态是：%s!", buyerName,store.getName(),goodsName,state);
		return message;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	
}
