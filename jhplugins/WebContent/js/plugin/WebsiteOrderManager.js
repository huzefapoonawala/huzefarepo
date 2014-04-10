(function() {
	
	var orders = [];
	
	function createDataGrid() {
		var struct = [{
            type: "dojox.grid._CheckBoxSelector"
        },{
			cells: [
				[
				 	{name: "Order Id", field: "orderId", width:"7%"},
				 	{name: "PO Number", field: "poNumber", width:"9%",
				 		formatter: function(value,rIdx) { 
			                return '<a href="javascript:void(0);" title="Click here to update po number" onclick="dojo.publish(\'jh/wo/update/ponumber\',['+rIdx+'])">'+value+'</a>'; 
			            } 
				 	},
					{name: "Customer Name", field: "customerName", width:"13%"},
					{name: "Shipping Address", field: "address"},
					{name: "Order Date", field:"dateAdded", width:"8%"},
					{name: "Order Status", field:"orderStatus", width:"8%"},
					{name: "Total Billed Amount", field: "totalBilledAmount", width:"6%" },
					{name:'Available In Store', field:'fulfillOrderAtStore', width:'7%', align:'center',
			        	formatter: function(value,rIdx) { 
			                return value ? ('<span style="color:green;">YES</span>') : ('<span style="color:red;">NO</span>'); 
			            } 
			        },
					{name:'', field:'', width:'3%', align:'center',
			        	formatter: function(value,rIdx) { 
			                return '<img src="../images/view.gif" width="16px" height="16px" title="Click here to view order details" style="cursor:pointer;" onclick="dojo.publish(\'jh/wo/view/details\',['+rIdx+'])" />'; 
			            } 
			        }
				]
			]
		}];
		var gridStore = new dojo.data.ItemFileWriteStore({data:{identifier:'orderId',items:[]}});
		var grid = new dojox.grid.EnhancedGrid({
			structure:struct,
			selectionMode:'multiple',
			style:{height:'450px', width:'100%'},
			store:gridStore,
			singleClickEdit:true,
			keepSelection:true,
			noDataMessage:'Please select orders to process.'
		},'dataGrid')/*.placeAt(dojo.body())*/;
		grid.startup();
		grid.attr({noDataMessage:'No orders to be processed.'});
	}
	
	function showWebsiteOrders() {
		dijit.byId('dataGrid').set('noDataMessage','Loading orders...');
		dijit.byId('dataGrid').setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'orderId',items:[]}}));
		dijit.byId('dataGrid').set('noDataMessage','No orders to be processed.');
		dojo.xhrPost({
			url:'../json/WebsiteProductsManager_fetchWebsiteOrders.action',
			handleAs:'json',
			sync:false,
			load: function(response) {
				orders = response.orders;
				var dgItems = [];
				for ( var i = 0; i < orders.length; i++) {
					dgItems.push({
						orderId:orders[i].orderId,
						customerName:orders[i].firstName+' '+orders[i].lastName,
						dateAdded:orders[i].dateAdded,
						totalBilledAmount:orders[i].totalBilledAmount,
						address:orders[i].shippingAddress,
						fulfillOrderAtStore:orders[i].fulfillOrderAtStore,
						oIdx:i,
						orderStatus:orders[i].orderStatus,
						poNumber:orders[i].poNumber
					});
					orders[i]['dgIdx'] = i;
				}
				var grid = dijit.byId('dataGrid');
				grid.setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'orderId',items:dgItems}}));
				grid.selection.clear();
			},
			error: function () {
				alert('Due to error could not complete process');
			}
		});
	}
	
	function configureOrderDetailsView() {
		dojo.subscribe('jh/wo/view/details', function(rIdx) {
			currOrdNum = dijit.byId('dataGrid').get('store').getIdentity(dijit.byId('dataGrid').getItem(rIdx));
			dijit.byId('orderDetailDialog').set('title','Details For Order Id '+orders[rIdx].orderId);
			dijit.byId('orderDetailDialog').show();
			if (!dijit.byId('detailsDataGrid')) {
				createOrderDetailsGrid();
			}
			var items = dojo.clone(orders[rIdx].itemsOnOrder);
			dijit.byId('detailsDataGrid').setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'sku',items:items}}));
		});
	}
	
	function configureUpdateInvoiceView() {
		dojo.subscribe('jh/wo/update/ponumber', function(rIdx) {
			var item = dijit.byId('dataGrid').getItem(rIdx), store = dijit.byId('dataGrid').get('store');
			dijit.byId('uinOrderId').set('value',store.getIdentity(item));
			dijit.byId('uinPoNumber').set('value',store.getValue(item,'poNumber'));
			dojo.attr('rIdx','value',rIdx);
			dijit.byId('updateInvoiceDialog').show();
		});
		dijit.byId('updateInoiceForm').attr({
			onSubmit: function() {
				var isValid = this.validate();
				if (isValid && confirm('Are you sure, you want to update the po number of the selected order.')) {
					dojo.xhrPost({
						url:'../json/WebsiteProductsManager_updatePoNumber.action',
						form:'updateInoiceForm',
						handleAs:'json',
						sync:false,
						load: function(response) {
							alert('PO number updated successfully.');
							dijit.byId('updateInvoiceDialog').hide();
							var rIdx = parseInt(dojo.attr('rIdx','value')), item = dijit.byId('dataGrid').getItem(rIdx), store = dijit.byId('dataGrid').get('store');
							store.setValue(item,'poNumber',dijit.byId('uinPoNumber').get('value'));
							orders[rIdx]['poNumber'] = dijit.byId('uinPoNumber').get('value');
							store.save();
						},
						error: function() {
							alert('Due to error could not complete process');
						}
					});
				}
				return false;
			}
		});
	}
	
	function createOrderDetailsGrid() {
		var struct = [{
					cells:[
					        {name: "SKU #", field: "sku", width: "100px"},
					        {name:'Image', field:'image', width:'75px', align:'center',
						 		formatter: function(value) { 
						 			var src = value; 
						 			return "<img src=\"" + src + "\" width=\"70px\" height=\"70px\" />"; 
						 		} 
						 	},
					        {name: "Order Quantity", field: "onOrder", width: "100px"},
					        {name: "Description", field: "description", width:'460px'}
					 ]
		}];
		var gridStore = new dojo.data.ItemFileWriteStore({data:{identifier:'sku',items:[]}});
		var grid = new dojox.grid.EnhancedGrid({
			structure:struct,
			selectionMode:'none',
			style:{height:'400px', width:'99%'},
			store:gridStore
		},'detailsDataGrid');
		grid.startup();
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
		dojo.publish('jh/set/breadcrum',['Website Order Manager']);
		dojo.parser.parse();
		dijit.byId('confirmForm').placeAt('bodyDiv');
		dijit.byId('confirmForm').attr({
			style:'visibility:visible;',
			onSubmit: function() {
				var isValid = this.validate(), toProcessOrders = [], grid = dijit.byId('dataGrid'), store = grid.get('store');
				if (isValid) {
					toProcessOrders = grid.get('selection').getSelected();
					if (toProcessOrders.length == 0) {
						alert('Kindly select orders to process');
						isValid = false;
					}
				}
				if (isValid && confirm('Are you sure, you want to process selected orders.')) {
					var arr = [];
					dojo.forEach(toProcessOrders, function(item) {
						arr.push(dojo.clone(orders[store.getValue(item,'oIdx')]));
					});
					try {
						dojo.xhrPost({
							url:'../json/WebsiteProductsManager_processOrders.action',
							content:{ordersToProcess: dojo.toJson(arr)},
							form:'confirmForm',
							handleAs:'json',
							sync:false,
							load: function(response) {
								alert('Orders processed successfully.');
								showWebsiteOrders();
							},
							error: function() {
								alert('Due to error could not complete process');
							}
						});
					} catch (e) {
						console.log(e);
					}
				}
				return false;
			},
			onReset: function() {
				orders = [];
				var grid = dijit.byId('dataGrid');
				grid.attr({noDataMessage:'Please select orders to process.'});
				grid.setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'orderId',items:[]}}));
				this.inherited("onReset", arguments);
			}
		});
		dijit.byId('showOrderButton').attr({
			onClick: function() {
				showWebsiteOrders();
			}
		});
		createDataGrid();
		configureOrderDetailsView();
		configureUpdateInvoiceView();
	});
})();