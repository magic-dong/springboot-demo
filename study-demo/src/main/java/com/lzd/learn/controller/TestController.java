package com.lzd.learn.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lzd.learn.annotation.GetLimitMapping;
import com.lzd.learn.annotation.PreventDuplicateSubmission;
import com.lzd.learn.json.MessageResult;
import com.lzd.learn.service.LockService;
import com.lzd.learn.service.RetryService;
import com.lzd.learn.utils.CommonUtils;
import com.lzd.learn.utils.DateUtils;
import com.lzd.learn.utils.RedisUtils;

@RestController
@RequestMapping("/test")
public class TestController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(TestController.class);

	@GetMapping("/redis")
	public MessageResult testRedis(@RequestParam Map<String, Object> params) {
		try {
			// 判断是否输入正确
			if (params == null || params.get("secucode") == null || "".equals(params.get("secucode"))) {
				return MessageResult.error("Param secucode is required...");
			}
			if (params == null || params.get("endDate") == null || "".equals(params.get("endDate"))) {
				return MessageResult.error("Param endDate is required...");
			}
			// 根据股代码判断行情市场
			String secucode = CommonUtils.marketJudge(params.get("secucode").toString());
			// 规范时间字符串
			String enddate = DateUtils.formatDateStr("yyyy-MM-dd", params.get("endDate").toString());

			// 存入缓存中
			RedisUtils.set("secucode", secucode);
			RedisUtils.set("enddate", enddate, 30);

			// 获取缓存中的数据
			String rs = String.valueOf(RedisUtils.get("secucode"));
			String re = String.valueOf(RedisUtils.get("enddate"));
			return MessageResult.success(rs + "," + re);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return MessageResult.error(e.getMessage());
		}

	}

	/*====================================测试限流请求接口demo==============================*/
	@GetLimitMapping(value = "/request", second = 10, counter = 10)
	public MessageResult requestLimit(@RequestParam Map<String, Object> params) {
		try {
			return success();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return error(e.getMessage());
		}

	}
	
	/*====================================测试重试demo==============================*/
	@Autowired
	private RetryService retryService;

	@GetMapping("/retry")
	public MessageResult testRetry(@RequestParam Map<String, Object> params) {
		try {
			// String res = testService.testSpringRetry(params);
			String res = retryService.testGuavaRetry(params);
			return success(res);
		} catch (Exception e) {
			// TODO: handle exception
			return error(e.getMessage());
		}
	}

	/*====================================测试分布式锁demo==============================*/
	@Autowired
	private LockService lockService;

	@GetMapping("/lock")
	public MessageResult lock(@RequestParam Map<String, Object> params) {
		try {
			String method1 = lockService.method1();
			return success(method1);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
			return error();
		}
	}

	/*====================================测试防重复提交demo==============================*/
	private Map<String,Object> map = new HashMap<>();

	@GetMapping("/createOrder/{userId}/{num}")
	@PreventDuplicateSubmission
	public MessageResult createOrder(@PathVariable Integer userId, @PathVariable Integer num) throws InterruptedException {
		try {
			// 这里模拟用户限购 假设每个用户只能买一个
			if (num > 1) {
				return success("每个用户只能购买一份");
			}
			// 通过map来模拟读库是否购买过
			if (map.get(userId+"") != null) {
				return success("已经购买过了，无法再次购买");
			}
			// 模拟创建订单逻辑 方便观察效果
			map.put(userId+"", num);
			return success("创建订单成功，订单号为:" + new Random().nextInt(10000));

		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
			return error(e.getMessage());
		}
	}

}