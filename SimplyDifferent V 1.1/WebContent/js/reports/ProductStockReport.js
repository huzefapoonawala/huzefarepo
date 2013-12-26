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
		dojo.publish('sd/set/breadcrum',['Reports >> Stock Report']);
		dojo.parser.parse();
		
		xhr.post({
			url:'../json/BrandManager_fetchBrandList.action',
			handleAs:'json',
			sync:true,
			load: function(data) {
				registry.byId('brandId').set('store',new dojo.data.ItemFileReadStore({data:{identifier:'brandId',label:'brandName',items:data.list}}));
			}
		});
		
		registry.byId('searchForm').attr({
			onSubmit: function() {
				if (this.validate() && confirm('Are you sure, you want search with the selected details.')) {
					xhr.post({
						url:'../json/Reports_fetchProductStockReport.action',
						handleAs:'json',
						form:'searchForm',
						sync:true,
						load: function(data) {
							registry.byId('reportGrid').setStore(dojo.data.ItemFileWriteStore({data:{identifier:'id',items:data.productStockReport.products}}));
						}
					});
				}
				return false;
			}
		});
		
		var reportLayout = registry.byId('reportDiv');
		dojo.place(reportLayout.domNode,'bodyDiv');
		
		var struct = [[{name:'Brand Name', field:'brandName', width:'30%'},
			           {name:'Product Name', field:'productName', width:'30%'},
			           {name:'Default Purchase Price', field:'purchasePrice'},
			           {name:'Current Stock', field:'stock'},
			           {name:'Total Stock Value', field:'stockValue'}
			         ]];
//		var rgridStore = new dojo.data.ItemFileWriteStore({data:{identifier:'invoiceId',items:[]}});
		var rgrid = new dojox.grid.EnhancedGrid({
			id:'reportGrid',
			region: 'center',
			structure:struct,
			selectionMode:'none',
//			store:rgridStore,
			noDataMessage:'No products found'
		});
		reportLayout.addChild(rgrid);
	});
})();