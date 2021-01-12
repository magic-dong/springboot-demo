package com.lzd.learn.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.lzd.learn.utils.HttpClientUtils;
import com.lzd.learn.utils.StringUtils;

public class BaseDao {
	
	/**
	 * 获取json数据中总条数
	 * @author lzd
	 * @date 2019年8月16日:下午3:29:11
	 * @param httpUrl
	 * @return
	 * @description
	 */
	public int count(String httpUrl){
		int count=0;
		if(StringUtils.isNotNull(httpUrl)){
			JSONObject parseObject = JSONObject.parseObject(HttpClientUtils.doGet(httpUrl, "UTF-8"));
			if (parseObject != null && parseObject.getBoolean("success")) {
				// 获取目标数据总条数
				count= Integer.valueOf(parseObject.get("totalCount").toString());
			}
		}
		return count;
	}
	
	/**
	 * Json数据转Map<String,Object>集合
	 * @author lzd
	 * @date 2019年8月16日:下午3:16:12
	 * @param httpUrl
	 * @return
	 * @description
	 */
	public Map<String,Object> jsonConvertMap(String httpUrl){
		Map<String,Object> resultMap=null;
		if(StringUtils.isNotNull(httpUrl)){
			JSONObject parseObject = JSONObject.parseObject(HttpClientUtils.doGet(httpUrl,"UTF-8"));
			if(parseObject !=null && parseObject.getBoolean("success")){
				//获取目标数据
				String data = String.valueOf(parseObject.get("data"));
				//判断是否是有效数据
				if(StringUtils.isNotNull(data) && data.startsWith("{")){
					resultMap=JSON.parseObject( data,new TypeReference<Map<String,Object>>(){});
				}
			}
		}
		return resultMap;
	}
	
	/**
	 * Json数据转List<Map<String,Object>>集合
	 * @author lzd
	 * @date 2019年8月16日:下午3:20:35
	 * @param httpUrl
	 * @return
	 * @description
	 */
	public List<Map<String,Object>> jsonConvertList(String httpUrl){
		List<Map<String,Object>> dataMapList=null;
		if(StringUtils.isNotNull(httpUrl)){
			JSONObject parseObject = JSONObject.parseObject(HttpClientUtils.doGet(httpUrl,"UTF-8"));
			if(parseObject !=null && parseObject.getBoolean("success")){
				//获取目标数据
				String data = String.valueOf(parseObject.get("data"));
				//判断是否有有效数据
				if(StringUtils.isNotNull(data) && data.startsWith("[")){
					JSONArray parseArray = JSONArray.parseArray(data);
					if(parseArray!=null && parseArray.size()>0){
						dataMapList=new ArrayList<>(parseArray.size());
						for (int i = 0; i < parseArray.size(); i++) {
							Map<String,Object> resultMap=JSON.parseObject( parseArray.get(i).toString(),new TypeReference<Map<String,Object>>(){});
							dataMapList.add(resultMap);
						}
					}
				}
			}
		}
		return dataMapList;
	}
}
