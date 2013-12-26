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
		dojo.publish('sd/set/breadcrum',['Master >> Customer Manager']);
		parser.parse();
		dijit.byId('formDiv').attr({
			onSubmit: function() {
				var isValid = this.validate();
				if (isValid && confirm('Are you sure, you want to save the new customer.')) {
					xhr.post({
						url:'../json/CustomerManager_save.action',
						handleAs:'json',
						form : 'formDiv',
						sync:true,
						load: function(data) {
							alert('Customer saved successfully');
							dijit.byId('formDiv').reset();
							populateCustomer();
						},
						error: function() {
							alert('Due to some problem could not process request');
						}
					});
				}
				return false;
			}
		}, "formDiv");		
		var bLayout = dijit.byId('customerLayoutDiv'),
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
			id:'customerGrid',
			region: "center",
			structure:[[{name:'Customer Name', field:'name', width:'15%'},
			           {name:'Address', field:'address', width:'25%'},
			           {name:'Contact Person', field:'contactPerson', width:'16%'},
			           {name:'Contact Number', field:'contactNumber', width:'12%'},
			           {name:'Email', field:'email', width:'15%'},
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
				if (isValid && confirm('Are you sure, you want to save changes for the selected customer.')) {
					xhr.post({
						url:'../json/CustomerManager_edit.action',
						handleAs:'json',
						form : 'editFormDiv',
						sync:true,
						load: function(data) {
							alert('Changes saved successfully');
							populateCustomer();
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
					dojo.attr('eCId','value',store.getIdentity(item));
					dijit.byId('eName').set('value',store.getValue(item,'name'));
					dijit.byId('eAddress').set('value',store.getValue(item,'address'));
					dijit.byId('eContact').set('value',store.getValue(item,'contactNumber'));
					dijit.byId('eEmail').set('value',store.getValue(item,'email'));
					dijit.byId('ecPerson').set('value',store.getValue(item,'contactPerson'));
					dijit.byId('eVat').set('value',store.getValue(item,'vatNumber'));
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
					if (confirm('Are you sure, you want to delete selected customer.')) {
						var bId = pGrid.get('store').getIdentity(pGrid.get('selection').getSelected()[0]);
						xhr.post({
							url:'../json/CustomerManager_delete.action',
							handleAs:'json',
							content:{'customer.id':bId},
							load: function(data) {
								alert('Customer deleted successfully');
								populateCustomer();
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
		var populateCustomer = function() {
			xhr.post({
				url:'../json/CustomerManager_fetchAllCustomers.action',
				handleAs:'json',
				load: function(data) {
					pGrid.setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'id',items:data.customers}}));
				}
			});
		};
		populateCustomer();
	});
})();