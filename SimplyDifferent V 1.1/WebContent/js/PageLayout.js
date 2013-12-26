(function () {
	var APP_THEME = 'claro', keyHandler;
	require(['dojo','dojo/dom-construct','dojo/dom-attr','dojo/_base/event','dojo/domReady!'], function(dojo,domConstruct, dattr, eventHandler) {
		dattr.set(dojo.body(),'class',APP_THEME);
		domConstruct.create('div',{id:'menuToolbar'},dojo.body());
		domConstruct.create('div',{id:'breadCrumDiv','class':'breadcrum',innerHTML:'You are here : <span id="subBreadcrumDiv"></span>'},dojo.body());
		domConstruct.create('div',{id:'bodyDiv'},dojo.body());
		var skDiv = domConstruct.create('div',{id:'shortcutKeyDiv'},dojo.body());
		dojo.subscribe('sd/set/breadcrum', function(breadcrum) {
			dattr.set('subBreadcrumDiv','innerHTML',breadcrum);
		});
		dojo.subscribe('sd/toolbar/loaded', function() {
			dojo.publish('sd/event/keypress/description',[skDiv]);
		});
		require(['sd/common/Toolbar']);
		
		function keyListener() {
			keyHandler = dojo.connect(dojo.body(),'onkeypress',function(evt){
				switch (evt.keyCode) {
				/*case dojo.keys.BACKSPACE:
					eventHandler.stop(evt);
					break;*/

				default:
					dojo.publish('sd/event/keypress',[evt]);
					break;
				}
			});
		}
		
		keyListener();
	});
})();