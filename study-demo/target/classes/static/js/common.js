$(function(){
	$(document).click(function () {
        $(".btnSelect").hide();
    });
	
	//下拉按钮下拉
    $(".layui-btn-select").on("click",function (event) {
    	event.stopPropagation();
        $(this).find('.btnSelect').toggle();
    });
});
//form序列化为json
$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

//获取url后的参数值
function getUrlParam(key) {
	var href = window.location.href;
	var url = href.split("?");
	if(url.length <= 1){
		return "";
	}
	var params = url[1].split("&");
	
	for(var i=0; i<params.length; i++){
		var param = params[i].split("=");
		if(key == param[0]){
			return param[1];
		}
	}
}

function hoverOpenImg(){
    var img_show = null; // tips提示
    $('td img').hover(function(){
        //alert($(this).attr('src'));
        var img = "<img class='img_msg' src='"+$(this).attr('src')+"' style='width:180px;' />";
        img_show = layer.tips(img, this,{
            tips:[2, 'rgba(41,41,41,.5)']
            ,area: ['210px']
        });
    },function(){
        layer.close(img_show);
    });
    $('td img').attr('style','max-width:70px');
}

//获取当前时间的上一个季度时间
function getLastQuarter(){ 
	Date.prototype.format =function(format)  
    {  
	    var o = {  
	    "M+" : this.getMonth()+1, //month  
	    "d+" : this.getDate(), //day  
	    "h+" : this.getHours(), //hour  
	    "m+" : this.getMinutes(), //minute  
	    "s+" : this.getSeconds(), //second  
	    "q+" : Math.floor((this.getMonth()+3)/3), //quarter  
	    "S" : this.getMilliseconds() //millisecond  
	    }  
	    if(/(y+)/.test(format)) format=format.replace(RegExp.$1,  
	    (this.getFullYear()+"").substr(4- RegExp.$1.length));  
	    for(var k in o)if(new RegExp("("+ k +")").test(format))  
	    format = format.replace(RegExp.$1,  
	    RegExp.$1.length==1? o[k] :  
	    ("00"+ o[k]).substr((""+ o[k]).length));  
	    return format;  
    }; 
    var dayMSec = 24 * 3600 * 1000;
	var today = new Date();
	var time=today.format('yyyy-MM-dd');
	var year=time.substr(0,4);
	var month=time.substr(5,2);
	var date="";
	if(month=='01'||month=='02'||month=='03'){
		year=year-1;
		date="12-31";
	}else if(month=='04'||month=='05'||month=='06'){
		date="03-31";
	}else if(month=='07'||month=='08'||month=='09'){
		date="06-30";
	}else if(month=='10'||month=='11'||month=='12'){
		date="09-30";
	}
	var quarter=year+"-"+date;
    return quarter;
}  

//获取勾选的列
function getCheckedMenuIds(treeObj){
	var nodes = treeObj.getCheckedNodes(true);
	var length = nodes.length;
	var ids = [];
	for(var i=0; i<length; i++){
		var node = nodes[i];
		var id = node['id'];
		ids.push(id);
	}
	return ids;
}

//初始化数据接口下拉框接口数据
function initInterfaces(interfaceType,elementId){
	layui.use(['table','form','layer'], function(){
		 var table = layui.table
         ,form = layui.form
         ,layer=layui.layer;
		 
		 if(interfaceType!=null && interfaceType!=""){
			 $.ajax({
			        type : 'get',
			        url : ctx+'downloadExcle/interfaces?interfaceType='+interfaceType,
			        async: false,
			        success : function(result){
			            if(result != null){
			            	if(result.success){
			            		$(result.data).each(function(i,interfaces){
			            			var showUrl=interfaces.showUrl;
			            			var interfaceId=interfaces.interfaceId;
			            			var downloadUrl=interfaces.downloadUrl;
			            			var name=interfaces.name;
			            			var option=$("<option value='"+showUrl+"' interface-id='"+interfaceId+"' download-url='"+downloadUrl+"'>"+name+"</option>");
			            			$("#"+elementId).append(option);
			            		});
			            	}else{
			            		layer.msg(result.error, {shift: -1, time: 2000});
			            	}
			            }
			        },
			        error: function(xhr,textStatus,errorThrown){
			            var msg = xhr.responseText;
			            console.log(msg);
			        }
			 });
			 form.render('select');//select是固定写法 不是选择器 
		 }
	});
}

//根据接口查询对应列数据集合
function queryColumnsByInterfaceId(interfaceId){
	var columns=[];
	if(interfaceId!=null&&interfaceId!=""){
		//查询对应接口列数据集合
		layui.use(['table','form','layer'], function(){
			 var table = layui.table
	         ,form = layui.form
	         ,layer=layui.layer;
			 
			 $.ajax({
			        type : 'get',
			        url : ctx+'downloadExcle/columns?interfaceId='+interfaceId,
			        async: false,
			        success : function(result){
			            if(result != null){
			            	if(result.success){
			            		$(result.data).each(function(i,entity){
			            			var lineObj={};
			            			lineObj["field"]=entity.column;
			            			lineObj["title"]="<br/>"+entity.columnName;
			            			lineObj["align"]="center";
			            			var columnWidth=(entity.columnWidth!=null && entity.columnWidth>0)?entity.columnWidth:"158";
			            			lineObj["width"]=columnWidth;
			            			columns.push(lineObj);
			            		});
			            	}else{
			            		layer.msg(result.error, {shift: -1, time: 2000});
			            	}
			            }
			        },
			        error: function(xhr,textStatus,errorThrown){
			            var msg = xhr.responseText;
			            var response = JSON.parse(msg);
			            console.log(response);
			        }
			 });
		});
	}
	return columns;
}