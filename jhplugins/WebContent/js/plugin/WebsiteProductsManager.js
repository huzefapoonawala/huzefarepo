(function() {
	
	function createItemDataGrid() {
		var struct = [{
            type: "dojox.grid._CheckBoxSelector"
        },{
			cells: [
				[
				 {name:'Image', field:'image', width:'75px', align:'center',
					 formatter: function(value) { 
						 var src = value; 
						 return "<img src=\"" + src + "\" width=\"70px\" height=\"70px\" />"; 
					 } 
				 },
				 {name: "SKU #", field: "sku", width:'130px'},
//				 {name: "Department", field: "deptName", width:'200px'},
//				 {name: "Category", field: "categoryName", width:'200px'},
				 {name: "Description", field: "description", width:'300px'}
				]
			]
		}];
		var gridStore = new dojo.data.ItemFileWriteStore({data:{identifier:'sku',items:[]}});
		var grid = new dojox.grid.EnhancedGrid({
			structure:struct,
			selectionMode:'multiple',
			style:{height:'450px', width:'99%'},
			store:gridStore,
			keepSelection:true,
			noDataMessage:''
		},'dataGrid');
		grid.startup();
	}
	
	function createCateDataGrid() {
		var struct = [{
            type: "dojox.grid._CheckBoxSelector"
        },{
			cells: [
				[
				 {name: "Department", field: "deptName", width:'400px'},
				 {name: "Category", field: "categoryName", width:'400px'}
				]
			]
		}];
		var gridStore = new dojo.data.ItemFileWriteStore({data:{identifier:'categoryCode',items:[]}});
		var grid = new dojox.grid.EnhancedGrid({
			structure:struct,
			selectionMode:'multiple',
			style:{height:'450px', width:'99%'},
			store:gridStore,
			keepSelection:true,
			noDataMessage:''
		},'cateGrid');
		grid.startup();
	}
	
	function createDeptDataGrid() {
		var struct = [{
            type: "dojox.grid._CheckBoxSelector"
        },{
			cells: [
				[
				 {name: "Department", field: "deptName", width:'600px'}
				]
			]
		}];
		var gridStore = new dojo.data.ItemFileWriteStore({data:{identifier:'deptCode',items:[]}});
		var grid = new dojox.grid.EnhancedGrid({
			structure:struct,
			selectionMode:'multiple',
			style:{height:'450px', width:'99%'},
			store:gridStore,
			keepSelection:true,
			noDataMessage:''
		},'deptGrid');
		grid.startup();
	}
	
	require(["dojo",
	         "dojo/parser",
	         "dijit/form/Button",
	         "dijit/form/Form",
	         "dojo/data/ItemFileWriteStore",
	         "dojox/grid/EnhancedGrid",
	         "dijit/form/CheckBox",
	         "dijit/form/ValidationTextBox",
	         "dojox/grid/EnhancedGrid",
	         "dojox/grid/_CheckBoxSelector",
	         "dojo/domReady!"], function() {
		dojo.publish('jh/set/breadcrum',['Website Product Sync']);
		dojo.parser.parse();
		dijit.byId('uploadForm').placeAt('bodyDiv');
		dijit.byId('uploadForm').attr({
			style:'visibility:visible;',
			onSubmit: function() {
				if (!confirm('Are you sure, you want to sync items from orgill FTP.')) {
					return false;
				}
				dojo.style('WaitingDownloadFtpMessage','display','');
				dojo.style('CompleteDownloadFtpMessage','display','none');
				dojo.xhrPost({
					url:'../json/WebsiteProductsManager_downloadAndPersistOrgillFilesFromFTP.action',
					form:'uploadForm',
					handleAs:'json',
					load: function(response) {
						dojo.style('WaitingDownloadFtpMessage','display','none');
						dojo.style('CompleteDownloadFtpMessage','display','');
					},
					error: function() {
						alert('Due to some error could not complete process');
						dojo.style('WaitingDownloadFtpMessage','display','none');
						dojo.style('CompleteDownloadFtpMessage','display','none');
					}
				});
				return false;
			}
		});
		dijit.byId('manageForm').placeAt('bodyDiv');
		dijit.byId('manageForm').attr({
			style:{display:''}
		});
		createItemDataGrid();
		dijit.byId('manageCateForm').placeAt('bodyDiv');
		dijit.byId('manageCateForm').attr({
			style:{display:''}
		});
		createCateDataGrid();
		dijit.byId('manageDeptForm').placeAt('bodyDiv');
		dijit.byId('manageDeptForm').attr({
			style:{display:''}
		});
		createDeptDataGrid();
		hideAllGrids();
		dijit.byId('dCateButton').attr({
			onClick: function() {
				if (!confirm('Are you sure, you want to sync categories from orgill FTP.')) {
					return false;
				}
				dojo.style('WaitingDownloadCategoryMessage','display','');
				dojo.style('CompleteDownloadCategoryMessage','display','none');
				dojo.xhrPost({
					url:'../json/WebsiteProductsManager_downloadAndPersistOrgillCategoriesFromFTP.action',
					form:'uploadForm',
					handleAs:'json',
					load: function(response) {
						dojo.style('WaitingDownloadCategoryMessage','display','none');
						dojo.style('CompleteDownloadCategoryMessage','display','');
					},
					error: function() {
						alert('Due to some error could not complete process');
						dojo.style('WaitingDownloadCategoryMessage','display','none');
						dojo.style('CompleteDownloadCategoryMessage','display','none');
					}
				});
				return false;
			}
		});
		dijit.byId('deleteItemsButton').attr({
			onClick: dojo.partial(manageItemSync,'../json/WebsiteProductsManager_deactivateWebsiteItems.action',
					'Kindly select atleast one item to deactivate',
					'Are you sure, you want to deactivate selected items.',
					'Items deactivated successfully.',
					dojo.partial(manageItemGrid,'../json/WebsiteProductsManager_fetchWebsiteItemsToDeleteAfterSync.action',manageItemsForDeleteButton), false) 
		});
		dijit.byId('addItemsButton').attr({
			onClick: dojo.partial(manageItemSync,'../json/Item_addOrActivateItems.action',
									'Kindly select atleast one item to add',
									'Are you sure, you want to add selected items.',
									'Items added successfully.',
									dojo.partial(manageItemGrid,'../json/WebsiteProductsManager_fetchItemsToAddAfterSync.action',manageItemsForAddButton), true)
		});
		dijit.byId('activateItemsButton').attr({
			onClick: dojo.partial(manageItemSync,'../json/WebsiteProductsManager_activateWebsiteItems.action',
									'Kindly select atleast one item to activate',
									'Are you sure, you want to activate selected items.',
									'Items activated successfully.',
									dojo.partial(manageItemGrid,'../json/WebsiteProductsManager_fetchWebsiteItemsToActivateAfterSync.action',manageItemsForActivateButton), false)
		});
		
		function manageItemSync(updateUrl, alertMsg, confirmMsg, successMsg, refreshFunc, isImagePathReq) {
			var isValid = true, products = [], grid = dijit.byId('dataGrid'), store = grid.get('store');
			if (isImagePathReq && dijit.byId('imagePath').get('value').length == 0) {
				alert('Please enter new images path');
				isValid = false;
			}
			if (isValid) {
				products = grid.get('selection').getSelected();
				if (products.length == 0) {
					alert(alertMsg);
					isValid = false;
				}
			}
			if (isValid && confirm(confirmMsg)) {
				var arr = [];
				try {
					if (grid.allItemsSelected) {
						store.fetch({
							onComplete:function(items,req){
								products = items;
							}
						});
					} 
					dojo.forEach(products, function(item) {
						arr.push(store.getIdentity(item));
					});
					if (isImagePathReq) {
						dojo.attr('AddingLoadingItemsMessageDiv',{style:{display:''}});
					}
					dojo.xhrPost({
						url:updateUrl,
						content:{itemsToModify:dojo.toJson(arr), imagePath:dijit.byId('imagePath').get('value')},
						handleAs:'json',
						load: function(response) {
							dojo.attr('AddingLoadingItemsMessageDiv',{style:{display:'none'}});
							alert(successMsg);
							dijit.byId('dataGrid').selection.deselectAll();
							refreshFunc();
						}
					});
				} catch (e) {
					console.log(e);
				}
			}
			return false;
		}
		dijit.byId('item2DeleteButton').attr({
			onClick: dojo.partial(manageItemGrid,'../json/WebsiteProductsManager_fetchWebsiteItemsToDeleteAfterSync.action',manageItemsForDeleteButton)
		});
		dijit.byId('item2ActivateButton').attr({
			onClick: dojo.partial(manageItemGrid,'../json/WebsiteProductsManager_fetchWebsiteItemsToActivateAfterSync.action',manageItemsForActivateButton)
		});
		dijit.byId('item2AddButton').attr({
			onClick: dojo.partial(manageItemGrid,'../json/WebsiteProductsManager_fetchItemsToAddAfterSync.action',manageItemsForAddButton)
		});
		function manageItemGrid(url, manageButtonFunc) {
			hideAllGrids();
			dijit.byId('manageForm').attr({
				style:{display:''}
			});
			dijit.byId('dataGrid')._clearData();
			dijit.byId('dataGrid').attr({noDataMessage:'Loading data...'});
			var store = new dojo.data.ItemFileWriteStore({data:{identifier:'sku',items:[]}});
			dijit.byId('dataGrid').setStore(store);
			manageButtonFunc();
			dojo.xhrPost({
				url:url,
				handleAs:'json',
				load: function(res) {
					dijit.byId('dataGrid').attr({noDataMessage:'No data found'});
					store = new dojo.data.ItemFileWriteStore({data:{identifier:'sku',items:res.products}});
					dijit.byId('dataGrid').setStore(store);
				}
			});
		}
		function manageItemsForDeleteButton() {
			dijit.byId('deleteItemsButton').attr({style:{display:''}});
			dijit.byId('addItemsButton').attr({style:{display:'none'}});
			dojo.attr('newImageDiv',{style:{display:'none'}});
			dijit.byId('activateItemsButton').attr({style:{display:'none'}});
		}
		function manageItemsForAddButton() {
			dijit.byId('deleteItemsButton').attr({style:{display:'none'}});
			dijit.byId('addItemsButton').attr({style:{display:''}});
			dojo.attr('newImageDiv',{style:{display:''}});
			dijit.byId('activateItemsButton').attr({style:{display:'none'}});
		}
		function manageItemsForActivateButton() {
			dijit.byId('deleteItemsButton').attr({style:{display:'none'}});
			dijit.byId('addItemsButton').attr({style:{display:'none'}});
			dijit.byId('activateItemsButton').attr({style:{display:''}});
			dojo.attr('newImageDiv',{style:{display:'none'}});
		}
		function hideAllGrids() {
			dijit.byId('manageForm').attr({
				style:{display:'none'}
			});
			dijit.byId('manageCateForm').attr({
				style:{display:'none'}
			});
			dijit.byId('manageDeptForm').attr({
				style:{display:'none'}
			});
		}
		
		dijit.byId('showDept2AddButton').attr({
			onClick: dojo.partial(manageCateGrid,'../json/WebsiteProductsManager_fetchDeptToAddAfterSync.action','manageDeptForm','deptGrid','deptCode',manageDeptForAddButton)
		});
		dijit.byId('showCate2AddButton').attr({
			onClick: dojo.partial(manageCateGrid,'../json/WebsiteProductsManager_fetchCategoryToAddAfterSync.action','manageCateForm','cateGrid','categoryCode',manageCateForAddButton)
		});
		function manageCateGrid(url,divName,gridName,idField,manageButtonFunc) {
			hideAllGrids();
			dijit.byId(divName).attr({
				style:{display:''}
			});
			dijit.byId(gridName).attr({noDataMessage:'Loading data...'});
			dijit.byId(gridName).setStore(new dojo.data.ItemFileWriteStore({data:{identifier:idField,items:[]}}));
			manageButtonFunc();
			dojo.xhrPost({
				url:url,
				handleAs:'json',
				sync:true,
				load: function(res) {
					dijit.byId(gridName).attr({noDataMessage:'No data found'});
					dijit.byId(gridName).setStore(new dojo.data.ItemFileWriteStore({data:{identifier:idField,items:res.products}}));
				}
			});
		}
		function manageDeptForAddButton() {
			dijit.byId('addDeptButton').attr({style:{display:''}});
			dijit.byId('addCateButton').attr({style:{display:'none'}});
		}
		function manageCateForAddButton() {
			dijit.byId('addDeptButton').attr({style:{display:'none'}});
			dijit.byId('addCateButton').attr({style:{display:''}});
		}
		
		dijit.byId('addDeptButton').attr({
			onClick: dojo.partial(manageDeptCateSync,'../json/Item_addDepartments.action','deptGrid',
					'Kindly select atleast one department to add',
					'Are you sure, you want to add selected departments.',
					'Departments added successfully.',
					dojo.partial(manageCateGrid,'../json/WebsiteProductsManager_fetchDeptToAddAfterSync.action','manageDeptForm','deptGrid','deptCode',manageDeptForAddButton)) 
		});
		dijit.byId('addCateButton').attr({
			onClick: dojo.partial(manageDeptCateSync,'../json/Item_addCategories.action','cateGrid',
					'Kindly select atleast one category to add',
					'Are you sure, you want to add selected categories.',
					'Categories added successfully.',
					dojo.partial(manageCateGrid,'../json/WebsiteProductsManager_fetchCategoryToAddAfterSync.action','manageCateForm','cateGrid','categoryCode',manageCateForAddButton)) 
		});
		function manageDeptCateSync(updateUrl, gridName, alertMsg, confirmMsg, successMsg, refreshFunc) {
			var isValid = true, products = [], grid = dijit.byId(gridName), store = grid.get('store');
			if (isValid) {
				products = grid.get('selection').getSelected();
				if (products.length == 0) {
					alert(alertMsg);
					isValid = false;
				}
			}
			if (isValid && confirm(confirmMsg)) {
				var arr = [];
				try {
					if (grid.allItemsSelected) {
						store.fetch({
							onComplete:function(items,req){
								products = items;
							}
						});
					} 
					dojo.forEach(products, function(item) {
						var dJson = {};
						dojo.forEach(store.getAttributes(item), function(attr) {
							dJson[attr] = store.getValue(item,attr);
						});
						arr.push(dJson);
					});
					dojo.xhrPost({
						url:updateUrl,
						content:{itemsToModify:dojo.toJson(arr)},
						handleAs:'json',
						load: function(response) {
							alert(successMsg);
							refreshFunc();
						}
					});
				} catch (e) {
					console.log(e);
				}
			}
			return false;
		}
	});
})();