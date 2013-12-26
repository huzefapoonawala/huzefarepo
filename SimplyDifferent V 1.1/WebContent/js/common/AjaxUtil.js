(function () {
	dojo.provide("sd.common.AjaxUtil");
	
	var thisModule = sd.common.AjaxUtil;
	
	thisModule.doAjaxRequest = function (params) {
		var ajaxParams = {
				content : {},
				method : 'post',
				preventCache : true,
				handleAs : 'json',
				load : function(response, ioArgs){
					return response;
				},
				error : function(response, ioArgs){
					console.log(response);
					return response;
				}
			};
		dojo.mixin(ajaxParams,params);
		return dojo.xhrPost(ajaxParams);
	}
})();