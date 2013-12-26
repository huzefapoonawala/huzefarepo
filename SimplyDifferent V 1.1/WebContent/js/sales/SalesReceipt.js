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
	         "dijit/form/Select",
	         "dijit/form/NumberTextBox",
	         "dojo/data/ItemFileReadStore",
	         "dojo/data/ItemFileWriteStore",
	         "dojox/grid/EnhancedGrid",
	         "dijit/Dialog",
	         "dojo/domReady!"], function(registry, xhr, BorderContainer, ContentPane, TableContainer, TabContainer, lang) {
		dojo.publish('sd/set/breadcrum',['Sales >> Sales Receipt']);
		dojo.parser.parse();
		var tabs = new TabContainer({
			tabPosition: "top",
			style:{width:'100%',height:'600px'}
		},"bodyDiv");
		
		tabs.addChild(populateSalesReceiptTab());
		tabs.startup();
		
	
		function populateSalesReceiptTab() {
			var srTab = registry.byId('salesReceiptTab');
			var siGridStore = new dojo.data.ItemFileWriteStore({data:{identifier:'invoiceId',items:[]}});
			var struct = [[{name:'Customer Name', field:'customerName', width:'25%'},
			               {name:'Invoice Number', field:'invoiceNumber', width:'17%'},
				           {name:'Invoice Date', field:'invoiceDate', width:'13%'},
				           {name:'Total Billed Amount', field:'totalAmount'},
				           {name:'Amount Received', field:'receivedAmount'},
				           {name:'Amount Balance', field:'balanceAmount'},
				           {name:'', field:'', width:'15px', align:'center',
					        	formatter: function(value,rIdx) { 
					                return '<img src="../images/plus.gif" title="Click here to create invoice receipt" style="cursor:pointer;" onclick="dojo.publish(\'sd/salesreceipt/create\',['+rIdx+'])" />'; 
					            } 
					        }
				           ]];
			var siGrid = new dojox.grid.EnhancedGrid({
				id:'siGrid',
				region: 'center',
				structure:struct,
				selectionMode:'none',
				store:siGridStore,
				noDataMessage:''
			});
			xhr.post({
				url:'../json/CustomerManager_fetchAllCustomers.action',
				handleAs:'json',
				sync:true,
				load: function(data) {
					registry.byId('customerId').set('store',new dojo.data.ItemFileReadStore({data:{identifier:'id',label:'name',items:data.customers}}));
				}
			});
			registry.byId('customerId').on('change', function(value) {
				if (value) {
					registry.byId('salesInvoiceId').reset();
					xhr.post({
						url:'../json/SalesManager_fetchInvoicesByCustomer.action',
						handleAs:'json',
						content:{'invoice.customerId':value},
						sync:true,
						load: function(data) {
							registry.byId('salesInvoiceId').set('store',new dojo.data.ItemFileReadStore({data:{identifier:'invoiceId',label:'invoiceNumber',items:data.invoices}}));
						}
					});
				}
				else{
					registry.byId('salesInvoiceId').set('store',new dojo.data.ItemFileReadStore({data:{identifier:'invoiceId',label:'invoiceNumber',items:[]}}));
				}
			});
			
			var searchForm = dijit.byId('searchForm'); 
			searchForm.attr({
				onSubmit: function() {
					var isValid = this.validate();
					if (isValid && confirm('Are you sure, you want to search for invoices with provided details.')) {
						siGrid.attr('noDataMessage','Loading data please wait...');
						xhr.post({
							url:'../json/SalesManager_fetchInvoicesForSalesReceipt.action',
							handleAs:'json',
							form:'searchForm',
							sync:true,
							load: function(data) {
								siGrid.attr('noDataMessage','No outstanding');
								var store = new dojo.data.ItemFileWriteStore({data:{identifier:'invoiceId',items:data.invoices}});
								siGrid.setStore(store);
							}
						});
					}
					return false;
				}
			});
			registry.byId('searchButton').on('click', function() {
				registry.byId('searchForm').submit();
			});
			registry.byId('sResetButton').on('click', function() {
				searchForm.reset();
			});
			srTab.addChild(siGrid);
			return srTab;
		}
		
		function resetInvoiceGridStore(grid) {
			grid.setStore(dojo.data.ItemFileWriteStore({data:{identifier:'id',items:[]}})); 
		}
		function resetEl(elArr) {
			dojo.forEach(elArr, function(div) {
				registry.byId(div).reset();
			});
		}
		
		dojo.subscribe('sd/salesreceipt/create', function(rIdx) {
			var store = dijit.byId('siGrid').get('store'), item = dijit.byId('siGrid').getItem(rIdx);
			dojo.attr('rIdx','value',rIdx);
			dojo.attr('csrInvoiceId','value',store.getIdentity(item));
			dijit.byId('cCustomerName').set('value',store.getValue(item,'customerName'));
			dijit.byId('cInvoiceNumber').set('value',store.getValue(item,'invoiceNumber'));
			dijit.byId('cTotalAmount').set('value',store.getValue(item,'totalAmount'));
			dijit.byId('cBalanceAmount').set('value',store.getValue(item,'balanceAmount'));
			dijit.byId('cReceivedDate').set('value',new Date());
			dijit.byId('createReceiptDialog').show();
		});
		
		registry.byId('csidformDiv').attr({
			onSubmit: function() {
				var isValid=this.validate();
				if (isValid && confirm('Are you sure, you want to create the sales receipt for this invoice.')) {
					xhr.post({
						url:'../json/SalesManager_createSalesReceipt.action',
						handleAs:'json',
						form:'csidformDiv',
						sync:true,
						load: function(data) {
							var store = dijit.byId('siGrid').get('store'), item = dijit.byId('siGrid').getItem(dojo.attr('rIdx','value'));
							store.setValue(item,'receivedAmount',store.getValue(item,'receivedAmount')+dijit.byId('cReceivedAmount').get('value'));
							store.setValue(item,'balanceAmount',store.getValue(item,'balanceAmount')-dijit.byId('cReceivedAmount').get('value'));
							store.save();
							isValid = true;
						}
					});
					if (isValid) {
						this.reset();
					}
				}
				return false;
			},
			onReset: function() {
				dijit.byId('createReceiptDialog').hide();
				return true;
			}
		});
	});
})();