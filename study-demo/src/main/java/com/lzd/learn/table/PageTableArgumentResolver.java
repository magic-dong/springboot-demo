package com.lzd.learn.table;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.google.common.collect.Maps;

/**
 * 分页、查询参数解析
 * @author lzd
 * @date 2019年7月3日
 * @version
 */
public class PageTableArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> cla = parameter.getParameterType();
		return cla.isAssignableFrom(PageTableRequest.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

		PageTableRequest tableRequest = new PageTableRequest();
		Map<String, String[]> param = request.getParameterMap();
		Integer page=null;//页数
		Integer limit=null;//显示行数
		if (param.containsKey("page")) {
			page=Integer.parseInt(request.getParameter("page"));
		}
		if (param.containsKey("limit")) {
			limit=Integer.parseInt(request.getParameter("limit"));
		}
		
		if(page!=null && limit!=null){
			tableRequest.setOffset((page-1)*limit+1);
			tableRequest.setLimit(page*limit);
		}
		
		Map<String, Object> map = Maps.newHashMap();
		tableRequest.setParams(map);

		param.forEach((k, v) -> {
			if (v.length == 1) {
				map.put(k, v[0]);
			} else {
				map.put(k, Arrays.asList(v));
			}
		});
		
		setOrderBy(tableRequest, map);
		removeParam(tableRequest);

		return tableRequest;
	}

	/**
	 * 去除datatables分页带的一些复杂参数
	 * 
	 * @param tableRequest
	 */
	private void removeParam(PageTableRequest tableRequest) {
		Map<String, Object> map = tableRequest.getParams();

		if (!CollectionUtils.isEmpty(map)) {
			Map<String, Object> param = new HashMap<>();
			map.forEach((k, v) -> {
				if (k.indexOf("[") < 0 && k.indexOf("]") < 0 && !"_".equals(k)) {
					param.put(k, v);
				}
			});

			tableRequest.setParams(param);
		}
	}

	/**
	 * 从datatables分页请求数据中解析排序
	 * 
	 * @param tableRequest
	 * @param map
	 */
	private void setOrderBy(PageTableRequest tableRequest, Map<String, Object> map) {
		StringBuffer orderBy = new StringBuffer();
		String field = (String) map.get("field");
		String order = (String) map.get("order");
		if (!StringUtils.isEmpty(field)) {
			orderBy.append(field).append(" ").append(order);
			tableRequest.getParams().put("orderBy"," order by " +orderBy);
		}
	}
	

}
