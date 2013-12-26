(function() {
	require(["dijit/registry",
	         "dojo/_base/xhr",
	         "dijit/layout/BorderContainer",
	         "dijit/layout/ContentPane",
	         "dojox/layout/TableContainer",
	         "dijit/layout/TabContainer",
	         "dojo/_base/lang",
	         "dojo/parser",
	         "dijit/form/FilteringSelect",
	         "dijit/form/ValidationTextBox",
	         "dijit/form/Form",
	         "dijit/form/Button",
	         "dijit/form/DateTextBox",
	         "dijit/form/ComboBox",
	         "dijit/form/NumberTextBox",
	         "dojo/data/ItemFileReadStore",
	         "dojo/data/ItemFileWriteStore",
	         "dojox/grid/EnhancedGrid",
	         "dijit/Dialog",
	         "dojo/domReady!"], function(registry, xhr, BorderContainer, ContentPane, TableContainer, TabContainer, lang) {
		dojo.publish('sd/set/breadcrum',['Sales >> Sales Invoice']);
		dojo.parser.parse();
		var deletedItems = [];
		var count = 0, ecount = -1;
		var tabs = new TabContainer({
			tabPosition: "top",
			style:{width:'100%',height:'600px'}
		},"bodyDiv");
		
		var struct = [[{name:'Brand - Product', field:'brandProduct', width:'30%'},
			           {name:'Batch Number', field:'batchNumber', width:'15%'},
			           {name:'Sales Price (per kg/ltr)', field:'salesPrice', width:'13%'},
			           {name:'Quantity', field:'quantity'},
			           {name:'VAT (in %)', field:'vat'},
			           {name:'Unit Type', field:'unitType'},
			           {name:'Expiry Date', field:'expiryDate'}
			           ]];
		
		tabs.addChild(populateCreateInvoiceTab());		
		tabs.addChild(populateEditInvoiceTab());
		tabs.startup();
		
		registry.byId('brandDiv').on('change', function(value) {
			if (value) {
				resetEl(['productDiv','ppDiv','qtyDiv','vatDiv','utDiv','productStock','productBatchStock']);
				xhr.post({
					url:'../json/ProductManager_fetchProductsByBrand.action',
					content:{'brand.brandId':value},
					handleAs:'json',
					sync:true,
					load: function(data) {
						registry.byId('productDiv').set('store',new dojo.data.ItemFileReadStore({data:{identifier:'id',label:'productName',items:data.products}}));
					}
				});
			}
		});
		registry.byId('productDiv').on('change', function(value) {
			resetEl(['batchNumber','productBatchStock','expiryDate','ppDiv']);
			if (value) {
				this.get('store').fetchItemByIdentity({
					identity:value,
					onItem: function(item) {
						registry.byId('utDiv').set('value',this.getValue(item,'unitType'));
						registry.byId('productStock').set('value',this.getValue(item,'stock')+calculateUnsavedProductStockIn(value)-calculateUnsavedProductStockOut(value, getSelectedTabGridStore()));
						registry.byId('vatDiv').set('value',this.getValue(item,'vat'));
						xhr.post({
							url:'../json/ProductManager_fetchBatchDetailsByProduct.action',
							content:{'product.id':value},
							handleAs:'json',
							sync:true,
							load: function(data) {
								var items = [];
								dojo.forEach(data.batchStockList, function(bJson) {
									items.push(dojo.mixin({batchExpiry:bJson.batchNumber+' ('+bJson.expiryDate+')'},bJson));
								});
								registry.byId('batchNumber').set('store',new dojo.data.ItemFileReadStore({data:{identifier:'id',label:'batchExpiry',items:items}}));
							}
						});
					},
					scope:this.get('store')
				});
				if (registry.byId('customerId').get('value')) {
					xhr.post({
						url:'../json/SalesManager_fetchDetailsForProductByCustomer.action',
						content:{'product.id':value,'customer.id':registry.byId('customerId')},
						handleAs:'json',
						sync:true,
						load: function(data) {
							if (data.details) {
								registry.byId('ppDiv').set('value',data.details.salesPrice);
							}
						}
					});
				}
			}
		});
		registry.byId('batchNumber').on('change', function(value) {
			if (value) {
				this.get('store').fetchItemByIdentity({
					identity:value,
					onItem: function(item) {
						registry.byId('productBatchStock').set('value',this.getValue(item,'quantity')+calculateUnsavedBatchStockIn(this.getValue(item,'productId'),this.getValue(item,'batchNumber'),this.getValue(item,'expiryDate'))-calculateUnsavedBatchStockOut(this.getValue(item,'productId'),this.getValue(item,'batchNumber'),this.getValue(item,'expiryDate'), getSelectedTabGridStore()));
						registry.byId('expiryDate').set('value',this.getValue(item,'expiryDate'));
						dojo.attr('batchNo','value',this.getValue(item,'batchNumber'));
					},
					scope:this.get('store')
				});
			}
		});
		xhr.post({
			url:'../json/BrandManager_fetchBrandList.action',
			handleAs:'json',
			sync:true,
			load: function(data) {
				var store = new dojo.data.ItemFileReadStore({data:{identifier:'brandId',label:'brandName',items:data.list}});
				registry.byId('brandDiv').set('store',store);
			}
		});
		xhr.post({
			url:'../json/CustomerManager_fetchAllCustomers.action',
			handleAs:'json',
			sync:true,
			load: function(data) {
				registry.byId('customerId').set('store',new dojo.data.ItemFileReadStore({data:{identifier:'id',label:'name',items:data.customers}}));
				registry.byId('editCustomerId').set('store',new dojo.data.ItemFileReadStore({data:{identifier:'id',label:'name',items:lang.clone(data.customers)}}));
			}
		});
		dijit.byId('pformDiv').attr({
			onSubmit: function() {
				var isValid = this.validate();
				if (isValid && registry.byId('qtyDiv').get('value') > registry.byId('productBatchStock').get('value')) {
					alert('The quantity entered is greater than stock of selected product. Please enter valid quantity.');
					registry.byId('qtyDiv').focus();
					isValid = false;
				}
				if (isValid && confirm('Are you sure, you want to add particulars to invoice.')) {
					var json = dojo.mixin(dojo.formToObject('pformDiv'),{id:(tabId == 'createInvoiceTab' ? count++ : ecount--), isNew:true, brandProduct:registry.byId('brandDiv').get('displayedValue')+' - '+registry.byId('productDiv').get('displayedValue'), expiryDate:dijit.byId('expiryDate').get('displayedValue')});
					var tabId = dijit.byId('bodyDiv').selectedChildWidget.get('id');
					var store = getSelectedTabGridStore();
					store.newItem(json);
					dijit.byId(tabId == 'createInvoiceTab' ? 'totalAmount' : 'editTotalAmount').set('value',calculateAndSetTotalAmount(store));
					var pDijit = registry.byId('productDiv');
					pDijit.get('store').fetchItemByIdentity({
						identity:pDijit.get('value'),
						onItem: function(item) {
							registry.byId('productStock').set('value',this.getValue(item,'stock')+calculateUnsavedProductStockIn(json.productId)-calculateUnsavedProductStockOut(json.productId, store));
						},
						scope:pDijit.get('store')
					});
					var pbDijit = registry.byId('batchNumber');
					pbDijit.get('store').fetchItemByIdentity({
						identity:pbDijit.get('value'),
						onItem: function(item) {
							registry.byId('productBatchStock').set('value',this.getValue(item,'quantity')+calculateUnsavedBatchStockIn(json.productId, json.batchNumber, json.expiryDate)-calculateUnsavedBatchStockOut(json.productId, json.batchNumber, json.expiryDate, store));
						},
						scope:pbDijit.get('store')
					});
						
				}
				return false;
			},
			onReset: function() {
				registry.byId('productDiv').set('store',new dojo.data.ItemFileReadStore({data:{identifier:'id',label:'productName',items:[]}}));
				registry.byId('batchNumber').set('store',new dojo.data.ItemFileReadStore({data:{identifier:'batchNumber',label:'batchNumber',items:[]}}));
				return true;
			}
		});
		function populateCreateInvoiceTab() {
			var bLayout = registry.byId('createInvoiceTab');
			registry.byId('addPButton').on('click', function() {
				registry.byId('pformDiv').reset();
				registry.byId('addPDialog').show();
			});
			registry.byId('deletePButton').on('click', function() {
				if (pGrid.get('selection').getSelected().length == 0) {
					alert('Kindly select row to delete');
				}
				else if(confirm('Are you sure, you want to delete selected row.')){
					pGridStore.deleteItem(pGrid.get('selection').getSelected()[0]);
					dijit.byId('totalAmount').set('value',calculateAndSetTotalAmount(pGridStore));
				}
			});
			registry.byId('saveInvoiceButton').on('click', function() {
				registry.byId('mainForm').submit();
			});
			dijit.byId('mainForm').attr({
				onSubmit: function() {
					var isValid = this.validate();
					if (isValid && registry.byId('customInvoiceNumber').get('value')) {
						xhr.post({
							url:'../json/SalesManager_isInvoiceNumberValid.action',
							content:{'invoice.invoiceNumber':registry.byId('customInvoiceNumber').get('value')},
							handleAs:'json',
							sync:true,
							load: function(data) {
								if (!data.invoice.isInvoiceNumberValid) {
									isValid = false;
									alert('Invoice number already exist');
								}
							}
						});
					}
					if (isValid) {
						pGridStore.fetch({
							query: {}, 
							onComplete: function(items, request){
								if (items.length == 0) {
									alert('Kindly enter atleast one particular for this invoice');
									isValid = false;
								}
								if (isValid && confirm('Are you sure, you want to save the invoice.')) {
									var dataArr = [];
									dojo.forEach(items, function(item) {
										var sjson = {};
										dojo.forEach(pGridStore.getAttributes(item), function(attr) {
											sjson[attr] = pGridStore.getValue(item,attr);
										});
										dataArr.push(sjson);
									});
									dojo.attr('particularsData','value',dojo.toJson(dataArr));
									xhr.post({
										url:'../json/SalesManager_save.action',
										form:'mainForm',
										handleAs:'json',
										sync:true,
										load: function(data) {
											alert('Invoice saved successfully');
											dojo.attr('downloadInvoiceId','value',data.invoice.invoiceId);
											dijit.byId('downloadForm').submit();
											window.location = '../forward/sales_SalesInvoice.action';
										},
										error: function(error) {
											alert('Due to some problem could not process request');
										}
									});
								}
							}, 
							queryOptions: {deep:true}
						});
					}
					return false;
				},
				onReset: function() {
					return true;
				}
			});
			xhr.post({
				url:'../json/SalesManager_fetchNextInvoiceNumber.action',
				handleAs:'json',
				sync:true,
				load: function(data) {
					registry.byId('invoiceNumber').set('value',data.nextInvoiceNumber);
				}
			});
			var pGridStore = new dojo.data.ItemFileWriteStore({data:{identifier:'id',items:[]}});
			var pGrid = new dojox.grid.EnhancedGrid({
				id:'particularsGrid',
				region: 'center',
				structure:struct,
				selectionMode:'single',
				store:pGridStore
			});
			bLayout.addChild(pGrid);
			return bLayout;
		}
		
		function populateEditInvoiceTab() {
			var editTab = registry.byId('editInvoiceTab');
			var epGridStore = new dojo.data.ItemFileWriteStore({data:{identifier:'id',items:[]}});
			var epGrid = new dojox.grid.EnhancedGrid({
				id:'eParticularsGrid',
				region: 'center',
				structure:struct,
				selectionMode:'single',
				store:epGridStore
			});
			registry.byId('editCustomerId').on('change', function(value) {
				if (value) {
					resetPartialEditForm();
					registry.byId('editInvoiceId').reset();
					xhr.post({
						url:'../json/SalesManager_fetchInvoicesByCustomer.action',
						handleAs:'json',
						content:{'invoice.customerId':value},
						sync:true,
						load: function(data) {
							registry.byId('editInvoiceId').set('store',new dojo.data.ItemFileReadStore({data:{identifier:'invoiceId',label:'invoiceNumber',items:data.invoices}}));
						}
					});
				}
				else{
					registry.byId('editInvoiceId').set('store',new dojo.data.ItemFileReadStore({data:{identifier:'invoiceId',label:'invoiceNumber',items:[]}}));
				}
			});
			registry.byId('addPEditButton').on('click', function() {
				registry.byId('pformDiv').reset();
				registry.byId('addPDialog').show();
			});
			registry.byId('deletePEditButton').on('click', function() {
				if (epGrid.get('selection').getSelected().length == 0) {
					alert('Kindly select row to delete');
				}
				else if(confirm('Are you sure, you want to delete selected row.')){
					var dItem = epGrid.get('selection').getSelected()[0];
					var store = epGrid.get('store');
					var sjson = {};
					dojo.forEach(store.getAttributes(dItem), function(attr) {
						sjson[attr] = store.getValue(dItem,attr);
					});
					dojo.mixin(sjson,{isDelete:true});
					deletedItems.push(sjson);
					store.deleteItem(dItem);
					dijit.byId('editTotalAmount').set('value',calculateAndSetTotalAmount(store));
				}
			});
			var editForm = dijit.byId('editForm'); 
			editForm.attr({
				onReset: function() {
					registry.byId('editInvoiceId').set('store', new dojo.data.ItemFileReadStore({data:{identifier:'invoiceId',label:'invoiceNumber',items:[]}}));
					resetInvoiceGridStore(epGrid);
					return true;
				}
			});
			registry.byId('saveChangesButton').on('click', function() {
				registry.byId('editForm').submit();
			});
			registry.byId('eResetButton').on('click', function() {
				resetPartialEditForm();
				editForm.reset();
			});
			dijit.byId('editForm').attr({
				onSubmit: function() {
					var isValid = this.validate();
					if (isValid) {
						var store = registry.byId('eParticularsGrid').get('store');
						store.fetch({
							query: {}, 
							onComplete: function(items, request){
								if (items.length == 0) {
									alert('Kindly enter atleast one particular for this invoice');
									isValid = false;
								}
								if (isValid && confirm('Are you sure, you want to save the changes for this invoice.')) {
									var dataArr = [];
									dojo.forEach(items, function(item) {
										var sjson = {};
										dojo.forEach(store.getAttributes(item), function(attr) {
											sjson[attr] = store.getValue(item,attr);
										});
										dataArr.push(sjson);
									});
									if (deletedItems) {
										dataArr = dataArr.concat(deletedItems);
									}
									dojo.attr('editParticularsData','value',dojo.toJson(dataArr));
									xhr.post({
										url:'../json/SalesManager_edit.action',
										form:'editForm',
										handleAs:'json',
										sync:true,
										load: function(data) {
											alert('Invoice changes saved successfully');
											dojo.attr('downloadInvoiceId','value',data.invoice.invoiceId);
											dijit.byId('downloadForm').submit();
											window.location = '../forward/sales_SalesInvoice.action';
										},
										error: function(error) {
											alert('Due to some problem could not process request');
										}
									});
								}
							}, 
							queryOptions: {deep:true},
							scope:store
						});
					}
					return false;
				},
				onReset: function() {
					return true;
				}
			});
			dojo.subscribe('si/invoice/load', function() {
				deletedItems = [];
				var invoiceId = registry.byId('editInvoiceId').get('value');
				if (invoiceId) {
					resetPartialEditForm();
					var store = dijit.byId('editInvoiceId').get('store');
					store.fetchItemByIdentity({
						identity:invoiceId,
						onItem: function(item) {
							registry.byId('editInvoiceDate').set('displayedValue',store.getValue(item,'invoiceDate'));
							registry.byId('editTotalAmount').set('value',store.getValue(item,'totalAmount'));
							registry.byId('editPoNumber').set('displayedValue',store.getValue(item,'poNumber'));
						}
					});
					xhr.post({
						url:'../json/SalesManager_fetchInvoiceDetails.action',
						content:{'invoice.invoiceId':invoiceId},
						handleAs:'json',
						load: function(data) {
							var invoiceData = data.invoiceDetails;
							dojo.forEach(invoiceData, function(node) {
								dojo.mixin(node,{brandProduct:node.brandName+' - '+node.productName});
							});
							epGrid.setStore(dojo.data.ItemFileWriteStore({data:{identifier:'id',items:invoiceData}}));
							dojo.publish('pi/isloaded',[true]);
						}
					});
				} else {
					alert('Kindly select/enter invoice number to load');
				}
			});
			editTab.addChild(epGrid);
			return editTab;
		}
		
		function calculateAndSetTotalAmount(store) {
			var totalAmt = '';
			store.fetch({
				query: {}, 
				onComplete: function(items, request){
					if(items.length > 0) {
						var amount = 0;
						dojo.forEach(items, function(item) {
							var samt = store.getValue(item,'salesPrice')*store.getValue(item,'quantity');
							amount += (samt+((samt*store.getValue(item,'vat'))/100));
						});
						totalAmt = amount;
					}
				}, 
				queryOptions: {deep:true}
			});
			return totalAmt;
		}
		
		function resetPartialEditForm() {
			resetInvoiceGridStore(registry.byId('eParticularsGrid'));
			resetEl(['editInvoiceDate','editTotalAmount','editPoNumber']);
			dojo.publish('pi/isloaded',[false]);
		}
		
		function resetInvoiceGridStore(grid) {
			grid.setStore(dojo.data.ItemFileWriteStore({data:{identifier:'id',items:[]}})); 
		}
		function resetEl(elArr) {
			dojo.forEach(elArr, function(div) {
				registry.byId(div).reset();
			});
		}
		function calculateUnsavedProductStockOut(productId, store) {
			var stockOut = 0;
			store.fetch({
				query:{},
				onComplete: function(items) {
					dojo.forEach(items, function(item) {
						if (store.hasAttribute(item,'isNew') && store.getValue(item,'isNew') && store.getValue(item,'productId') == productId) {
							stockOut += parseFloat(store.getValue(item,'quantity'));
						}
					});
				}
			});
			return stockOut;
		}
		function calculateUnsavedBatchStockOut(productId, batchNumber, expiryDate, store) {
			var stockOut = 0;
			store.fetch({
				query:{},
				onComplete: function(items) {
					dojo.forEach(items, function(item) {
						if (store.hasAttribute(item,'isNew') && store.getValue(item,'isNew') && store.getValue(item,'productId') == productId && store.getValue(item,'batchNumber') == batchNumber && store.getValue(item,'expiryDate') == expiryDate) {
							stockOut += parseFloat(store.getValue(item,'quantity'));
						}
					});
				}
			});
			return stockOut;
		}
		dojo.subscribe('pi/isloaded', function(isLoaded) {
			registry.byId('addPEditButton').set('disabled', !isLoaded);
			registry.byId('deletePEditButton').set('disabled', !isLoaded);
			registry.byId('saveChangesButton').set('disabled', !isLoaded);
		});
		dojo.subscribe('si/invoice/generatepdf', function() {
			var value = registry.byId('editInvoiceId').get('value');
			if (value) {
				dojo.attr('downloadInvoiceId','value',value);
				dijit.byId('downloadForm').submit();
			}
			else{
				alert('Kindly select/enter invoice number for which to generate pdf');
			}
		});
		
		function getSelectedTabGridStore() {
			return registry.byId(dijit.byId('bodyDiv').selectedChildWidget.get('id') == 'createInvoiceTab' ? 'particularsGrid' : 'eParticularsGrid').get('store');
		}
		
		function calculateUnsavedProductStockIn(productId) {
			var stockIn = 0;
			dojo.forEach(deletedItems, function(item) {
				if(item.productId == productId){
					stockIn += item.quantity;
				}
			});
			return stockIn;
		}
		
		function calculateUnsavedBatchStockIn(productId, batchNumber, expiryDate) {
			var stockIn = 0;
			dojo.forEach(deletedItems, function(item) {
				if(item.productId == productId && item.batchNumber == batchNumber && item.expiryDate == expiryDate){
					stockIn += item.quantity;
				}
			});
			return stockIn;
		}
	});
})();