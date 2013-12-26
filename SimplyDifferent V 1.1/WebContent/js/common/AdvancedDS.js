(function () {
	dojo.provide("boss.dsutil.AdvancedDS");
	
	dojo.require("dojox.data.QueryReadStore");
	
	var thisModule = boss.dsutil.AdvancedDS;
	
	thisModule.generateAdvancedDS = function (dataHash) {
		var fetchArgs = {
			onError : function (response, request) {
				if (dojo.isIE || response.stack.search("session expired") != -1) {
					dojo.publish(boss.defaulttopics.Sessionexpired,[]);
				}
			}
		};
		return new dojox.data.QueryReadStore(dojo.mixin({
			requestMethod : 'post',
			fetchMain : function(request) {
				dojo.mixin(request,fetchArgs);
				return this.inherited("fetch", arguments);
			}
		},dataHash));
	}
})();