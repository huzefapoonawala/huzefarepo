(function () {
	dojo.provide("sd.master.CategoryManager");
	
	dojo.require("dijit.form.Form");
	dojo.require("dijit.form.ValidationTextBox");
	dojo.require("dijit.form.Button");
	dojo.require("dijit.form.Textarea");
	
	dojo.require("sd.common.datagridUtil");
	dojo.require("sd.common.CategoryUtil");
	
	dojo.addOnLoad(function () {
		var datagrid = new dhtmlXGridObject('CategoryListDiv');
		datagrid.setImagePath("../../resources/dhtmlx/css/imgs/");
		datagrid.setHeader("Category name,Description");
		datagrid.setInitWidthsP("35,64");
		datagrid.setColAlign("left,left");
		datagrid.setColTypes("ro,ro");
		datagrid.setColSorting("str,na");
		datagrid.setColumnIds("categoryName,desc");
		datagrid.enableAutoWidth(true);
		datagrid.enableResizing("false,false");
		datagrid.setSkin("dhx_skyblue");
		datagrid.init();
		
		var dataFormat = ['categoryName','description'];
		sd.common.datagridUtil.loadGridData(sd.common.CategoryUtil.asJson(),datagrid,dataFormat,'items','categoryName',null);
	});
})();