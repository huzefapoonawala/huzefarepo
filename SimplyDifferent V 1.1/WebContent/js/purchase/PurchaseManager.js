(function () {
	dojo.provide("sd.purchase.PurchaseManager");
	
	dojo.require("dijit.form.Form");
	dojo.require("dijit.form.ValidationTextBox");
	dojo.require("dijit.form.NumberTextBox");
	dojo.require("dijit.form.Select");
	dojo.require("dijit.form.Button");
	dojo.require("dijit.form.DateTextBox");
	
	dojo.require("sd.common.datagridUtil");
	dojo.require("sd.common.CategoryUtil");
	dojo.require("sd.common.ProductUtil");
	dojo.require("sd.common.VariantUtil");
	dojo.require("sd.common.SupplierUtil");
	dojo.require("sd.common.SelectOptionsUtil");
	
	var tm = sd.purchase.PurchaseManager;
	var iCount = 0;
	var dataArray = tm.dataArray = {items:[]};
	
	tm.getProductList = function (value) {
		dijit.byId('productId').setStore(sd.common.ProductUtil.asOptions({content:{'productVO.categoryName' : value}}),'8');
	};
	
	tm.getVariantList = function (value) {
		dijit.byId('variantId').setStore(value.length == 0 ? null : sd.common.VariantUtil.asOptions({content:{'variantVO.productId' : value}}));
	};
	
	function generateItemDatagrid() {
		var dataFormat = ['supplierName','variantText','itemNumber','quantityText','expiryDate','purchasePrice','purchaseDiscount'];
		for ( var i = 0; i < dataArray.items.length; i++) {
			dojo.mixin(dataArray.items[i],{id:i});
		}
		sd.common.datagridUtil.loadGridData(dataArray,tm.datagrid,dataFormat,'items','id',null);
		dojo.publish('topic/pm/ManageDeleteItemButton',[dataArray.items.length > 0]);
	}
	
	
	dojo.addOnLoad(function () {
		var datagrid = tm.datagrid = new dhtmlXGridObject('ItemListDiv');
		datagrid.setImagePath("../../resources/dhtmlx/css/imgs/");
		datagrid.setHeader("Supplier,Category - Product - Variant,Item number,Quantity,Expiry date,Price,Discount");
		datagrid.setInitWidthsP("13,*,8,8,10,6,7");
		datagrid.setColAlign("left,left,left,center,center,center,center");
		datagrid.setColTypes("ro,ro,ro,ro,ro,ro,ro");
		datagrid.setColSorting("str,str,na,int,str,str,int");
		datagrid.setColumnIds("supplierName,variantName,itemNumber,quantity,expiryDate,price,discount");
		datagrid.enableAutoWidth(true);
		datagrid.enableResizing("false,false,false,false,false,false,false");
		datagrid.setSkin("dhx_skyblue");
		datagrid.init();
		dijit.byId('supplierId').setStore(sd.common.SupplierUtil.asOptions());
		dijit.byId('categoryName').setStore(sd.common.CategoryUtil.asOptions());
		dojo.subscribe('topic/pm/AddItem',null,function(form){
			var dataJson = dojo.formToObject(form);
			dataArray.items.push(dojo.mixin(dataJson,{quantityText:dataJson.quantity+' '+dataJson.quantityType, variantText:dataJson.categoryName+' - '+dataJson.productName+' - '+dataJson.variantName}));
			generateItemDatagrid();
		});
		dojo.subscribe('topic/pm/DeleteItem',null,function(){
			if (datagrid.getSelectedId()) {
				dataArray.items.splice(datagrid.getSelectedId(), 1);
				generateItemDatagrid();
			}
			else {
				alert('Kindly select item from the table to delete');
			}
			
		});
	});
	
	tm.labelNameChange = function(el,toId,value) {
		if(value.length == 0)
			dojo.attr(toId,'value',value);
		else{
			el.store.fetchItemByIdentity({
				identity:value,
				onItem:function(item){
					dojo.attr(toId,'value',this.store.getValue(item,'label'));
				},
				onError:function(error){
					console.log(error);
				},
				scope:el
			});
		}
	};
})();