(function () {
	dojo.provide("sd.common.datagridUtil");
	
	dojo.require("sd.common.AjaxUtil");
	dojo.require("sd.common.dhtmlxUtil");
	
	sd.common.datagridUtil.loadGridData = function(data,datagrid,dataFormat,rootNodeKey,idKey,adjColArr) {
		datagrid.clearAll();
		if (data) {
			datagrid.parse(sd.common.dhtmlxUtil.convertJson2dhtmlxFomat(data,rootNodeKey,dataFormat,idKey),'json');
			dojo.forEach(adjColArr, function (colName) {
				datagrid.adjustColumnSize(datagrid.getColIndexById(colName));
			});
		}
	};
})();