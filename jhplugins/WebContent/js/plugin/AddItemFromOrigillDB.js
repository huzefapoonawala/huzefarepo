(function() {
	var itemData = [], storeName = '';	
	function configureEditDialog() {
		dojo.subscribe('jh/iu/edititem', function(rIdx){			
			dojo.forEach(['sku','retailPrice','costPrice',"description", 'deptName', 'categoryName'], function(key) {
				dijit.byId(key).set('value',/*store.getValue(item,key)*/itemData[rIdx][key]);
			});
			dojo.empty('itemImage');
			dojo.attr('itemImage','innerHTML','<img width="90px" height="90px" src="'+/*store.getValue(item,'image')*/itemData[rIdx]['image']+'" alt="No image found">');
			dijit.byId('editDiv').attr({style:{display:''}});
			if (itemData[rIdx]['isItemExist']) {
				dojo.style('addActionPane', 'display', 'none');
				alert('Item already exist in dynamics db');
			}
			else{
				dojo.style('addActionPane', 'display', '');	
			}
		});
		dijit.byId('editDiv').placeAt('bodyDiv');
		dijit.byId('editForm').attr({
			onSubmit: function() {
				var isValid = this.validate();
				if (isValid && confirm('Are you sure, you want to add the searched item.')) {
					dojo.xhrPost({
						url:'../json/Item_copyItemsFromOrgillDB.action',
						content:{skus:[dijit.byId('sku').get('value')]},
						handleAs:'json',
						sync:true,
						load: function(response) {
							alert('Item added successfully');
							dojo.style('addActionPane', 'display', 'none');
						}
					});
				}
				return false;
			},
			onReset: function() {
				dijit.byId('editDiv').attr({style:{display:'none'}});
				this.inherited("onReset", arguments);
			}
		});
	}

	function resetAndFocusSearchSku() {
		var el = dijit.byId('ssku');
		el.reset();
		el.focus();
	}
	
	require(["dojo",
	         "dojo/parser",
	         "dijit/form/ValidationTextBox",
	         "dijit/form/Form",
	         "dojox/layout/TableContainer",
	         "dijit/layout/ContentPane",
	         "dijit/form/Button",
	         "dijit/form/Select",
	         "dijit/form/NumberTextBox",
	         "dijit/form/Textarea",
	         "dijit/Dialog",
	         "dojo/data/ItemFileWriteStore",
	         "dojox/grid/EnhancedGrid",
	         "dijit/TitlePane",
	         "dojo/cookie",
	         "dojo/domReady!"], function() {
		dojo.publish('jh/set/breadcrum',['Add Item From OrgillDB']);
		dojo.parser.parse();
		dijit.byId('searchForm').placeAt('bodyDiv');
		dijit.byId('searchForm').attr({
			style:'visibility:visible;',
			onSubmit: function() {
				var isValid = this.validate();
				if (isValid && (dijit.byId('ssku').get('value').length == 0 /*&& dijit.byId('salias').get('value').length == 0*/)) {
					alert('Enter sku');
					isValid = false;
				}
				if (isValid) {
					dojo.xhrPost({
						url:'../json/Item_fetchItemDetailsBySkuFromOrgillDB.action',
						form:'searchForm',
						handleAs:'json',
						sync:true,
						load: function(response) {
							if (response.item && response.item.sku) {
								itemData = [];
								itemData.push(response.item);
								dojo.publish('jh/iu/edititem',[0]);
							} else {
								alert('No data found for the entered sku');
							}
							resetAndFocusSearchSku();
						}
					});
				}
				return false;
			}
		});
		configureEditDialog();
	});
})();