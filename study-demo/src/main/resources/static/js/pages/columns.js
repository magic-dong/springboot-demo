/**
 * Created by lzd on 2017/7/27.
 */
function getSettting() {
	var setting = {
	        check: {
	            enable: true
	        },
	        data: {
	            simpleData: {
	                enable: true
	            }
	        }
	 };
	  
	return setting;
};

function getMenuTree(columns) {
	var zNodes =[];
	var root = {
			id : 0,
			pId : 0,
			name : "全选",
			open : true
	};
	zNodes.push(root);
	//判断是否有数据
	if(columns!=null && columns.length>0){
		var length = columns.length;
		for (var i = 0; i < length; i++) {
			var entity = columns[i];
			var node = createNode(entity);
			zNodes[i+1]=node;
		}
	}
	return zNodes;
};

function createNode(entity) {
	var column = entity.column;
	var columnName = entity.columnName;
	var node = {
		id : column,
		name : columnName,
		pId : 0,
		open : true
	};
	return node;
};
