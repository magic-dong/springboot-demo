package com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.lzd.learn.entity.test.Student;
import com.lzd.learn.utils.DateUtils;
import com.lzd.learn.utils.JsonUitls;

import net.sf.json.JSONObject;
public class Test {
	
	public static void main(String[] args){
		// TODO Auto-generated method stub
		//测试
//		test1();
		
		//test2();
//		BigDecimal netProfit=new BigDecimal("1.32");
//		BigDecimal operatingRevenue=new BigDecimal("12.90");
//		String netProfitRate=NumberUtils.divideAndPercent(netProfit, operatingRevenue);
//		System.out.println(netProfitRate);
		
//		String json="{\"responseInfo\":{\"pageinfo\":{\"pageNum\":1,\"perNum\":15,\"totalCount\":1,\"totalPage\":1},\"result\":0,\"userInfoList\":[{\"ubi\":{\"state\":0,\"usertype\":0,\"mobile\":\"\",\"ifmobileBind\":0,\"email\":\"\",\"ifemailBnd\":0,\"regtime\":\"2019-04-17 13:37:39\",\"realnameAuthen\":0},\"userid\":2815519,\"username\":\"xq20\"}]}}";
//		Gson gson=new Gson();
//		Map<String,UserInfoList> map=gson.fromJson(json, new TypeToken<Map<String,UserInfoList>>() {}.getType());
//		UserInfoList userInfoList = map.get("responseInfo");
//		System.out.println(userInfoList);
		
//		//模拟代码先登录接口，再继续操作其他业务接口
//		test3();
		
		try {
			System.out.println(DateUtils.parseDate("20191107"));
			System.out.println(DateUtils.parseDateToStr("yyyy-MM-dd",DateUtils.parseDate("20191107")));
			String publicTime=DateUtils.parseDateToStr("yyyyMMdd", DateUtils.parseDate("2019-11-06"));
			System.out.println(publicTime);
			System.out.println(DateUtils.getTargetDay("20191210", -1, "yyyyMMdd"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private static void test1(){
		 List<Student> slist = new ArrayList<>();
		 Student stu = new Student(1, "Magic Dong");
		 slist.add(stu);
		 stu = new Student(2, "Magic Dong2");
		 slist.add(stu);
		 stu = new Student(3, "Magic Dong3");
		 slist.add(stu);
		 System.out.println(JsonUitls.ListToJson(slist));
		
		 String json = "[{\"sId\":1,\"sName\":\"Magic Dong\"},{\"sId\":2,\"sName\":\"Magic Dong2\"},{\"sId\":3,\"sName\":\"Magic Dong3\"}]";
		 List<Student> stulist = (List<Student>) JsonUitls.JsonToList(json);
		 System.out.println("长度：" + stulist.size());
		
		 System.out.println(JsonUitls.ObjectToJson(stu));
		 json="{\"sId\":1,\"sName\":\"Magic Dong\"}";
		 System.out.println(JsonUitls.JsonToObject(json, Student.class));
	}
	
	@SuppressWarnings({ "deprecation", "unused" })
	private static void test2(){
		try {
			HttpClient client = new HttpClient();
			String url ="http://127.0.0.1:8080/draftSection/createGraft";
			client.getParams().setSoTimeout(30000);
			PostMethod post = new PostMethod(url);
			Map<String, Object> parmmap = new HashMap<String, Object>();
			parmmap.put("secucode", "SZ000008");
			post.setRequestHeader("Content-Type", "application/json");
			post.setRequestContentLength(89);
			post.setRequestBody(JSONObject.fromObject(parmmap).toString());
			int code = client.executeMethod(post);
	        if (code == 200) {
	        	  StringBuffer buf = new StringBuffer();
	              InputStream stream = post.getResponseBodyAsStream();
	              BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
	              String line;
	              while ((line = br.readLine()) != null) {
	                    buf.append(line);
	              }
	              String resultinfo = buf.toString();
	              resultinfo = resultinfo.substring(resultinfo.indexOf("{"), resultinfo.length());
	              System.out.println(resultinfo);
	        }else{
	        	System.out.println(post.getResponseBodyAsString());
	        }
		 } catch (Exception e) {
			e.printStackTrace();
		 }
	}

	@SuppressWarnings({"unused" })
	private static void test3(){
		try {
			String url ="http://127.0.0.1:8081/sys/login";
			Map<String, String> parmmap = new HashMap<String, String>();
			parmmap.put("username", "admin");
			parmmap.put("password", "ztadmin");
			String cookie = doPost(url, parmmap, "UTF-8");
			System.out.println(cookie);
	        
			String url2 ="http://127.0.0.1:8081/shops/20170731131934567428";
			CloseableHttpClient httpClient =HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url2);
			httpget.setHeader("Cookie", cookie);
		    CloseableHttpResponse response = httpClient.execute(httpget);
		    try {
                // 得到返回对象
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // 获取返回结果
                    String result = EntityUtils.toString(entity);
                    System.out.println(result);
                }
            } finally {
                // 关闭到客户端的连接
                 response.close();
            }
		 } catch (Exception e) {
			e.printStackTrace();
		 }
	}
	
	/**
	 * 获取登录后返回的Cookie值
	 * @author lzd
	 * @date 2019年7月18日:下午3:03:06
	 * @param url
	 * @param map
	 * @param charset
	 * @return
	 * @description
	 */
	public static String doPost(String url,Map<String, String> map, String charset) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        String cookie = null;
        CloseableHttpResponse response=null;
        try {
            CookieStore cookieStore = new BasicCookieStore();
            httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            httpPost = new HttpPost(url);
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> elem = (Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                httpPost.setEntity(entity);
            }
            response = httpClient.execute(httpPost);
            //获取响应文本中的"Set-Cookie"值
            cookie=response.getFirstHeader("Set-Cookie").getValue();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        	if(response !=null){
        		try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
		}
        return cookie;
    }
	
	public static int getSecondMax(int[] arrays){
		try {
			if (arrays==null || arrays.length <= 1) {
	            throw new Exception("数组非法");
	        }
	        int max1 = arrays[0], max2 = 0; // 用来记录最大值和第二大的值
	        for (int i = 1; i < arrays.length; i++) { // 遍历数组
	        	if (arrays[i] == max1) { // 与最大值相等，则不需要执行下面的，循环继续
                    continue;
                }
                if (arrays[i] > max1) { // 当前数据大于最大值 
                    max2 = max1; // 将max1赋值给max2
                    max1 = arrays[i]; // 为max1重新赋值
                } else { // 当前数据小于最大值
                	max2 = Math.max(max2, arrays[i]); // 为max2重新赋值，比较获取较大的值
                }
	        }
	        return max2;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}
}
