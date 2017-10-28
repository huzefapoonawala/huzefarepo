(function() {
	var currData = {}, currInvNum, oldSQty, browseInvoiceAction, isInvRefresh=false, currInvIdx;
	function getSuppliers() {
		dojo.xhrPost({
			url:'../json/Supplier_fetchAllSuppliers.action',
			handleAs:'json',
			load: function(response) {
				dijit.byId('supplierId').set('store',new dojo.data.ItemFileReadStore({data:{identifier:'id', label:'supplierName', items:response.suppliers}}));
			}
		});
	}
	
	function createDataGrid() {
		var struct = [{
            type: "dojox.grid._CheckBoxSelector"
        },{
			cells: [
				[
				 	{name: "Invoice Number", field: "invoiceNumber", width:"13%"},
					{ name: "PO", field: "po", width:"14%"},
					{name:"Invoice Date", field:"invoiceDate", width:"12%"},
					{ name: "Ship To", field: "shipTo", width:"13%" },
					{ name: "Bill To", field: "billTo", width:"13%" },
					{ name: "Additional Cost", field: "shippingCost", width:"13%" },
					{ name: "Total Invoice Cost", field: "totalCost", width:"13%" },
					{name:'', field:'', width:'4%', align:'center',
			        	formatter: function(value,rIdx) { 
			                return '<img src="../images/view.gif" width="16px" height="16px" title="Click here to view invoice details" style="cursor:pointer;" onclick="dojo.publish(\'jh/pi/view/invoicedetails/selectedindex\',['+rIdx+']); dojo.publish(\'jh/pi/view/invoicedetails\',['+rIdx+'])" />'; 
			            } 
			        }
				]
			]
		}];
		var gridStore = new dojo.data.ItemFileWriteStore({data:{identifier:'invoiceNumber',items:[]}});
		var grid = new dojox.grid.EnhancedGrid({
//			id:'dataGrid',
			structure:struct,
			selectionMode:'multiple',
			style:{height:'450px', width:'100%'},
			store:gridStore,
			singleClickEdit:true,
			keepSelection:true,
			noDataMessage:'Please select file to upload.'
		},'dataGrid')/*.placeAt(dojo.body())*/;
		grid.startup();
		grid.attr({noDataMessage:'No invoices found in the uploaded file.'});
	}
	
	function configureInvoiceDetailsView() {
		dijit.byId('updateAllCb').attr({
			onClick: function() {
				var isUpdateRU = this.get('checked');
				dijit.byId('detailsDataGrid').get('store').fetch({
					onItem: function(item, request){
						request.store.setValue(item,'updateRP',isUpdateRU);
					}
				});
			}
		});
		dijit.byId('invoiceDetailDialog').attr({
			onHide: function() {
				var nuArr = [];
				dijit.byId('detailsDataGrid').get('store').fetch({
					onItem: function(item,request){
						if (!request.store.getValue(item,'updateRP')) {
							nuArr.push(request.store.getValue(item,'lineNumber'));
						}
					}
				});
				dojo.forEach(currData[currInvNum].invoiceDetails, function(item) {
					item.updateRP = nuArr.indexOf(item.lineNumber) == -1;
				});
				dojo.style('warningEl','display','none');
				dojo.style('aliasWarningEl','display','none');
			}
		});
		dojo.subscribe('jh/pi/view/invoicedetails', function(rIdx) {			
			currInvNum = dijit.byId('dataGrid').get('store').getIdentity(dijit.byId('dataGrid').getItem(rIdx));
			dijit.byId('invoiceDetailDialog').set('title','Details For Invoice Number '+currData[currInvNum].invoiceNumber);
			dijit.byId('invoiceDetailDialog').show();
			if (!dijit.byId('detailsDataGrid')) {
				createInvoiceDetailsGrid();
			}
			var items = dojo.clone(currData[currInvNum].invoiceDetails);
			dijit.byId('detailsDataGrid').setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'lineNumber',items:items}}));
			if (currData[currInvNum].skusNotAvailable || currData[currInvNum].skusAvailableInOrgillDB) {
				dojo.style('warningEl','display','');
			}
			if (currData[currInvNum].aliasesNotAvailable) {
				dojo.style('aliasWarningEl','display','');
			}
		});
		dojo.subscribe('jh/pi/view/invoicedetails/selectedindex', function(rIdx) {
			currInvIdx = rIdx;
		});
		dojo.subscribe('jh/pi/skusna', function() {
			dijit.byId('skusnaDialog').show();
			if (!dijit.byId('skusnaDataGrid')) {
				createSkusnaGrid();
			}
			if (!dijit.byId('skusAvailInOrgDataGrid')) {
				createSkusAvailInOrgDataGrid();
			};
			
			var items = [];
			if(currData[currInvNum].skusNotAvailable){
				dojo.forEach(currData[currInvNum].skusNotAvailable, function(sku) {
					items.push({sku:sku});
				});
			}
			dijit.byId('skusnaDataGrid').setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'sku',items:items}}));

			var availItems = [];
			if (currData[currInvNum].skusAvailableInOrgillDB) {
				dojo.forEach(currData[currInvNum].skusAvailableInOrgillDB, function(sku) {
					availItems.push({sku:sku});
				});
			}
			dijit.byId('skusAvailInOrgDataGrid').setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'sku',items:availItems}}));
		});
		dojo.subscribe('jh/pi/aliasna', function() {
			dijit.byId('aliasnaDialog').show();
			if (!dijit.byId('aliasesnaDataGrid')) {
				createAliasesnaGrid();
			}
			dijit.byId('aliasesnaDataGrid').setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'sku',items:currData[currInvNum].aliasesNotAvailable}}));
		});
	}
	
	function createInvoiceDetailsGrid() {
		var struct = [{
					defaultCell: { width: "12%" },
					cells:[
					       	{name:"Update Retail Price", align:'center', editable:true, type: dojox.grid.cells.Bool, field:"updateRP", width:'45px'},
					        {name: "Line Number", field: "lineNumber"},
					        {name:'Image', field:'image', width:'75px', align:'center',
						 		formatter: function(value) { 
						 			var src = value; 
						 			return "<img src=\"" + src + "\" width=\"70px\" height=\"70px\" />"; 
						 		} 
						 	},
					        {name: "SKU #", field: "sku"},
					        {name: "Quantity Received", field: "quantity"},
					        {name: "Quantity On Hand", field: "stockQuantity"},
					        {name: "Cost Price", field: "costPrice"},
					        {name: "Retail Price", field: "retailPrice"},
					        {name: "Restock Level", field: "restockLevel"},
					        {name: "Reorder Level", field: "reorderLevel"}/*,
					        {name:'', field:'', width:'15px', align:'center',
					        	formatter: function(value,rIdx) { 
					                return '<img src="../images/edit.gif" title="Click here to edit this item data" style="cursor:pointer;" onclick="dojo.publish(\'jh/pi/edititem\',['+rIdx+'])" />'; 
					            } 
					        }*/
					 ]
		}];
		var gridStore = new dojo.data.ItemFileWriteStore({data:{identifier:'lineNumber',items:[]}});
		var grid = new dojox.grid.EnhancedGrid({
			structure:struct,
			selectionMode:'none',
			style:{height:'400px', width:'99%'},
			store:gridStore,
			dodblclick: function(e) {
				dojo.publish('jh/pi/edititem',[e.rowIndex]);
			}
		},'detailsDataGrid');
		grid.startup();
	}
	
	function createSkusnaGrid() {
		var struct = [{
					defaultCell: { width: "100%" },
					cells:[
					        {name: "SKU #", field: "sku"}
					 ]
		}];
		var gridStore = new dojo.data.ItemFileWriteStore({data:{identifier:'sku',items:[]}});
		var grid = new dojox.grid.EnhancedGrid({
			structure:struct,
			selectionMode:'none',
			style:{height:'200px', width:'99%'},
			store:gridStore
		},'skusnaDataGrid');
		grid.startup();
	}

	function createSkusAvailInOrgDataGrid() {
		var struct = [{
            	type: "dojox.grid._CheckBoxSelector"
        	},{
					defaultCell: { width: "100%" },
					cells:[
					        {name: "SKU #", field: "sku"}
					 ]
		}];
		var gridStore = new dojo.data.ItemFileWriteStore({data:{identifier:'sku',items:[]}});
		var grid = new dojox.grid.EnhancedGrid({
			structure:struct,
			selectionMode:'multiple',
			style:{height:'200px', width:'99%'},
			store:gridStore
		},'skusAvailInOrgDataGrid');
		grid.startup();
	}
	
	function createAliasesnaGrid() {
		var struct = [{
					cells:[
					        {name: "SKU #", field: "sku"},
					        {name: "Alias", field: "upc", width:"70%"}
					 ]
		}];
		var gridStore = new dojo.data.ItemFileWriteStore({data:{identifier:'sku',items:[]}});
		var grid = new dojox.grid.EnhancedGrid({
			structure:struct,
			selectionMode:'none',
			style:{height:'300px', width:'99%'},
			store:gridStore
		},'aliasesnaDataGrid');
		grid.startup();
	}
	
	function configureEditDialog() {
		dojo.subscribe('jh/pi/edititem', function(rIdx){
			var grid = dijit.byId('detailsDataGrid'), store = grid.get('store'), item = grid.getItem(rIdx);
			dojo.forEach(['sku','retailPrice','costPrice','quantity', 'lineNumber'], function(key) {
				dijit.byId(key).set('value',store.getValue(item,key));
			});
			dojo.attr('rIdx','value',rIdx);
			var isNA = true;
			if (store.getValue(item,'id')) {
				isNA = false;
			}
			dojo.attr('id','value',isNA ? '' : store.getValue(item,'id'));
			oldSQty = store.getValue(item,'stockQuantity');
			dojo.forEach(['description', 'stockQuantity', 'restockLevel', 'reorderLevel', 'lastReceived', 'lastSold',  'onOrder', 'transferOut', 'committedQuantity'], function(el) {
				dijit.byId(el).set('disabled',isNA);
				if (!isNA) {
					dijit.byId(el).set('value',store.getValue(item,el));
				}
				else{
					dijit.byId(el).reset();
				}
			});
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
			dojo.style('addAliasIcon','visibility',isNA ? 'hidden' : 'visible');
		});
		dijit.byId('editForm').attr({
			onSubmit: function() {
				var rIdx = dojo.attr('rIdx','value'), grid = dijit.byId('detailsDataGrid'), store = grid.get('store'), item = grid.getItem(rIdx);
				var isValid = this.validate();
				if (store.getValue(item,'id')) {
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
				}
				if (isValid && confirm('Are you sure, you want to update the selected item\'s details.')) {
					if (store.getValue(item,'id')) {
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
					else{
						updateDetailsGrid();
					}
					/*dojo.forEach(['retailPrice','costPrice','quantity'], function(key) {
						store.setValue(item,key,dijit.byId(key).get('value'));
						currData[currInvNum].invoiceDetails[rIdx][key] = dijit.byId(key).get('value');
					});
					this.reset();*/
				}
				return false;
			},
			onReset: function() {
				dijit.byId('editDialog').hide();
				this.inherited("onReset", arguments);
			}
		});
	}
	
	function executeUpdate() {
		var arr = [], formData = dojo.formToObject('editForm');
		arr.push(formData);
		dojo.xhrPost({
			url:'../json/Item_updateItems.action',
			content:{itemsToModify: dojo.toJson(arr), updateCostPrice:false, updateRetailPrice:false},
			handleAs:'json',
			sync:true,
			load: function(response) {
				oldSQty = dijit.byId('stockQuantity').get('value');
				alert('Changes saved successfully');
				updateDetailsGrid();
			}
		});
	}
	
	function updateDetailsGrid() {
		var rIdx = dojo.attr('rIdx','value'), grid = dijit.byId('detailsDataGrid'), store = grid.get('store'), item = grid.getItem(rIdx), cdIdx;
		var lineNo = store.getIdentity(item);
		for ( var i = 0; i < currData[currInvNum].invoiceDetails.length; i++) {
			if (currData[currInvNum].invoiceDetails[i]['lineNumber'] == lineNo) {
				cdIdx = i;
				break;
			}
		}
		dojo.forEach(['retailPrice','costPrice','quantity', 'description', 'stockQuantity', 'restockLevel', 'reorderLevel', 'committedQuantity'], function(key) {
			if (!dijit.byId(key).get('disabled')) {
				store.setValue(item,key,dijit.byId(key).get('value'));
				currData[currInvNum].invoiceDetails[cdIdx][key] = dijit.byId(key).get('value');
			}
		});
		store.save();
		dijit.byId('editForm').reset();
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
		var store = dijit.byId('detailsDataGrid').get('store'), lNo = dijit.byId('lineNumber').get('value');
		store.fetchItemByIdentity({
			identity:lNo, 
			onItem: function(item){
				store.setValue(item,'aliases',aliases);
			}
		});
		for ( var i = 0; i < currData[currInvNum].invoiceDetails.length; i++) {
			var item = currData[currInvNum].invoiceDetails[i];
			if (item.lineNumber == lNo) {
				item.aliases = aliases;
				break;
			}
		}
	}
	
	function createFtpGrid() {
		var struct = [{
					cells:[
					       	{name: "FTP User", field: "userName", width:'25%'},
					        {name: "Invoice File Name", field: "fileName", width:'45%'},
					        {name: "Last Modified", field: "lastModified", width:'30%'}
					 ]
		}];
		var grid = new dojox.grid.EnhancedGrid({
			structure:struct,
			selectionMode:'single',
			style:{height:'300px', width:'99%'}/*,
			store:gridStore*/
		},'ftpDataGrid');
		grid.startup();
	}
	
	function loadInvoice(res, ioArgs) {
		currData = {};
		var invoices = [];
		dojo.forEach(res.invoices, function(inv) {
			var invoice = {};
			dojo.forEach(['invoiceNumber','po','shipTo','billTo','shippingCost','totalCost','invoiceDate'], function(key) {
				invoice[key] = inv[key];
			});
			invoices.push(invoice);
			currData[inv.invoiceNumber] = inv;
		});
		dijit.byId('dataGrid').setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'invoiceNumber',items:invoices}}));
		if (isInvRefresh) {
			dojo.publish('jh/pi/view/invoicedetails',[currInvIdx]);
		}
	}
	
	require(["dojo",
	         "dojo/parser",
	         "dijit/form/FilteringSelect",
	         "dijit/form/ValidationTextBox",
	         "dijit/form/Button",
	         "dijit/form/Form",
	         "dojo/data/ItemFileWriteStore",
	         "dojo/data/ItemFileReadStore",
	         "dojox/grid/EnhancedGrid",
	         "dojo/io/iframe",
	         "dijit/Dialog",
	         "dojox/grid/_CheckBoxSelector",
	         "dijit/form/CheckBox",
	         "dijit/form/NumberTextBox",
	         "dojox/layout/TableContainer",
	         "dijit/form/Textarea",
	         "dijit/form/Select",
	         "dojo/domReady!"], function() {
		dojo.publish('jh/set/breadcrum',['Invoice Uploader']);
		dojo.parser.parse();
		dijit.byId('uploadForm').placeAt('bodyDiv');
		dijit.byId('uploadForm').attr({
			style:'visibility:visible;',
			onSubmit: function() {
				var isValid = this.validate();
				if (isValid && dojo.attr('invoiceFile','value').length == 0) {
					alert('Kindly select file to upload');
					isValid = false;
				}
				if (isValid && confirm('Are you sure, you want to upload the selected file.')) {
					browseInvoiceAction = 'LOCAL';
					isInvRefresh = false;
					dojo.publish('jh/pi/showinvoicefromlocal', []);
				}
				return false;
			},
			onReset: function() {
				dojo.attr('invoiceFile','value','');
				this.inherited("onReset", arguments);
			}
		});
		dojo.subscribe('jh/pi/showinvoicefromlocal', function(){
			dijit.byId('dataGrid').set('noDataMessage','Loading invoices...');
			dijit.byId('dataGrid').setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'invoiceNumber',items:[]}}));
			dijit.byId('dataGrid').set('noDataMessage','No invoices found');
			dojo.io.iframe.send({
				url:'../json/InvoiceUploader_validateInvoiceFile.action',
				form:'uploadForm',
				handleAs:'json',
				load: loadInvoice,
				error: function(err) {
					console.log(err);
				}
			});
		});
		dijit.byId('saveForm').placeAt('bodyDiv');
		dijit.byId('saveForm').attr({
			style:'visibility:visible;',
			onSubmit: function() {
				var isValid = this.validate(), sInvoices = [], grid = dijit.byId('dataGrid'), store = grid.get('store');
				if (isValid) {
					sInvoices = grid.get('selection').getSelected();
					if (sInvoices.length == 0) {
						alert('Kindly select invoices to save');
						isValid = false;
					}
				}
				if (isValid && confirm('Are you sure, you want to save selected invoices.')) {
					var arr = [];
					dojo.forEach(sInvoices, function(item) {
						arr.push(dojo.clone(currData[store.getIdentity(item)]));
					});
					try {
						dojo.xhrPost({
							url:'../json/InvoiceUploader_saveInvoices.action',
							content:{invoiceData: dojo.toJson(arr)},
							form:'saveForm',
							handleAs:'json',
							sync:true,
							load: function(response) {
								alert('Invoices saved successfully.');
								dijit.byId('saveForm').reset();
							}
						});
					} catch (e) {
						console.log(e);
					}
				}
				return false;
			},
			onReset: function() {
//				dijit.byId('dataGrid').get('selection').deselectAll();
				dijit.byId('uploadForm').reset();
				currData = {};
				var grid = dijit.byId('dataGrid');
				grid.attr({noDataMessage:'Please select file to upload.'});
				grid.setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'invoiceNumber',items:[]}}));
				this.inherited("onReset", arguments);
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
		dijit.byId('ftpButton').attr({
			onClick: function() {
				dijit.byId('ftpDialog').show();
				if (!dijit.byId('ftpDataGrid')) {
					createFtpGrid();
				}
				dijit.byId('ftpDataGrid').set('noDataMessage','Loading please wait...');
				dijit.byId('ftpDataGrid').setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'id',items:[]}}));
				dojo.xhrPost({
					url:'../json/InvoiceUploader_fetchAllPIFiles.action',
					handleAs:'json',
					sync:false,
					load: function(response) {
						dijit.byId('ftpDataGrid').set('noDataMessage','No data found');
						dijit.byId('ftpDataGrid').setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'id',items:response.piFiles}}));
					}
				});
			}
		});
		dijit.byId('showFtpButton').attr({
			onClick: function() {
				if (!dijit.byId('ftpDataGrid').selection.getSelected()[0]) {
					alert('Kindly select a file');
					return;
				}
				if (!confirm('Are you sure, you want to see details of selected file.')) {
					return;
				}
				dijit.byId('ftpDialog').hide();
				browseInvoiceAction = 'FTP';
				isInvRefresh = false;
				dojo.publish('jh/pi/showinvoicefromftp', []);
			}
		});
		dojo.subscribe('jh/pi/showinvoicefromftp', function() {
			var grid = dijit.byId('ftpDataGrid'), store = grid.get('store'), item = grid.selection.getSelected()[0];
			dijit.byId('dataGrid').set('noDataMessage','Loading invoices...');
			dijit.byId('dataGrid').setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'invoiceNumber',items:[]}}));
			dijit.byId('dataGrid').set('noDataMessage','No invoices found');
			dojo.xhrPost({
				url:'../json/InvoiceUploader_showFtpFileDetails.action',
				handleAs:'json',
				content: {
					ftpFileName:store.getValue(item,'fileName'),
					ftpUserIdx:store.getValue(item,'userIdx')
				},
				sync:false,
				load: loadInvoice
			});
		});
		dijit.byId('addSkusButton').attr({
			onClick: function() {
				var grid = dijit.byId('skusAvailInOrgDataGrid'), store = grid.get('store');
				var selSkus = grid.get('selection').getSelected(), isValid = true;
				if (selSkus.length == 0) {
					alert('Kindly select SKUs to add');
					isValid = false;
				}
				if (isValid && confirm('Are you sure, you want to add the selected SKUs from orgill database.')) {
					var skus2Add = [];
					dojo.forEach(selSkus, function(sku) {
						skus2Add.push(store.getValue(sku, 'sku'));
					});
					dojo.xhrPost({
						url:'../json/Item_copyItemsFromOrgillDB.action',
						content:{skus:skus2Add},
						handleAs:'json',
						sync:true,
						load: function(response) {
							alert('SKUs added successfully, refreshing page...');
							grid.get('selection').clear();
							dijit.byId('skusnaDialog').hide();
							dijit.byId('invoiceDetailDialog').hide();
							isInvRefresh = true;
							dojo.publish(browseInvoiceAction == 'FTP' ? 'jh/pi/showinvoicefromftp' : 'jh/pi/showinvoicefromlocal', []);
						}
					});
				}
			}
		});
		getSuppliers();
		createDataGrid();
		configureInvoiceDetailsView();
		configureEditDialog();
		configureReasonCodeDialog();
	});
})();