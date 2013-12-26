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
		dojo.publish('sd/set/breadcrum',['Master >> Brand Manager']);
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
				if (isValid && confirm('Are you sure, you want to save the new brand.')) {
					xhr.post({
						url:'../json/BrandManager_save.action',
						handleAs:'json',
						form : 'formDiv',
						sync:true,
						load: function(data) {
							alert('Brand saved successfully');
							dijit.byId('formDiv').reset();
							populateBrands();
						},
						error: function() {
							alert('Due to some problem could not process request');
						}
					});
				}
				return false;
			}
		}, "formDiv");		
		var bLayout = dijit.byId('brandLayoutDiv'),
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
			id:'brandsGrid',
			region: "center",
			structure:[[{name:'Brand Name', field:'brandName', width:'35%'},
			           {name:'Description', field:'description', width:'65%'}
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
				if (isValid && confirm('Are you sure, you want to save changes for the selected brand.')) {
					xhr.post({
						url:'../json/BrandManager_edit.action',
						handleAs:'json',
						form : 'editFormDiv',
						sync:true,
						load: function(data) {
							alert('Brand saved successfully');
							populateBrands();
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
					dijit.byId('editBrandName').set('value',store.getValue(item,'brandName'));
					dojo.attr('editBrandId','value',store.getValue(item,'brandId'));
					dijit.byId('editDescription').set('value',store.getValue(item,'description'));
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
					if (confirm('Are you sure, you want to delete selected brand.')) {
						var bId = pGrid.get('store').getIdentity(pGrid.get('selection').getSelected()[0]);
						xhr.post({
							url:'../json/BrandManager_delete.action',
							handleAs:'json',
							content:{'brand.brandId':bId},
							load: function(data) {
								alert('Brand deleted successfully');
								populateBrands();
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
		var populateBrands = function() {
			xhr.post({
				url:'../json/BrandManager_fetchBrandList.action',
				handleAs:'json',
				load: function(data) {
					pGrid.setStore(new dojo.data.ItemFileWriteStore({data:{identifier:'brandId',items:data.list}}));
				}
			});
		};
		populateBrands();
	});
})();