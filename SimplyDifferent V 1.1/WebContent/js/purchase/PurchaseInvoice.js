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
		dojo.publish('sd/set/breadcrum',['Purchase >> Purchase Invoice']);
		dojo.parser.parse();
		var count = 0, ecount = -1;
		var tabs = new TabContainer({
			tabPosition: "top",
			style:{width:'100%',height:'600px'}
		},"bodyDiv");		
		
		var struct = [[{name:'Brand Name', field:'brandName', width:'20%'},
			           {name:'Product Name', field:'productName', width:'27%'},
			           {name:'Batch Number', field:'batchNumber', width:'15%'},
			           {name:'Expiry Date', field:'expiryDate'},
			           {name:'Purchase Price (per kg/ltr)', field:'purchasePrice'},
			           {name:'Quantity', field:'quantity'},
			           {name:'VAT (in %)', field:'vat'},
			           {name:'Unit Type', field:'unitType'}
			           ]];
		
		tabs.addChild(populateCreateInvoiceTab());		
		tabs.addChild(populateEditInvoiceTab());
		tabs.startup();
		
		registry.byId('brandDiv').on('change', function(value) {
			if (value) {
				dojo.forEach(['productDiv','ppDiv','qtyDiv','vatDiv','utDiv'], function(div) {
					registry.byId(div).reset();
				});
				xhr.post({
					url:'../json/ProductManager_fetchProductsByBrand.action',
					content:{'brand.brandId':value},
					handleAs:'json',
					sync:true,
					load: function(data) {
						var store = new dojo.data.ItemFileReadStore({data:{identifier:'id',label:'productName',items:data.products}});
						registry.byId('productDiv').set('store',store);
					}
				});
			}
		});
		registry.byId('productDiv').on('change', function(value) {
			if (value) {
				this.get('store').fetchItemByIdentity({
					identity:value,
					onItem: function(item) {
						registry.byId('utDiv').set('value',this.getValue(item,'unitType'));
						registry.byId('ppDiv').set('value',this.getValue(item,'purchasePrice'));
						registry.byId('vatDiv').set('value',this.getValue(item,'vat'));
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
			url:'../json/SupplierManager_fetchAllSuppliers.action',
			handleAs:'json',
			sync:true,
			load: function(data) {
				registry.byId('supplierDiv').set('store',new dojo.data.ItemFileReadStore({data:{identifier:'id',label:'supplierName',items:data.suppliers}}));
				registry.byId('editSupplierDiv').set('store',new dojo.data.ItemFileReadStore({data:{identifier:'id',label:'supplierName',items:lang.clone(data.suppliers)}}));
			}
		});
		dijit.byId('pformDiv').attr({
			onSubmit: function() {
				if (this.validate() && confirm('Are you sure, you want to add particulars to invoice.')) {
					var tabId = dijit.byId('bodyDiv').selectedChildWidget.get('id');
					var store = registry.byId(tabId == 'createInvoiceTab' ? 'particularsGrid' : 'eParticularsGrid').get('store');
					store.newItem(dojo.mixin(dojo.formToObject('pformDiv'),{id:(tabId == 'createInvoiceTab' ? count++ : ecount--), isNew:true, productName:registry.byId('productDiv').get('displayedValue'),brandName:registry.byId('brandDiv').get('displayedValue'), expiryDate:dijit.byId('expiryDate').get('displayedValue')}));
					dijit.byId(tabId == 'createInvoiceTab' ? 'totalAmount' : 'editTotalAmount').set('value',calculateAndSetTotalAmount(store));
				}
				return false;
			},
			onReset: function() {
				var store = new dojo.data.ItemFileReadStore({data:{identifier:'id',label:'productName',items:[]}});
				registry.byId('productDiv').set('store',store);
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
					if (isValid) {
						xhr.post({
							url:'../json/PurchaseManager_isInvoiceNumberValid.action',
							form:'mainForm',
							handleAs:'json',
							sync:true,
							load: function(data) {
								if (!data.invoice.isInvoiceNumberValid) {
									isValid = false;
									alert('Invoice number already exist for the selected supplier');
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
										url:'../json/PurchaseManager_save.action',
										form:'mainForm',
										handleAs:'json',
										sync:true,
										load: function(data) {
											alert('Invoice saved successfully');
											window.location = '../forward/purchase_PurchaseInvoice.action';
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
				selectionMode:'none',
				store:epGridStore
			});
			registry.byId('editSupplierDiv').on('change', function(value) {
				if (value) {
					resetPartialEditForm();
					registry.byId('editInvoiceDiv').reset();
					xhr.post({
						url:'../json/PurchaseManager_fetchInvoicesBySupplier.action',
						handleAs:'json',
						content:{'supplier.id':value},
						sync:true,
						load: function(data) {
							registry.byId('editInvoiceDiv').set('store',new dojo.data.ItemFileReadStore({data:{identifier:'invoiceId',label:'invoiceNumber',items:data.invoices}}));
						}
					});
				}
				else{
					registry.byId('editInvoiceDiv').set('store',new dojo.data.ItemFileReadStore({data:{identifier:'invoiceId',label:'invoiceNumber',items:[]}}));
				}
			});
			registry.byId('addPEditButton').on('click', function() {
				registry.byId('pformDiv').reset();
				registry.byId('addPDialog').show();
			});
			var editForm = dijit.byId('editForm'); 
			editForm.attr({
				onReset: function() {
					registry.byId('editInvoiceDiv').set('store', new dojo.data.ItemFileReadStore({data:{identifier:'invoiceId',label:'invoiceNumber',items:[]}}));
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
									dojo.attr('editParticularsData','value',dojo.toJson(dataArr));
									xhr.post({
										url:'../json/PurchaseManager_edit.action',
										form:'editForm',
										handleAs:'json',
										sync:true,
										load: function(data) {
											alert('Invoice changes saved successfully');
											window.location = '../forward/purchase_PurchaseInvoice.action';
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
			dojo.subscribe('pi/invoice/load', function() {
				var invoiceId = registry.byId('editInvoiceDiv').get('value');
				if (invoiceId) {
					resetPartialEditForm();
					var store = dijit.byId('editInvoiceDiv').get('store');
					store.fetchItemByIdentity({
						identity:invoiceId,
						onItem: function(item) {
							registry.byId('editInvoiceDate').set('displayedValue',store.getValue(item,'invoiceDate'));
							registry.byId('editTotalAmount').set('value',store.getValue(item,'totalAmount'));
						}
					});
					xhr.post({
						url:'../json/PurchaseManager_fetchInvoiceDetails.action',
						content:{'invoice.invoiceId':invoiceId},
						handleAs:'json',
						load: function(data) {
							epGrid.setStore(dojo.data.ItemFileWriteStore({data:{identifier:'id',items:data.invoiceDetails}}));
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
							var samt = store.getValue(item,'purchasePrice')*store.getValue(item,'quantity');
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
			registry.byId('editInvoiceDate').reset();
			registry.byId('editTotalAmount').reset();
			dojo.publish('pi/isloaded',[false]);
		}
		
		function resetInvoiceGridStore(grid) {
			grid.setStore(dojo.data.ItemFileWriteStore({data:{identifier:'id',items:[]}})); 
		}
		dojo.subscribe('pi/isloaded', function(isLoaded) {
			registry.byId('addPEditButton').set('disabled', !isLoaded);
			registry.byId('saveChangesButton').set('disabled', !isLoaded);
		});
	});
})();