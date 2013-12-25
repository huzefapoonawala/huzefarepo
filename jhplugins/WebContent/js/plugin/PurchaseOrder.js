(function() {
	var oldSQty;
	function createDataGrid() {
		var struct = [{
            type: "dojox.grid._CheckBoxSelector"
        },/*{
			defaultCell: { width: "80px" },
			noscroll: true,
			cells: [
			        {name:'Image', field:'image', width:'120px', align:'center',
			        	formatter: function(value) { 
			                var src = value; 
			                return "<img src=\"" + src + "\" width=\"90px\" height=\"90px\" />"; 
			            } 
			        },
			        {name: "SKU #", field: "sku"}
			]
		},*/{
			defaultCell: { width: "90px" },
			cells: [
				[
				 	{name:'Image', field:'image', width:'95px', align:'center', rowSpan: 2,
				 		formatter: function(value) { 
				 			var src = value; 
				 			return "<img src=\"" + src + "\" width=\"90px\" height=\"90px\" />"; 
				 		} 
				 	},
				 	{name: "SKU #", field: "sku", rowSpan: 2, width:"80px"},
					{ name: "Retail Price", field: "retailPrice", width:"75px" /*, editable:true, type: dojox.grid.cells._Widget, widgetClass:dijit.form.NumberTextBox*/ },
					{ name: "Cost Price", field: "costPrice", width:"75px" },
					{ name: "Quantity On Hand", field: "stockQuantity", width:"100px" },
					{ name: "Re-stock Level", field: "restockLevel" },
					{ name: "Re-order Level", field: "reorderLevel" },
					{ name: "Quantity Sold", field: "quantitySoldRecently", width:'80px' },
					{ name: "Quantity To Order", field: "orderQuantity", width:"80px"/*, editable:true, type: dojox.grid.cells._Widget, widgetClass:dijit.form.NumberTextBox*/ },
					{ name: "Total Order Cost", field: "totalOrderCost", width:'70px' },
					{name:'', field:'', width:'12px', align:'center',
			        	formatter: function(value,rIdx) { 
			                return '<img src="../images/edit.gif" title="Click here to edit this item data" style="cursor:pointer;" onclick="dojo.publish(\'jh/po/edititem\',['+rIdx+'])" />'; 
			            } 
			        }
				],[
					{ name: "Product Description", field: "description", colSpan: 9 }
				]
			]
		}];
		var gridStore = new dojo.data.ItemFileWriteStore({data:{identifier:'id',items:[]}});
		var grid = new dojox.grid.EnhancedGrid({
			id:'dataGrid',
			structure:struct,
			selectionMode:'multiple',
			style:{height:'550px'},
			store:gridStore,
			singleClickEdit:true,
			keepSelection:true,
			noDataMessage:'Please select/enter criterias from the "Filter Panel" above.'
		}).placeAt('gridForm','first');
		grid.startup();
		grid.attr({noDataMessage:'No data found for the selected/entered criterias.'});
	}
	
	function getSuppliers() {
		dojo.xhrPost({
			url:'../json/Supplier_fetchAllSuppliers.action',
			handleAs:'json',
			load: function(response) {
				dijit.byId('supplierId').set('store',new dojo.data.ItemFileReadStore({data:{identifier:'id', label:'supplierName', items:response.suppliers}}));
			}
		});
	}
	
	function configureEditDialog() {
		dojo.subscribe('jh/po/edititem', function(rIdx){
			var grid = dijit.byId('dataGrid'), store = grid.get('store'), item = grid.getItem(rIdx);
			dojo.forEach(['sku','retailPrice','costPrice','stockQuantity','restockLevel','reorderLevel','orderQuantity',"description",'lastReceived', 'lastSold', 'onOrder', 'transferOut', 'committedQuantity'], function(key) {
				dijit.byId(key).set('value',store.getValue(item,key));
			});
			dojo.attr('rIdx','value',rIdx);
			dojo.attr('id','value',store.getIdentity(item));
			oldSQty = store.getValue(item,'stockQuantity');
			dojo.empty('itemImage');
			dojo.attr('itemImage','innerHTML','<img width="90px" height="90px" src="'+store.getValue(item,'image')+'">');
			dijit.byId('editDialog').show();
			if (!dijit.byId('aliasGrid')) {
				createAliasGrid();
			}
			var aarr = [], aliases = store.getValue(item,'aliases');
			if (aliases) {
				dojo.forEach(aliases.split(','), function(alias) {
					aarr.push({alias:alias});
				});
			}
			dijit.byId('aliasGrid').setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'alias',items:aarr}}));
		});
		dijit.byId('editForm').attr({
			onSubmit: function() {
				var isValid = this.validate();
				if (isValid && (dijit.byId('restockLevel').get('value') < 0 || dijit.byId('reorderLevel').get('value') < 0)) {
					alert('Re-stock level and/or Re-order level should be >= 0');
					if (dijit.byId('restockLevel').get('value') < 0) {
						dijit.byId('restockLevel').focus();
					} else {
						dijit.byId('reorderLevel').focus();
					}
					isValid = false;
				}
				if (isValid && !(dijit.byId('restockLevel').get('value') == 0 && dijit.byId('reorderLevel').get('value') == 0) && dijit.byId('restockLevel').get('value') <= dijit.byId('reorderLevel').get('value')) {
					alert('Re-stock level should be greater than Re-order level');
					dijit.byId('restockLevel').focus();
					isValid = false;
				}
				if (isValid && confirm('Are you sure, you want to save changes made to item details.')) {
					if (dijit.byId('stockQuantity').get('value') != oldSQty) {
						dojo.attr('reasonCodeId','disabled',false);
						dojo.attr('changeQuantity','disabled',false);
						dijit.byId('rcDialog').show();
					}
					else {
						dojo.attr('reasonCodeId','disabled',true);
						dojo.attr('changeQuantity','disabled',true);
						executeUpdate();
					}
				}
				return false;
			},
			onReset: function() {
				dijit.byId('editDialog').hide();
			}
		});
	}
	
	function configureReasonCodeDialog() {
		dojo.xhrPost({
			url:'../json/Item_fetchReasonCodes.action',
			handleAs:'json',
			sync:true,
			load: function(response) {
				var items = [{id:0, description:'No reason code'}];
				if (response.reasonCodes && response.reasonCodes.length > 0) {
					items = response.reasonCodes;
				} 
				dijit.byId('reasonCode').setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'id',label:'description',items:items}}));
			}
		});
		dijit.byId('rcButton').attr({
			onClick: function() {
				dojo.attr('reasonCodeId','value',dijit.byId('reasonCode').get('value'));
				dojo.attr('changeQuantity','value',dijit.byId('stockQuantity').get('value')-oldSQty);
				dijit.byId('rcDialog').hide();
				executeUpdate();
			}
		});
	}
	
	function executeUpdate() {
		var grid = dijit.byId('dataGrid'), store = grid.get('store'), item = grid.getItem(dojo.attr('rIdx','value')), arr = [];
		var formData = dojo.formToObject('editForm');
		arr.push(formData);
		dojo.xhrPost({
			url:'../json/Item_updateItems.action',
			content:{itemsToModify: dojo.toJson(arr)},
			handleAs:'json',
			sync:true,
			load: function(response) {
				alert('Changes saved successfully');
			}
		});
		for(key in formData){
			if (['id'].indexOf(key) > -1) {
				continue;
			}
			store.setValue(item,key,formData[key]);
		}
		store.setValue(item,'totalOrderCost',dojox.math.round(formData['orderQuantity']*formData['costPrice'],2));
		dijit.byId('editDialog').hide();
	}
	
	function createAliasGrid() {
		var struct = [
					 	{name: "Alias", field: "alias", width:"180px"},
						{name:'', field:'', width:'15px', align:'center',
				        	formatter: function(value,rIdx) { 
				                return '<img src="../images/delete.gif" title="Click here to delete this alias" style="cursor:pointer;" onclick="dojo.publish(\'jh/iu/deletealias\',['+rIdx+'])" />'; 
				            } 
				        }
					];
		var gridStore = new dojo.data.ItemFileWriteStore({data:{identifier:'alias',items:[]}});
		var grid = new dojox.grid.EnhancedGrid({
			id:'aliasGrid',
			structure:struct,
			selectionMode:'none',
			style:{height:'105px', width:'230px'},
			store:gridStore
		});
		grid.placeAt('aliasEl');
		grid.startup();
	}
	
	function setAliases() {
		var aliases = '';
		dijit.byId('aliasGrid').get('store').fetch({
			onComplete: function(items,req){
				dojo.forEach(items, function(item){
					aliases += req.store.getIdentity(item)+",";
				});
			}
		});
		if (aliases.length > 0) {
			aliases = aliases.substring(0, aliases.length-1);
		}
		else{
			aliases = null;
		}
		var store = dijit.byId('dataGrid').get('store');
		store.fetchItemByIdentity({
			identity:dojo.attr('id','value'), 
			onItem: function(item){
				store.setValue(item,'aliases',aliases);
			}
		});
	}
	
	require(["dojo",
	         "dojo/parser",
	         "dojo/data/ItemFileWriteStore",
	         "dojo/data/ItemFileReadStore",
	         "dojox/grid/EnhancedGrid",
	         "dojox/grid/_CheckBoxSelector",
	         "dijit/TitlePane",
	         "dijit/form/DateTextBox",
	         "dijit/form/ValidationTextBox",
	         "dijit/form/FilteringSelect",
	         "dijit/form/Form",
	         "dojox/layout/TableContainer",
	         "dijit/layout/ContentPane",
	         "dijit/form/Button",
	         "dijit/form/NumberTextBox",
	         "dijit/form/Textarea",
	         "dijit/form/Select",
	         "dijit/form/CheckBox",
	         /*"dojox/grid/cells/dijit",*/
	         "dijit/Dialog",
	         "dojox/math/round",
	         "dojo/domReady!"], function() {
		dojo.publish('jh/set/breadcrum',['Plugins >> Purchase Order Creator']);
		dojo.parser.parse();
		dijit.byId('fpanel').placeAt('bodyDiv');
		dijit.byId('gridForm').placeAt('bodyDiv');
		createDataGrid();
		configureEditDialog();
		dijit.byId('supplierId').attr({
			queryExpr:"*${0}*"
		});
		dijit.byId('poForm').attr({
			onSubmit: function() {
				if (this.validate()) {
					dojo.xhrPost({
						url:'../json/PurchaseOrder_fetchDetailsForPurchaseOrder.action',
						form:'poForm',
						handleAs:'json',
						sync:true,
						load: function(response) {
							dijit.byId('dataGrid').setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'id',items:response.poDetails}}));
						}
					});
				}
				return false;
			}
		});
		dijit.byId('gridForm').attr({
			onSubmit: function() {
				var grid = dijit.byId('dataGrid');
				var sItems = [];
				if (grid.allItemsSelected) {
					dijit.byId('dataGrid').get('store').fetch({
						onComplete: function(items, req){
							sItems = items;
						}
					});
				} else {
					sItems = grid.selection.getSelected();
				}
				var isValid = sItems.length > 0;
				if (!isValid) {
					alert('Kindly select atleast one record to generate purchase order');
				}
				else{
					if (confirm('Are you sure, you want to generate po for selected records.')) {
						var sArr = [], store = dijit.byId('dataGrid').get('store');
						dojo.forEach(sItems, function(item) {
							var sJson = {};
							dojo.forEach(['sku','retailPrice','costPrice','orderQuantity','description','totalOrderCost','committedQuantity'], function(key) {
								sJson[key] = store.getValue(item,key);
							});
							sArr.push(sJson);
						});
						dojo.attr('poData','value',dojo.toJson(sArr));
						dojo.attr('poSupplierId','value',dijit.byId('supplierId').get('value'));
						dojo.attr('poSupplierName','value',dijit.byId('supplierId').get('displayedValue'));
					} else {
						isValid = false;
					}
				}
				return isValid;
			}
		});
		dijit.byId('addAliasForm').attr({
			onSubmit: function() {
				var isValid = this.validate();
				if (isValid) {
					dojo.xhrPost({
						url:'../json/Item_checkAliasUniqueness.action',
						form:'addAliasForm',
						handleAs:'json',
						sync:true,
						load: function(response) {
							if (!response.aliasValid) {
								alert('Alias already available. Alias should be unique.');
								dijit.byId('newAlias').focus();
								isValid = false;
							}
						}
					});
				}
				if (isValid && confirm('Are you sure, you want to add alias to this sku.')) {
					dojo.xhrPost({
						url:'../json/Item_addAlias.action',
						form:'addAliasForm',
						content:{itemId:dojo.attr('id','value')},
						handleAs:'json',
						sync:true,
						load: function(response) {
							alert('Alias added to selected item successfully');
							/*var alias = dijit.byId('aliases').get('value');
							if (alias.length > 0) {
								alias += ',';
							}
							alias += dijit.byId('newAlias').get('value');
							dijit.byId('aliases').set('value',alias);
							var grid = dijit.byId('dataGrid'), store = grid.get('store'), item = grid.getItem(dojo.attr('rIdx','value'));
							store.setValue(item,'aliases',alias);*/
							var store = dijit.byId('aliasGrid').get('store');
							store.newItem({alias:dijit.byId('newAlias').get('value')});
							store.save();
							setAliases();
							dijit.byId('addAliasForm').reset();
							dijit.byId('addAliasDialog').hide();
						}
					});
				}
				return false;
			}
		});
		dojo.subscribe('jh/iu/deletealias', function(rIdx) {
			if (confirm('Are you sure, you want to delete the selected alias.')) {
				var store = dijit.byId('aliasGrid').get('store'), item = dijit.byId('aliasGrid').getItem(rIdx);
				dojo.xhrPost({
					url:'../json/Item_deleteAlias.action',
					content:{alias:store.getIdentity(item)},
					handleAs:'json',
					sync:true,
					load: function(response) {
						alert('Alias deleted successfully');
						store.deleteItem(item);
						store.save();
						setAliases();
					}
				});
			}
		});
		/*dijit.byId('saveChangesButton').attr({
			onClick: function() {
				var store = dijit.byId('dataGrid').get('store');
				if (!store.isDirty()) {
					alert('There are no changes to be saved');
					return;
				}
				if (!confirm('Are you sure, you want to save changes made to item details.')) {
					return;
				}
				var items = store._pending._modifiedItems, arr = [];
				for ( var key in items) {
					var sJson = {};
					var item = store._itemsByIdentity[key];
					dojo.forEach(store.getAttributes(item), function(attr) {
						sJson[attr] = store.getValue(item, attr);
					});
					arr.push(sJson);
				}
				dojo.xhrPost({
					url:'../json/PurchaseOrder_updateItemsFromPO.action',
					content:{itemsToModify: dojo.toJson(arr)},
					handleAs:'json',
					sync:true,
					load: function(response) {
						alert('Changes saved successfully');
					}
				});
			}
		});*/
		getSuppliers();
		configureReasonCodeDialog();
	});
})();