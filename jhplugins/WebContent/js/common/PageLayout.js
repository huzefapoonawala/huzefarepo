(function () {
	var APP_THEME = 'claro', keyHandler;
	require(['dojo','dojo/dom-construct','dojo/dom-attr','dojo/_base/event','dojo/domReady!'], function(dojo,domConstruct, dattr, eventHandler) {
		dattr.set(dojo.body(),'class',APP_THEME);
		domConstruct.create('div',{id:'menuToolbar'},dojo.body());
		domConstruct.create('div',{id:'breadCrumDiv','class':'breadcrum',innerHTML:'You are here : <span id="subBreadcrumDiv"></span>'},dojo.body());
		domConstruct.create('div',{id:'bodyDiv'},dojo.body());
		dojo.subscribe('jh/set/breadcrum', function(breadcrum) {
			dattr.set('subBreadcrumDiv','innerHTML',breadcrum);
		});
		require(['js/common/Toolbar']);
	});
})();