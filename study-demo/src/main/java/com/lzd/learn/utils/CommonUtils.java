package com.lzd.learn.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * 通用工具类
 * @author lzd
 * @date 2019年4月3日
 * @version
 */
public class CommonUtils {

    /**
     * 根据日期返回季度
     * @return
     */
    public static String calcQuarter(String endDate){
        if(endDate!=null&&endDate.length()>=10) {
            String quarter = new String();
            Integer month = Integer.parseInt(endDate.substring(5,7));
            switch (month){
                case 3:
                    quarter = "一季度";
                    break;
                case 6:
                    quarter = "半年";
                    break;
                case 9:
                    quarter = "三季度";
                    break;
                case 12:
                    quarter = "全年";
                    break;
                default:
                    quarter = "";
            }
            return quarter;
        }else{
            return "";
        }
    }

    
    /**
     * 根据股票代码判断市场
     * @author lzd
     * @date 2019年4月3日:下午3:33:49
     * @param secucode
     * @return
     * @description
     */
    public static String marketJudge(String secucode){
        if(secucode == null || secucode.length() <= 0){
            return null;
        }
        if(secucode.startsWith("6") || secucode.startsWith("9")){
            return "sh"+secucode;
        }else if(secucode.startsWith("0") || secucode.startsWith("2") || secucode.startsWith("3")){
            return "sz"+secucode;
        }else{
            return secucode;
        }
    }
    
    /**
     * 判断Map集合是否包含某key
     * @author lzd
     * @date 2019年4月15日:下午4:07:11
     * @param map
     * @param key
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static boolean isContainsKey(Map map,String key){
    	if(map==null){
    		return false;
    	}
    	return map.containsKey(key);
    }
    
    
    /**
     * 对集合进行排序
     * @author lzd
     * @date 2019年7月29日:下午2:54:30
     * @param list
     * @param orderBy
     * @return
     * @description
     */
    @SuppressWarnings({ "unchecked" })
	public static List<?> compareToByOrder(List<?> list,String orderBy){
    	if(list==null || list.isEmpty()){
    		return list;
    	}
    	if(orderBy==null || "".equals(orderBy.trim())){
    		return list;
    	}
    	//获取排序字段组合(order by xxx desc,xxx asc)
    	String[] columnArray = orderBy.replaceFirst(" order by ", "").replaceAll("，", ",").split(",");
    	//排序
		ComparatorChain chain = new ComparatorChain();
    	for (int i = 0; i < columnArray.length; i++) {
    		//拆除column和排序规则
    		String[] orderArray=columnArray[i].split(" ");
    		if(orderArray!=null && orderArray.length>0){
    			System.out.println(orderArray.length);
        		//列名
        		String column=orderArray[0];
        		//升/降序
        		String order=orderArray.length>=2 ? orderArray[1]:null;
        		boolean flag=order==null || "ASC".equals(order.trim().toUpperCase())? false:true;
                chain.addComparator(new BeanComparator(column), flag);//true：降序,false：升序
        	}
		}
    	Collections.sort(list, chain);//List<T> 或者是其他的集合
        return list;
    }
    
    /**
     * 通过字段组合数据筛选出目标数据集合
     * @author lzd
     * @date 2019年7月29日:下午5:14:21
     * @param fieldIds 目标字段数据组合，以逗号隔开 eg："1,2,3,34"
     * @param list 数据集合
     * @param fieldType 目标字段
     * @return
     * @throws Exception
     * @description
     */
    public static List<? extends Object> getCheckedList(String fieldIds,List<?> list,String fieldType) throws Exception{
    	if(fieldIds==null || "".equals(fieldIds.trim())){
    		return list;
    	}
    	if(list==null || list.isEmpty()){
    		return list;
    	}
    	//获的数据Ids数组
    	String[] idsArray=fieldIds.replaceAll("，", ",").split(",");
    	List<Object> newList=new ArrayList<>(list.size());
    	for (int i = 0; i < idsArray.length; i++) {
			String typeId=idsArray[i];
			for (int j = 0; j < list.size(); j++) {
				Object target = list.get(j);
				Class<? extends Object> classzz = target.getClass();
				Field[] fields = classzz.getDeclaredFields();
				String targetFieldValue=null;
				for (int k = 0; k < fields.length; k++) {
					//得到属性
	                Field field = fields[k];
	                //打开私有访问
	                field.setAccessible(true);
	                //获取属性
	                String fieldName = field.getName();
	                if(fieldName.equals(fieldType)){
	                	//获取属性值
		                Object fieldValue = field.get(target);
		                targetFieldValue=String.valueOf(fieldValue);
		                break;
	                }
				}
				if(targetFieldValue !=null && targetFieldValue.equals(typeId.replaceAll("'", ""))){
					newList.add(target);
				}
			}
		}
    	return newList;
    }
    
    /**
	 * 裴波拉契数列
	 * @author lzd
	 * @date 2019年10月10日:下午2:28:35
	 * @param n
	 * @return
	 * @description
	 */
	public static int pepperArray(int n){
		return (int)(1/Math.sqrt(5)*( Math.pow(((1+Math.sqrt(5))/2), n) - Math.pow(((1-Math.sqrt(5))/2), n) ));
	}
	
	/**
	 * 计算算术平方根
	 * @author lzd
	 * @date 2019年10月10日:上午11:50:51
	 * @param x
	 * @return
	 * @description
	 */
	public static double sqrt(double x){
		double val=x;
		double last;
		do {
			last=val;
			val=(val+x/val)*0.5D;
		} while (Math.abs(val-last)>Math.pow(2, -52));
		return val;
	}
	
	
}
