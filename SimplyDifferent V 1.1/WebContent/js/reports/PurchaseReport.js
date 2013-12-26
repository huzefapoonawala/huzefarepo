(function() {
	require(["dijit/registry",
	         "dojo/_base/xhr",
	         "dijit/layout/BorderContainer",
	         "dijit/layout/ContentPane",
	         "dojox/layout/TableContainer",
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
	         "dojo/domReady!"], function(registry, xhr, BorderContainer, ContentPane, TableContainer, lang) {
		dojo.publish('sd/set/breadcrum',['Reports >> Purchase Report']);
		dojo.parser.parse();
		
		xhr.post({
			url:'../json/SupplierManager_fetchAllSuppliers.action',
			handleAs:'json',
			sync:true,
			load: function(data) {
				registry.byId('supplierId').set('store',new dojo.data.ItemFileReadStore({data:{identifier:'id',label:'supplierName',items:data.suppliers}}));
			}
		});
		
		registry.byId('searchForm').attr({
			onSubmit: function() {
				if (this.validate() && confirm('Are you sure, you want search with the selected details.')) {
					xhr.post({
						url:'../json/Reports_fetchPurchaseReport.action',
						handleAs:'json',
						form:'searchForm',
						sync:true,
						load: function(data) {
							registry.byId('reportGrid').setStore(dojo.data.ItemFileWriteStore({data:{identifier:'invoiceId',items:data.purchaseReport.invoices}}));
						}
					});
				}
				return false;
			}
		});
		
		var reportLayout = registry.byId('reportDiv');
		dojo.place(reportLayout.domNode,'bodyDiv');
		
		var struct = [[{name:'Supplier Name', field:'supplierName', width:'25%'},
			           {name:'Invoice Number', field:'invoiceNumber'},
			           {name:'Invoice Date', field:'invoiceDate'},
			           {name:'VAT', field:'vatAmount'},
			           {name:'Taxable', field:'netAmount'},
			           {name:'Total Amount', field:'totalAmount'}
			         ]];
//		var rgridStore = new dojo.data.ItemFileWriteStore({data:{identifier:'invoiceId',items:[]}});
		var rgrid = new dojox.grid.EnhancedGrid({
			id:'reportGrid',
			region: 'center',
			structure:struct,
			selectionMode:'none',
//			store:rgridStore,
			noDataMessage:'No invoices found'
		});
		reportLayout.addChild(rgrid);
	});
})();