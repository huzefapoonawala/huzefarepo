(function () {
	dojo.provide("sd.common.CustomDS");
	
	dojo.require("dojo.data.ItemFileWriteStore");

	var thisModule = sd.common.CustomDS;
	
	thisModule.generateCustomDS = function (dataHash) {
		return new dojo.data.ItemFileWriteStore({
			data : dataHash,
			clearOnClose : true,
			urlPreventCache : true,
			queryOptions : {deep:true}
		});
	}
})();