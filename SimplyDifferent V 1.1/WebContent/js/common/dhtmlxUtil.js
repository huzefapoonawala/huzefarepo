(function () {
	dojo.provide('sd.common.dhtmlxUtil');
	
	var thisModule = sd.common.dhtmlxUtil;
	
	thisModule.convertJson2dhtmlxFomat = function(dataJson, rootNode,format,id) {
		var jsonArr = new Array();
		for ( var i in dataJson[rootNode]) {
			var subDataArr = new Array();
			try {
				for ( var j in format) {
					var value = null;
					if (typeof format[j].value != 'undefined') {
						value = format[j].value;
					}
					if (value == null) {
						/*var ids = new Array();
						if (dojo.isString(format[j])) {
							ids.push(format[j]);
						}
						else if (dojo.isObject(format[j])) {
							if (dojo.isString(format[j].id)) {
								ids.push(format[j].id);
							} else {
								ids = format[j].id;
								value = new Array();
							}
						}
						ids.forEach(function(id) {
							var val = null;
							id.split(".").forEach(function (node) {
								try {
									val = val == null ? dataJson[rootNode][i][node] : val[node];
								} catch (e) {
									console.log(e);
								}
							});
							if (dojo.isArray(value)) {
								value.push(val);
							} else {
								value = val;
							}
						});*/
						dojo.forEach((!dojo.isObject(format[j]) ? format[j] : format[j].id).split("."),function(node) {
							try {
								value = value == null ? dataJson[rootNode][i][node] : value[node];
							} catch (e) {
								console.log(e);
							}
						});
					}
					subDataArr.push(!dojo.isFunction(format[j].validate) ? value : format[j].validate(i,value,dataJson[rootNode][i]));
				}
			} catch (e) {
				console.log(e);
			}
			
			jsonArr.push({id:dataJson[rootNode][i][id],data:subDataArr});
		}
		return {rows:jsonArr};
	}
})();