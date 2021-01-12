/**
 * Created by lzd on 2019/7/29.
 */
$(function () {
	 //初始化最近季度
	initQuarter();
	
	//初始化接口数据
	initInterfaces("1","quarterInterfaces");
	
    layui.use(['table','form','laypage','element','laydate','upload'], function(){

        var table = layui.table
            ,upload = layui.upload
            ,laydate = layui.laydate
            ,laypage = layui.laypage //分页
            ,form = layui.form;
        
        //日历
        laydate.render({
            elem: '#year' //指定元素
            ,type: 'year'
            //,range: '~'
            ,format: 'yyyy'
            ,max:0
        });
        
        //搜索
        var $ = layui.$, active = {
        	reload: function(){
        	   var enddate=$("#year").val()+"-"+$("#date").val();
        	   //console.log(table.where);
          	   //执行重载
          	   table.reload('myTable', {
          	        where:{
          	        	'endate':enddate
          	        },
          	        page: {
          	            curr: 1 //重新从第 1 页开始
          	        }
          	   });
          	}    
        };
        
        //搜索
        $('#searchBt').on('click', function(){ 
        	var showUrl=$("#quarterInterfaces option:selected").val();
        	var interfaceId=$("#quarterInterfaces option:selected").attr("interface-id");
        	var enddate=$("#year").val()+"-"+$("#date").val();
        	var secucode=$("#secucode").val();
        	if(showUrl!=null && showUrl!=""){
        		//where参数
            	var where={};
        		where["enddate"]=enddate;
        		if(secucode!=null && secucode.trim()!="" && secucode!="undefiend"){
        			where["secucode"]=secucode;
        		}
        		console.log(where);
        		//选择数据表格
            	selectTableByInterface(table,showUrl,interfaceId,where);
        	}else{
        	   //重新加载页面
               //parent.location.reload();
        	   window.location.reload(true);
        	}
        });
      
        //监听数据接口下拉框
        form.on('select(quarterInterfaces)',function(obj){
        	var interfaceId=$("#quarterInterfaces option:selected").attr("interface-id");
        	if(interfaceId!=null && interfaceId!="" && interfaceId!="undefiend"){
        		if(interfaceId=="1" || interfaceId=="2"){
        			$("#secucode").parent().parent().removeClass("layui-hide");
        		}else{
        			//清空一下
        			$("#secucode").val("");
        			$("#secucode").parent().parent().addClass("layui-hide");
        		}
        	}
        	form.render('select');//select是固定写法 不是选择器
        });
        
        //排序
    	table.on('sort(table)', function(obj){ //注：sort是排序事件名，table是table原始容器的属性 lay-filter="对应的值"
	    	 table.reload('myTable', {
	    		initSort: obj //记录初始排序，如果不设的话，将无法标记表头的排序状态。
	    		,where: { //请求参数
	    		   field: obj.field //排序字段
	    		  ,order: obj.type //排序方式
	    		}
	    	 });
    	});
    	
        //分页
        laypage.render({
            elem: 'pageDemo' //分页容器的id
            ,count: 100 //总页数
            ,skin: '#5a98ff' //自定义选中色值
            ,skip: true //开启跳页
            ,jump: function(obj, first){
                if(!first){
                    layer.msg('第'+ obj.curr +'页');
                }
            }
        });
        
        //下载
        $("#download").on("click",function () {
        	//接口Id
        	var interfaceId=$("#quarterInterfaces option:selected").attr("interface-id");
        	//接口对应下载文件的路径
        	var downloadUrl=$("#quarterInterfaces option:selected").attr("download-url");
        	//截止时间
        	var enddate=$("#year").val()+"-"+$("#date").val();
        	//股票代码
        	var secucode=$("#secucode").val();
        	if(interfaceId!=null && interfaceId!="" && interfaceId!="undefined"){
        		//获取列集合数据
        		var columns=queryColumnsByInterfaceId(interfaceId);
        		if(columns!=null && columns.length>0){
        			layer.open({
                        skin: 'layer-open',
                        type: 2,
                        title: '数据列',
                        shade: 0.6,
                        area: ['800px', '500px'],
                        btnAlign: 'c',
                        shadeClose: true,
                        content: [ctx+"page/columns?interfaceId="+interfaceId, 'yes'],
                        btn: ['确定', '取消'],
                        yes: function (index, layero) {
	                      	var body=layer.getChildFrame('body',index);
	                        var iframeWin=window[layero.find('iframe')[0]['name']];
	                        var treeObj=iframeWin.initTreeObj();
	                        var ids=getCheckedMenuIds(treeObj);
	                        console.log(ids);
	                        var columns="";
	                        for (var i = 1; i < ids.length; i++) {
	                        	columns=columns+ids[i]+",";
	      					}
	                        //参数
	                        var param={};
	                        //判断是否勾选了列
	                        if(columns!=null&&columns!=""){
	                        	columns=columns.substr(0,columns.lastIndexOf(","));
	                        	if(downloadUrl!=null && downloadUrl!="" && downloadUrl!="undefined"){
	                        		param["columns"]=columns;
	                        		param["enddate"]=enddate;
	                        		if(secucode!=null && secucode.trim()!="" && secucode!="undefiend"){
	                        			param["secucode"]=secucode;
	                        		}
	                          		$.post(ctx+downloadUrl,param,
	                                	  function(result) {
	              	        	     		 if (result.success) {
	              	        	     		    window.location.href = ctx+"common/download?fileName=" + encodeURIComponent(result.data) + "&delete=" + true;
	              	        	     		 } else {
	              	        	     			layer.msg(result.error, {shift: -1, time: 2000});
	              	        	     		 }
	              	        	     	  }
	                                );
	                          	}else{
	                          		layer.msg("对不起，服务器繁忙，请稍后再试！", {shift: -1, time: 2000});
	                          	}
	                         }else{//不勾选默认下载所有列
	                        	layer.confirm('不勾选默认下载全部列数据',{
	                  	            title:'信息'
	                  	        },function (index) {
	                  	        	if(downloadUrl!=null && downloadUrl!="" && downloadUrl!="undefined"){
	                  	        		param["enddate"]=enddate;
	                  	        		if(secucode!=null && secucode.trim()!="" && secucode!="undefiend"){
		                        			param["secucode"]=secucode;
		                        		}
	                              		$.post(ctx+downloadUrl,param,
	                                    	  function(result) {
	                  	        	     		 if (result.success) {
	                  	        	     		     window.location.href = ctx+"common/download?fileName=" + encodeURIComponent(result.data) + "&delete=" + true;
	                  	        	     		 } else {
	                  	        	     			 layer.msg(result.error, {shift: -1, time: 2000});
	                  	        	     		 }
	                  	        	     	  }
	                                    );
	                              	}else{
	                              		layer.msg("对不起，服务器繁忙，请稍后再试！", {shift: -1, time: 2000});
	                              	}
	                      			layer.close(index);
	                  	        });
	                        		
	                         }
                        },
                        btn2:function (index, layero) {
                            console.log("取消");
                        }
                    });
        		}else{
        			layer.msg("对不起，接口没有配置数据列，请联系管理员！", {shift: -1, time: 2500});
        		}
        	}else{
        		layer.alert('请选择有效的数据接口！', {
        			icon: 5,
        			title: "提示"
        		});
        	}
        });
        
    });
});

//接口数据表格
function selectTableByInterface(table,showUrl,interfaceId,where){
	//获取列集合数据
	var columns=queryColumnsByInterfaceId(interfaceId);
	//console.log(columns);
	if(columns!=null && columns.length>0){
		//表格
	    table.render({
	        elem: '#table'
	        ,url: ctx+showUrl
	        ,limit:10
	        ,limits:[10,20,50,100]
	    	,id:'myTable'
	        ,page: true
	        ,even:true
	        ,cols: [columns]
	    	,where:where
	        ,done: function (res, curr, count) {
	            $('th').css({'font-weight': 'bold'});
	        }
	    });
	}else{
		layer.msg("对不起，接口没有配置数据列，请联系管理员！", {shift: -1, time: 2500});
	}
}

//初始化季度
function initQuarter(){
	//获取当前时间的上一个季度时间
	var lastQuarter=getLastQuarter();
	var year=lastQuarter.substring(0,lastQuarter.indexOf("-"));
	var quarterDate=lastQuarter.substring(lastQuarter.indexOf("-")+1);
	$("#year").val(year);
	$("#date option[value="+quarterDate+"]").attr("selected","selected");
}
