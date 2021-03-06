(function () {
	dojo.provide("sd.common.SupplierUtil");
	
	dojo.require("sd.common.AjaxUtil");
	dojo.require("sd.common.SelectOptionsUtil");
	
	var tm = sd.common.SupplierUtil;
	
	tm.asJson = function (additionalParams) {
		var jsonObject = {items:[]};
		var params = {url:'../../forward/json/SupplierManager_supplierList.action',sync:true};
		if (additionalParams) {
			dojo.mixin(params,additionalParams);
		}
		sd.common.AjaxUtil.doAjaxRequest(params)
			.addCallback(function (response) {
			jsonObject = response;
		});
		return jsonObject;
	};
	
	tm.asOptions = function (additionalParams) {
		return sd.common.SelectOptionsUtil.generateOptionsDS(tm.asJson(additionalParams),'id', 'supplierName');
	};
})();