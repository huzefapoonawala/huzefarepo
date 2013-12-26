(function () {
	dojo.provide("sd.common.SelectOptionsUtil");
	
	dojo.require("sd.common.CustomDS");
	
	var tm = sd.common.SelectOptionsUtil;
	
	tm.generateOptionsDS = function (items,valueKey,labelKey) {
		var ds = null;
		try {
			var options = [];
			for ( var i in items.items) {
				options.push({value:items.items[i][valueKey],label:items.items[i][labelKey ? labelKey : valueKey]});
			}
			ds = sd.common.CustomDS.generateCustomDS({identifier:'value',label:'label',items:options});
		} catch (e) {
			console.log(e);
		}
		return ds;
	};
	
	tm.getValueByLabel = function (opts,label) {
		var value = '';
		for ( var i = 0; i < opts.length; i++) {
			if (opts[i].label.toUpperCase() == label.toUpperCase()) {
				value += opts[i].value;
				break;
			}
		}
		return value;
	};
})();