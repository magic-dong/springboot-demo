/**
 * Created by lzd on 2019/7/29.
 */
$(function () {
	//初始化接口数据
	initInterfaces("2","companyInterfaces");
	
    layui.use(['table','form','laypage','element','laydate','upload'], function(){

        var table = layui.table
            ,upload = layui.upload
            ,laydate = layui.laydate
            ,laypage = layui.laypage //分页
            ,form = layui.form
            ,layer=layui.layer;
        
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
        	var secucode=$("#secucode").val();
        	var showUrl=$("#companyInterfaces option:selected").val();
        	var interfaceId=$("#companyInterfaces option:selected").attr("interface-id");
        	if(showUrl!=null && showUrl!=""){
        		//选择数据表格
            	selectTableByInterface(table,showUrl,interfaceId,secucode);
        	}else{
        	   //重新加载页面
               //parent.location.reload();
        	   window.location.reload(true);
        	}
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
        	//股票代码
        	var secucode=$("#secucode").val();
        	//接口Id
        	var interfaceId=$("#companyInterfaces option:selected").attr("interface-id");
        	//接口对应下载文件的路径
        	var downloadUrl=$("#companyInterfaces option:selected").attr("download-url");
        	if(interfaceId!=null && interfaceId!="" && interfaceId!="undefined"){
        		//获取接口对应的数据列
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
                        	//判断是否勾选了列
                        	if(columns!=null&&columns!=""){
                        		columns=columns.substr(0,columns.lastIndexOf(","));
                        		if(downloadUrl!=null && downloadUrl!="" && downloadUrl!="undefined"){
                          		$.post(ctx+downloadUrl, 
                                			{
                                			   "secucode":secucode,
                                			   "columns":columns
                                			},
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
                              		$.post(ctx+downloadUrl, 
                                    			{
                                    			   "secucode":secucode
                                    			},
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
function selectTableByInterface(table,showUrl,interfaceId,secucode){
	//获取列集合数据
	var columns=queryColumnsByInterfaceId(interfaceId);
	//console.log(columns);
	if(columns!=null && columns.length>0){
		//表格
	    table.render({
	        elem: '#table'
	        ,url: ctx+showUrl
	        ,limit:12
	        ,limits:[12,20,50,100]
	    	,id:'myTable'
	        ,page: true
	        ,even:true
	        ,cols: [columns]
	    	,where:{'secucode':secucode}
	        ,done: function (res, curr, count) {
	            $('th').css({'font-weight': 'bold'});
	        }
	    });
	}else{
		layer.msg("对不起，接口没有配置数据列，请联系管理员！", {shift: -1, time: 2500});
	}
}
