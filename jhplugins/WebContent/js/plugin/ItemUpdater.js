(function() {
	var itemData = [], oldSQty, storeName = '';	
	function configureEditDialog() {
		dojo.subscribe('jh/iu/edititem', function(rIdx){
//			var grid = dijit.byId('dataGrid'), store = grid.get('store'), item = grid.getItem(rIdx);
			dojo.forEach(["lastReceived","lastSold"], function(key) {
				dijit.byId(key).reset();
			});
			dojo.forEach(['sku','retailPrice','costPrice','stockQuantity','committedQuantity','restockLevel','reorderLevel',"description","lastReceived","lastSold", 'onOrder', 'transferOut'/*,"aliases"*/], function(key) {
				dijit.byId(key).set('value',/*store.getValue(item,key)*/itemData[rIdx][key]);
			});
			dojo.attr('rIdx','value',rIdx);
			dojo.attr('id','value',/*store.getIdentity(item)*/itemData[rIdx]['id']);
			dojo.attr('binLocation','value',itemData[rIdx]['binLocation'] ? itemData[rIdx]['binLocation'] : '');
			oldSQty = itemData[rIdx]['stockQuantity'];
			dojo.empty('itemImage');
			dojo.attr('itemImage','innerHTML','<img width="90px" height="90px" src="'+/*store.getValue(item,'image')*/itemData[rIdx]['image']+'" alt="No image found">');
//			dijit.byId('editDialog').show();
			dijit.byId('editDiv').attr({style:{display:''}});
			if (!dijit.byId('aliasGrid')) {
				createAliasGrid();
			}
			var aarr = [];
			if (itemData[rIdx]['aliases']) {
				dojo.forEach(itemData[rIdx]['aliases'].split(','), function(alias) {
					aarr.push({alias:alias});
				});
			}
			dijit.byId('aliasGrid').setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'alias',items:aarr}}));
		});
		dijit.byId('editDiv').placeAt('bodyDiv');
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
				if (isValid && !(dijit.byId('restockLevel').get('value') == 0 && dijit.byId('reorderLevel').get('value') == 0 ) && dijit.byId('restockLevel').get('value') <= dijit.byId('reorderLevel').get('value')) {
					alert('Re-stock level should be greater than Re-order level');
					dijit.byId('restockLevel').focus();
					isValid = false;
				}
				if (isValid && confirm('Are you sure, you want to save changes made to item details.')) {
					/*if (dijit.byId('stockQuantity').get('value') != oldSQty) {
						dojo.attr('reasonCodeId','disabled',false);
						dojo.attr('changeQuantity','disabled',false);
						dijit.byId('rcDialog').show();
					}
					else {
						dojo.attr('reasonCodeId','disabled',true);
						dojo.attr('changeQuantity','disabled',true);
						executeUpdate();
					}*/
					if (!dijit.byId('reasonCode').get('disabled')) {
						dojo.attr('reasonCodeId','value',dijit.byId('reasonCode').get('value'));
						dojo.attr('changeQuantity','value',dijit.byId('stockQuantity').get('value')-oldSQty);
					}
					executeUpdate();
//					var grid = dijit.byId('dataGrid'), store = grid.get('store'), item = grid.getItem(dojo.attr('rIdx','value'));
					/*for(key in formData){
						if (['id'].indexOf(key) > -1) {
							continue;
						}
						store.setValue(item,key,formData[key]);
					}
					dijit.byId('editDialog').hide();*/
				}
				return false;
			},
			onReset: function() {
//				dijit.byId('editDialog').hide();
				dijit.byId('editDiv').attr({style:{display:'none'}});
				dojo.style('itemupdatedmsg','display','none');
				manageRCDisplay(false);
				this.inherited("onReset", arguments);
			}
		});
	}

	function executeUpdate() {
		var arr = [], formData = dojo.formToObject('editForm');
		arr.push(formData);
		dojo.xhrPost({
			url:'../json/Item_updateItems.action',
			content:{itemsToModify: dojo.toJson(arr)},
			handleAs:'json',
			sync:true,
			load: function(response) {
				oldSQty = dijit.byId('stockQuantity').get('value');
//				alert('Changes saved successfully');
				dojo.style('itemupdatedmsg','display','');
				manageRCDisplay(false);
				resetAndFocusSearchSku();
			}
		});
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
		/*dijit.byId('rcButton').attr({
			onClick: function() {
				dojo.attr('reasonCodeId','value',dijit.byId('reasonCode').get('value'));
				dojo.attr('changeQuantity','value',dijit.byId('stockQuantity').get('value')-oldSQty);
				dijit.byId('rcDialog').hide();
				executeUpdate();
			}
		});*/
	}
	
	function resetAndFocusSearchSku() {
		var el = dijit.byId('ssku');
		el.reset();
		el.focus();
	}
	
	function manageRCDisplay(isShow) {
		if (isShow) {
			dojo.style('reasonCodeEl','display','');
			dijit.byId('reasonCode').set('disabled',false);
			dojo.attr('reasonCodeId','disabled',false);
			dojo.attr('changeQuantity','disabled',false);
		} else {
			dojo.style('reasonCodeEl','display','none');
			dijit.byId('reasonCode').set('disabled',true);
			dojo.attr('reasonCodeId','disabled',true);
			dojo.attr('changeQuantity','disabled',true);
		}
	}
	
	require(["dojo",
	         "dojo/parser",
	         "dijit/form/ValidationTextBox",
	         "dijit/form/Form",
	         "dojox/layout/TableContainer",
	         "dijit/layout/ContentPane",
	         "dijit/form/Button",
	         "dijit/form/Select",
	         "dijit/form/NumberTextBox",
	         "dijit/form/Textarea",
	         "dijit/Dialog",
	         "dojo/data/ItemFileWriteStore",
	         "dojox/grid/EnhancedGrid",
	         "dijit/TitlePane",
	         "dojo/cookie",
	         "dojo/domReady!"], function() {
		dojo.publish('jh/set/breadcrum',['Item Updater']);
		dojo.parser.parse();
		dijit.byId('searchForm').placeAt('bodyDiv');
		dijit.byId('searchForm').attr({
			style:'visibility:visible;',
			onSubmit: function() {
				var isValid = this.validate();
				if (isValid && (dijit.byId('ssku').get('value').length == 0 /*&& dijit.byId('salias').get('value').length == 0*/)) {
					alert('Enter sku or alias');
					isValid = false;
				}
				if (isValid) {
					dojo.xhrPost({
						url:'../json/Item_fetchItemDetails.action',
						form:'searchForm',
						handleAs:'json',
						sync:true,
						load: function(response) {
							if (response.items.length > 0) {
								itemData = response.items;
								dojo.publish('jh/iu/edititem',[0]);
							} else {
								alert('No data found for the entered sku/alias');
							}
							resetAndFocusSearchSku();
							manageRCDisplay(false);
							dojo.style('itemupdatedmsg','display','none');
//							dijit.byId('dataGrid').setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'id',items:response.items}}));
						}
					});
				}
				return false;
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
					}
				});
			}
		});
		dojo.subscribe('iu/reasoncode/display', function() {
			var qty = dijit.byId('stockQuantity').get('value');
			if (!isNaN(qty)) {
				manageRCDisplay(dijit.byId('stockQuantity').get('value') != oldSQty);
			}
			else {
				manageRCDisplay(false);
			}
		});
		dojo.xhrPost({
			url:'../json/Common_fetchApplicationProperties.action',
			content:{'properties[0].propertyKey':'jhplugins.productlabel.storename'},
			handleAs:'json',
			sync:true,
			load: function(response) {
				if (response.properties) {
					storeName = response.properties[0].propertyValue;
				}
			}
		});
		dijit.byId('printLabelButton').attr({
			onClick : function() {
				var upcCode = '';
				try {
					if (dijit.byId('aliasGrid')) {
						var alGrid = dijit.byId('aliasGrid');
						upcCode = alGrid.get('store').getIdentity(alGrid.getItem(0));
					}
				} catch (e) {
					console.log(e);
				}
				if (upcCode.length == 0) {
					upcCode = dijit.byId('sku').get('value');
				}
				/*var data = {
					sku:dijit.byId('sku').get('value'),
					description:dijit.byId('description').get('value'),
					retailPrice:dijit.byId('retailPrice').get('value'),
					upcCode: upcCode,
					binLocation:dojo.attr('binLocation','value'),
					storeName:storeName
				};
				dojo.cookie('label.data', dojo.toJson(data));
				window.open('../forward/itemlabelgenerator.action', null, null, null);*/
				dojo.attr('label_sku','value',dijit.byId('sku').get('value'));
				dojo.attr('label_rp','value',dijit.byId('retailPrice').get('value'));
				dojo.attr('label_desc','value',dijit.byId('description').get('value'));
				dojo.attr('label_bl','value',dojo.attr('binLocation','value'));
				dojo.attr('label_alias','value',upcCode);
				dojo.byId('labelForm').submit();				
			}
		});
//		createDataGrid();
		configureEditDialog();
		configureReasonCodeDialog();
	});
})();