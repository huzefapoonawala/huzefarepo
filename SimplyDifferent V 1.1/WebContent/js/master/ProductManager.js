(function () {
	require(["dijit/layout/BorderContainer",
	         "dijit/layout/ContentPane",
	         "dojo/parser",
	         "dojo/_base/xhr",
	         "dojo/domReady!",
	         "dijit/form/FilteringSelect",
	         "dijit/form/ValidationTextBox",
	         "dijit/form/Form",
	         "dijit/form/Button",
	         "dijit/form/NumberTextBox",
	         "dojo/data/ItemFileReadStore",
	         "dojox/grid/EnhancedGrid",
	         "dojo/data/ItemFileWriteStore",
	         "dijit/form/Select",
	         "dijit/Dialog"],
	         function(BorderContainer, ContentPane,parser, xhr){
		dojo.publish('sd/set/breadcrum',['Master >> Product Manager']);
		var bLayout = new BorderContainer({
		    design: "headline",
		    style:{
		    	width:'100%',
		    	height:'550px'
		    }
		}, "bodyDiv"), 
		sbLayout = new BorderContainer({
		    design: "headline",
		    region:'center',
		    style:{
		    	width:'100%',
		    	height:'100%'
		    }
		});
		var form = new dijit.form.Form({
			region: "left",
			splitter: false,
			style:{
				width:'300px'
			},
			onSubmit: function() {
				var isValid = this.validate();
				if (isValid) {
					xhr.post({
						url:'../json/ProductManager_checkProductName.action',
						handleAs:'json',
						form : 'formDiv',
						sync:true,
						load: function(data) {
							isValid = data.success;
						}
					});
					if (!isValid) {
						alert('Product already exist for selected brand.');
					}
				}
				if (isValid && confirm('Are you sure, you want to save the new product.')) {
					xhr.post({
						url:'../json/ProductManager_save.action',
						handleAs:'json',
						form : 'formDiv',
						sync:true,
						load: function(data) {
							alert(data.result);
							if (data.success) {
								populateProducts();
								dijit.byId('formDiv').reset();
							}
						}
					});
				}
				return false;
			}
		}, "formDiv");		
		bLayout.addChild(form);
		sbLayout.addChild(new ContentPane({
			region:'top',
			/*style:{
				height:30
			},*/
			content:'<span id="editButtonDiv"></span>&nbsp;&nbsp<span id="deleteButtonDiv"></span>'
		}));
		var pGrid = new dojox.grid.EnhancedGrid({
			id:'productsGrid',
			region: "center",
			structure:[[{name:'Brand Name', field:'brandName', width:'22%'},
			           {name:'Product Name', field:'productName', width:'34%'},
			           {name:'Purchase Price (Per Kg/Ltr)', field:'purchasePrice', width:'14%'},
			           {name:'VAT (In %)', field:'vat', width:'10%'},
			           {name:'Unit Type', field:'unitType', width:'10%'},
			           {name:'Reorder Limit', field:'reorderLimit', width:'10%'}
			           ]],
			selectionMode:'single'		           
		});
		sbLayout.addChild(pGrid);
		bLayout.addChild(sbLayout);
		bLayout.startup();
		parser.parse();
		
		new dijit.form.Form({
			onSubmit: function() {
				var isValid = this.validate();
				/*if (isValid) {
					xhr.post({
						url:'../json/ProductManager_checkProductName.action',
						handleAs:'json',
						form : 'editFormDiv',
						sync:true,
						load: function(data) {
							isValid = data.success;
						}
					});
					if (!isValid) {
						alert('Product already exist for selected brand.');
					}
				}*/
				if (isValid && confirm('Are you sure, you want to save changes for the selected product.')) {
					xhr.post({
						url:'../json/ProductManager_edit.action',
						handleAs:'json',
						form : 'editFormDiv',
						sync:true,
						load: function(data) {
							alert(data.result);
							if (data.success) {
								populateProducts();
								dijit.byId('editDialog').hide();
							}
						}
					});
				}
				return false;
			}
		}, "editFormDiv");
		new dijit.form.Button({
			label:'Edit Selected',
			onClick: function() {
				if (pGrid.get('selection').getSelected().length) {
					dijit.byId('editFormDiv').reset();
					var store = pGrid.get('store');
					var item = pGrid.get('selection').getSelected()[0];
					dijit.byId('editBrandName').set('value',store.getValue(item,'brandName'));
					dojo.attr('editBrandId','value',store.getValue(item,'brandId'));
					dojo.attr('editProductId','value',store.getIdentity(item));
					dijit.byId('editProductName').set('value',store.getValue(item,'productName'));
					dijit.byId('editPurchasePrice').set('value',store.getValue(item,'purchasePrice'));
					dijit.byId('editVat').set('value',store.getValue(item,'vat'));
					dijit.byId('editUnitType').set('value',store.getValue(item,'unitType'));
					dijit.byId('editRLimit').set('value',store.getValue(item,'reorderLimit'));
					dijit.byId('editDialog').show();
				} else {
					alert('Kindly select row to edit');
				}
			}
		},'editButtonDiv');
		new dijit.form.Button({
			label:'Delete Selected',
			onClick: function() {
				if (pGrid.get('selection').getSelected().length) {
					if (confirm('Are you sure, you want to delete selected product.')) {
						var pId = pGrid.get('store').getIdentity(pGrid.get('selection').getSelected()[0]);
						xhr.post({
							url:'../json/ProductManager_delete.action',
							handleAs:'json',
							content:{'product.id':pId},
							load: function(data) {
								alert(data.result);
								if (data.success) {
									populateProducts();
								}
							}
						});
					}
				} else {
					alert('Kindly select row to delete');
				}
			}
		},'deleteButtonDiv');
		xhr.post({
			url:'../json/BrandManager_fetchBrandList.action',
			handleAs:'json',
			sync:true,
			load: function(data) {
				var store = new dojo.data.ItemFileReadStore({data:{identifier:'brandId',label:'brandName',items:data.list}});
				dijit.byId('brandDiv').set('store',store);
			}
		});
		var populateProducts = function() {
			xhr.post({
				url:'../json/ProductManager_fetchAllProducts.action',
				handleAs:'json',
				load: function(data) {
					var sdata = {identifier:'id',items:[]};
					dojo.forEach(data.products, function(node) {
						var dataJson = node;
						dojo.mixin(dataJson,node.brand);
						dataJson.brand = null;
						sdata.items.push(dataJson);
					});
					var store = new dojo.data.ItemFileWriteStore({data:sdata});
					pGrid.setStore(store);
				}
			});
		};
		populateProducts();
		
	});
})();