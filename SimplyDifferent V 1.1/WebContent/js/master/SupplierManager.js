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
	         "dijit/form/Textarea",
	         "dijit/Dialog"],
	         function(BorderContainer, ContentPane,parser, xhr){
		dojo.publish('sd/set/breadcrum',['Master >> Supplier Manager']);
		parser.parse();
		dijit.byId('formDiv').attr({
			onSubmit: function() {
				var isValid = this.validate();
				/*if (isValid) {
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
				}*/
				if (isValid && confirm('Are you sure, you want to save the new supplier.')) {
					xhr.post({
						url:'../json/SupplierManager_save.action',
						handleAs:'json',
						form : 'formDiv',
						sync:true,
						load: function(data) {
							alert('Supplier saved successfully');
							dijit.byId('formDiv').reset();
							populateSupplier();
						},
						error: function() {
							alert('Due to some problem could not process request');
						}
					});
				}
				return false;
			}
		}, "formDiv");		
		var bLayout = dijit.byId('supplierLayoutDiv'),
		sbLayout = new BorderContainer({
		    design: "headline",
		    region:'center',
		    style:{
		    	width:'100%',
		    	height:'100%'
		    }
		});
		sbLayout.addChild(new ContentPane({
			region:'top',
			content:'<span id="editButtonDiv"></span>&nbsp;&nbsp<span id="deleteButtonDiv"></span>'
		}));
		var pGrid = new dojox.grid.EnhancedGrid({
			id:'suppliersGrid',
			region: "center",
			structure:[[{name:'Supplier Name', field:'supplierName', width:'15%'},
			           {name:'Address', field:'supplierAddress', width:'25%'},
			           {name:'Contact Number', field:'supplierContact', width:'15%'},
			           {name:'Email', field:'supplierEmail', width:'18%'},
			           {name:'Website', field:'supplierWebsite', width:'12%'},
			           {name:'VAT Number', field:'vatNumber', width:'12%'}
			           ]],
			selectionMode:'single'		           
		});
		sbLayout.addChild(pGrid);
		bLayout.addChild(sbLayout);
		bLayout.placeAt('bodyDiv');
		dijit.byId('editFormDiv').attr({
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
				if (isValid && confirm('Are you sure, you want to save changes for the selected supplier.')) {
					xhr.post({
						url:'../json/SupplierManager_edit.action',
						handleAs:'json',
						form : 'editFormDiv',
						sync:true,
						load: function(data) {
							alert('Changes saved successfully');
							populateSupplier();
							dijit.byId('editDialog').hide();
						},
						error: function() {
							alert('Due to some problem could not process request');
						}
					
					});
				}
				return false;
			}
		});
		new dijit.form.Button({
			label:'Edit Selected',
			onClick: function() {
				if (pGrid.get('selection').getSelected().length) {
					dijit.byId('editFormDiv').reset();
					var store = pGrid.get('store');
					var item = pGrid.get('selection').getSelected()[0];
					dojo.attr('editSupplierId','value',store.getIdentity(item));
					dijit.byId('esName').set('value',store.getValue(item,'supplierName'));
					dijit.byId('esAddress').set('value',store.getValue(item,'supplierAddress'));
					dijit.byId('esContact').set('value',store.getValue(item,'supplierContact'));
					dijit.byId('esEmail').set('value',store.getValue(item,'supplierEmail'));
					dijit.byId('esWebsite').set('value',store.getValue(item,'supplierWebsite'));
					dijit.byId('esVat').set('value',store.getValue(item,'vatNumber'));
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
					if (confirm('Are you sure, you want to delete selected supplier.')) {
						var bId = pGrid.get('store').getIdentity(pGrid.get('selection').getSelected()[0]);
						xhr.post({
							url:'../json/SupplierManager_delete.action',
							handleAs:'json',
							content:{'supplier.id':bId},
							load: function(data) {
								alert('Supplier deleted successfully');
								populateSupplier();
							},
							error: function() {
								alert('Due to some problem could not process request');
							}
						});
					}
				} else {
					alert('Kindly select row to delete');
				}
			}
		},'deleteButtonDiv');
		var populateSupplier = function() {
			xhr.post({
				url:'../json/SupplierManager_fetchAllSuppliers.action',
				handleAs:'json',
				load: function(data) {
					pGrid.setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'id',items:data.suppliers}}));
				}
			});
		};
		populateSupplier();
	});
})();