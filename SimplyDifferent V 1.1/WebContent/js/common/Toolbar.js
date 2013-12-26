(function() {
	var MENU_JSON_URL = '../js/json/ToolbarMenu.json', MENU_DIV_ID = 'menuToolbar';
	require(["dijit/Toolbar",
	         "dijit/form/Button", 
	         "dijit/form/DropDownButton", 
	         "dijit/Menu",
	         "dijit/PopupMenuItem",
	         "dijit/MenuItem",
	         "dojo/dom-construct",
	         "dojo/_base/xhr",
	         "dojo/_base/event",
	         "dojo/domReady!"], function(Toolbar, Button, DropDownButton, Menu, PopupMenuItem, MenuItem, domConstruct, xhr, eventHandler) {
		
		var menuData = [], shortcutMenuItems = {};
		xhr.post({
			url: MENU_JSON_URL,
			sync: true,
			handleAs:'json',
			// The success callback with result from server
			load: function(data) {
				menuData = data;
			},
			// The error handler
			error: function() {
				// Do nothing -- keep old content there
			}
		});      
		var createSubMenu = function(node, breadcrum) {
			var smenu = new Menu({});
			dojo.forEach(node.children,function(snode){
				if (snode.children) {
					smenu.addChild(new PopupMenuItem({label:snode.label,popup:createSubMenu(snode,breadcrum+snode.label+' >> ')}));
				} else {
					var menuObj = dojo.mixin({breadcrum:breadcrum+snode.label},snode);
					smenu.addChild(new MenuItem(dojo.mixin({onClick:dojo.partial(executeClick,menuObj)},snode)));
					checkAndAdd2SKey(menuObj);
				}
			});
			return smenu;
		};
	    
		var executeClick = function(item,evt) {
			if (item.url) {
				var form = domConstruct.create("form", {
					method:'post',action:item.url+''
		        }, dojo.body());
				if (item.additionalParams) {
					for ( var key in item.additionalParams) {
						domConstruct.create("input",{type:'hidden',name:key+'',value:item.additionalParams[key]+''},form);
					}
				}
				domConstruct.create("input",{type:'hidden',name:'breadcrum',value:item.breadcrum},form);
				form.submit();
			} 
		};
		var menuToolbar = new Toolbar({},MENU_DIV_ID);
		dojo.forEach(menuData, function(smData) {
			var sbMenu;
			if (smData.children) {
				sbMenu = new DropDownButton({
					label: smData.label,
					dropDown:createSubMenu(smData,smData.label+' >> ')
				});
			} else {
				var menuObj = dojo.mixin({breadcrum:smData.label},smData);
				sbMenu = new Button({
					label: smData.label,
					onClick:dojo.partial(executeClick,menuObj)
				});
				checkAndAdd2SKey(menuObj);
			}
			menuToolbar.addChild(sbMenu);
		});
		dojo.subscribe('sd/event/keypress', function(evt) {
			if (shortcutMenuItems && evt.charCode == 0 && shortcutMenuItems[evt.keyCode]) {
				eventHandler.stop(evt);
				executeClick(shortcutMenuItems[evt.keyCode], null);
			}
		});
		dojo.subscribe('sd/event/keypress/description', function(el) {
			if (el) {
				var content = ''; 
				for ( var key in shortcutMenuItems) {
					var item = shortcutMenuItems[key];
					content += '<li>'+item.shortcutKey+'&nbsp;->&nbsp;'+item.label+'</li>';
				}
				if (content) {
					content = '<ul>Shortcut Keys'+content+'</ul>';
					dojo.attr(el,'innerHTML',content);
				}
			}
		});
		menuToolbar.startup();
		dojo.publish('sd/toolbar/loaded',[]);
		function checkAndAdd2SKey(item) {
			if(item.shortcutKey && dojo.keys[item.shortcutKey]){
				shortcutMenuItems[dojo.keys[item.shortcutKey]] = item;
			}
		}
	});
})();