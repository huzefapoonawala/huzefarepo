(function () {
	dojo.provide("sd.master.VariantManager");
	
	dojo.require("dijit.form.Form");
	dojo.require("dijit.form.ValidationTextBox");
	dojo.require("dijit.form.NumberTextBox");
	dojo.require("dijit.form.Select");
	dojo.require("dijit.form.Button");
	
	dojo.require("sd.common.datagridUtil");
	dojo.require("sd.common.CategoryUtil");
	dojo.require("sd.common.ProductUtil");
	dojo.require("sd.common.VariantUtil");
	dojo.require("sd.common.SelectOptionsUtil");
	
	var tm = sd.master.VariantManager;
	
	tm.getProductList = function (value) {
		dijit.byId('productId').setStore(sd.common.ProductUtil.asOptions({content:{'productVO.categoryName' : value}}),'8');
	};
	
	tm.getVariantList = function (value) {
		var dataFormat = ['product.categoryName','product.productName','variantName','purchaseDiscount','vat'];
		sd.common.datagridUtil.loadGridData(value.length <= 0 ? null : sd.common.VariantUtil.asJson({content:{'variantVO.productId' : value}}),tm.datagrid,dataFormat,'items','id',null);
	};
	
	function changeProduct(value) {
		this.value = value;
		this.setValue = function() {
			try {
				dijit.byId('productId').attr('value',sd.common.SelectOptionsUtil.getValueByLabel(dijit.byId("productId").attr('options'),this.value));
				dojo.unsubscribe(this.handle);
				console.log(this.handle);
			} catch (e) {
				console.log(e);
			}
		};
		this.handle = dojo.subscribe('topic/ChangeProduct', this, 'setValue');
	}
	
	dojo.addOnLoad(function () {
		var datagrid = tm.datagrid = new dhtmlXGridObject('VariantListDiv');
		datagrid.setImagePath("../../resources/dhtmlx/css/imgs/");
		datagrid.setHeader("Category name,Product name,Variant name,Purchase discount,VAT");
		datagrid.setInitWidthsP("25,28,28,12,5");
		datagrid.setColAlign("left,left,left,center,center");
		datagrid.setColTypes("ro,ro,ro,ro,ro");
		datagrid.setColSorting("str,str,str,int,int");
		datagrid.setColumnIds("categoryName,productName,variantName,purchaseDiscount,vat");
		datagrid.enableAutoWidth(true);
		datagrid.enableResizing("false,false,false,false,false");
		datagrid.setSkin("dhx_skyblue");
		datagrid.attachEvent("onRowSelect", function(id,ind){
			/*changeProduct(datagrid.cellById(id,datagrid.getColIndexById('productName')).getValue());
			dijit.byId("categoryName").attr('value',datagrid.cellById(id,datagrid.getColIndexById('categoryName')).getValue());*/
			dijit.byId("variantName").attr('value',datagrid.cellById(id,datagrid.getColIndexById('variantName')).getValue());
			dijit.byId("purchaseDiscount").attr('value',datagrid.cellById(id,datagrid.getColIndexById('purchaseDiscount')).getValue());
			dijit.byId("vat").attr('value',datagrid.cellById(id,datagrid.getColIndexById('vat')).getValue());
		});
		datagrid.init();
		dijit.byId('categoryName').setStore(sd.common.CategoryUtil.asOptions());
	});
})();