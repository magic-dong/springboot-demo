package com.lzd.learn.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 自定义导出Excel数据注解
 * @author lzd
 * @date 2019年7月25日
 * @version
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Excel
{
	/**
	 * 导出到Excel中的名字
	 * @author lzd
	 * @date 2018年8月24日:下午5:09:20
	 * @return
	 * @description
	 */
    String name();
    
    /**
     * 导出到Excel中的列顺序
     * @author lzd
     * @date 2018年8月25日:下午4:53:32
     * @return
     * @description
     */
    int sort() default 0;
    
    /**
     * 匹配真正需要转化的展示值 xx:xx,xx:xxx(逗号隔开)
     * @author lzd
     * @date 2018年8月25日:下午4:53:32
     * @return
     * @description
     */
    String  notes() default "";

    /**
     * 指定时间类型字段format格式
     * @author lzd
     * @date 2019年9月27日:下午4:53:18
     * @return
     * @description
     */
    String  dateFormat() default "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 提示信息
     * @author lzd
     * @date 2018年8月27日:下午5:09:34
     * @return
     * @description
     */
    String prompt() default "";

    /**
     * 设置只能选择不能输入的列内容
     * @author lzd
     * @date 2018年8月27日:下午5:09:44
     * @return
     * @description
     */
    String[] combo() default {};

    /**
     * 是否导出数据,应对需求:有时我们需要导出一份模板,
     * 这是标题需要但内容需要用户手工填写
     * @author lzd
     * @date 2018年8月27日:下午5:09:55
     * @return
     * @description
     */
    boolean isExport() default true;
}