(function() {
	
	function loadProperties() {
		dojo.xhrPost({
			url:'../json/Common_fetchApplicationProperties.action',
			handleAs:'json',
			form:'saveConfigsForm',
			sync:true,
			load: function(response) {
				if (response.properties) {
					dojo.forEach(response.properties, function(prop,idx) {
						if (dijit.byId('prop'+idx+'_value')) {
							dijit.byId('prop'+idx+'_value').set('value',prop.propertyValue);
						}
					});
				}
			}
		});
		if (dijit.byId('prop0_value').get('value') == '') {
			dojo.style('appPropWarningDiv','display','');
		}
	}
	
	function updateButtonLabel(buttonElId, labelText) {
		var buttonEl = dijit.byId(buttonElId), oldLabelText = buttonEl.get('label');
		buttonEl.set('label',labelText);
		return oldLabelText;
	}
	
	require(["dojo",
	         "dojo/parser",
	         "dijit/form/ValidationTextBox",
	         "dijit/form/Button",
	         "dijit/form/Form",
	         "dojo/data/ItemFileReadStore",
	         "dijit/form/NumberTextBox",
	         "dojo/domReady!"], function() {
		dojo.publish('jh/set/breadcrum',['Dynamics Extension Configurations']);
		dojo.destroy('menuToolbar');
		dojo.parser.parse();
		dijit.byId('saveConfigsForm').placeAt('bodyDiv');
		dijit.byId('saveConfigsForm').attr({
			style:'visibility:visible;',
			onSubmit: function() {
				var isValid = this.validate();
				if (isValid && confirm('Are you sure, you want to update the application properties.')) {
					var oldLabelText = updateButtonLabel('updtPropButton', 'Updating...');
					dojo.xhrPost({
						url:'../json/Common_persistApplicationProperties.action',
						handleAs:'json',
						form:'saveConfigsForm',
						sync:true,
						load: function(response) {
							updateButtonLabel('updtPropButton', oldLabelText);
							if (response.propertiesPersisted) {
								alert('Application properties updated successfully.');
								alert('Please restart the application server for the changes to take effect.');
							} else {
								alert('Due to an error could not save/update properties.');
							}
						},
						error: function() {
							updateButtonLabel('updtPropButton', oldLabelText);
							alert('Due to an error could not update properties.');
						}
					});
				}
				return false;
			}
		});
		dijit.byId('chkDbConnButton').attr({
			onClick: function() {
				var oldLabelText = updateButtonLabel('chkDbConnButton', 'Checking...');
				dojo.xhrPost({
					url:'../json/Common_checkDBConnection.action',
					handleAs:'json',
					form:'saveConfigsForm',
					sync:true,
					load: function(response) {
						updateButtonLabel('chkDbConnButton', oldLabelText);
						if (response.validDbConnection) {
							alert('Database connected successfully.');
						}
						else{
							alert('Could not connect to database. Please check database connection configs.');
						}
					}
				});
			}
		});
		loadProperties();
	});
})();