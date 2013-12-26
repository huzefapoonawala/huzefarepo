(function () {
	dojo.provide("sd.common.CategoryUtil");
	
	dojo.require("sd.common.AjaxUtil");
	dojo.require("sd.common.SelectOptionsUtil");
	
	var tm = sd.common.CategoryUtil;
	
	tm.asJson = function (additionalParams) {
		var jsonObject = {items:[]};
		var params = {url:'../../forward/json/CategoryManager_categoryList.action',sync:true};
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
		return sd.common.SelectOptionsUtil.generateOptionsDS(tm.asJson(additionalParams),'categoryName');
	};
})();