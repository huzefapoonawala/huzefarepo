<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<s:include value="../common/header.html" />
<script type="text/javascript">
	require(['js/plugin/ItemUpdater']);
</script>
</head>
<body>
<div id="searchForm" style="width: 100%; visibility: hidden;" data-dojo-type="dijit.form.Form" data-dojo-props="method:'post'">
	<br>
	<table>
		<tr>
			<td>
				Enter SKU/Alias&nbsp;&nbsp;
				<div data-dojo-type="dijit.form.ValidationTextBox" id="ssku" data-dojo-props="label:'SKU', name:'sku', required:false"></div>
			</td>
			<!-- <td width="50px" align="center">OR</td>
			<td>
				Enter Alias&nbsp;&nbsp;
				<div data-dojo-type="dijit.form.ValidationTextBox" id="salias" data-dojo-props="label:'Alias', name:'alias', required:false"></div>
			</td> -->
			<td width="10px">&nbsp;</td>
			<td>
				<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Search',type:'submit'"></div>
				<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Reset',type:'reset'" style="display: none;"></div>
			</td>
		</tr>
	</table>
	<br>
</div>
<div data-dojo-type="dijit.TitlePane" style="width: 630px; display: none;" id="editDiv">
<div id="editForm" style="width: 100%;" data-dojo-type="dijit.form.Form" data-dojo-props="method:'post'">
	<input type="hidden" id="rIdx" value="rIdx">
	<input type="hidden" id="id" name="id">
	<input type="hidden" id="reasonCodeId" name="reasonCodeId">
	<input type="hidden" id="changeQuantity" name="changeQuantity">
	<input type="hidden" id="binLocation" value="">
	<table id="editTable">
		<tr>
			<td>&nbsp;</td>
			<td colspan="4"><span id="itemImage"></span></td>
		</tr>
		<tr>
			<td>SKU#</td>
			<td><div data-dojo-type="dijit.form.ValidationTextBox" id="sku" data-dojo-props="label:'SKU#', name:'sku', required:true, readonly:true"></div></td>
			<td width="30px">&nbsp;</td>
			<td>Product Description</td>
			<td><div data-dojo-type="dijit.form.Textarea" id="description" data-dojo-props="label:'Product Description', name:'description', required:false, maxLength:'30', cols:22"></div></td>
		</tr>
		<tr>
			<td>Retail Price</td>
			<td><div data-dojo-type="dijit.form.NumberTextBox" id="retailPrice" data-dojo-props="label:'Retail Price', name:'retailPrice', required:true"></div></td>
			<td width="30px">&nbsp;</td>
			<td>Cost Price</td>
			<td><div data-dojo-type="dijit.form.NumberTextBox" id="costPrice" data-dojo-props="label:'Cost Price', name:'costPrice', required:true"></div></td>
		</tr>
		<tr>
			<td>Quantity In Stock</td>
			<td><div data-dojo-type="dijit.form.NumberTextBox" id="stockQuantity" onkeyup="dojo.publish('iu/reasoncode/display',[]);" data-dojo-props="label:'Quantity In Stock', name:'stockQuantity', required:true"></div></td>
			<td width="30px">&nbsp;</td>
			<td>Quantity Committed</td>
			<td><div data-dojo-type="dijit.form.NumberTextBox" id="committedQuantity" data-dojo-props="label:'Quantity Committed', name:'committedQuantity', readonly:true"></div></td>
		</tr>
		<tr id="reasonCodeEl" style="display: none;">
			<td>Reason Code</td>
			<td>
				<div data-dojo-type="dijit.form.Select" id="reasonCode" data-dojo-props="required:true, disabled:true"></div>&nbsp;&nbsp;
			</td>
			<td colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td>On Order</td>
			<td><div data-dojo-type="dijit.form.NumberTextBox" id="onOrder" data-dojo-props="name:'onOrder', required:false, readonly:true"></div></td>
			<td width="30px">&nbsp;</td>
			<td>Transfer Out</td>
			<td><div data-dojo-type="dijit.form.NumberTextBox" id="transferOut" data-dojo-props="name:'transferOut', readonly:false, readonly:true"></div></td>
		</tr>
		<tr>
			<td>Re-stock Level</td>
			<td><div data-dojo-type="dijit.form.NumberTextBox" id="restockLevel" data-dojo-props="label:'Re-stock Level', name:'restockLevel', required:true"></div></td>
			<td width="30px">&nbsp;</td>
			<td>Re-order Level</td>
			<td><div data-dojo-type="dijit.form.NumberTextBox" id="reorderLevel" data-dojo-props="label:'Re-order Level', name:'reorderLevel', required:true"></div></td>
		</tr>
		<tr>
			<td>Last Received</td>
			<td><div data-dojo-type="dijit.form.ValidationTextBox" id="lastReceived" data-dojo-props="label:'Last Received', name:'lastReceived', required:false, readonly:true"></div></td>
			<td width="30px">&nbsp;</td>
			<td>Last Sold</td>
			<td><div data-dojo-type="dijit.form.ValidationTextBox" id="lastSold" data-dojo-props="label:'Last Sold', name:'lastSold', required:false, readonly:true"></div></td>
		</tr>
	</table>
	<%-- <div id="editTable" data-dojo-type="dojox.layout.TableContainer" data-dojo-props="cols:1,orientation:'horiz',showLabels:true, labelWidth:110">
		<div data-dojo-type="dijit.layout.ContentPane"><span id="itemImage"></span></div>
		<div data-dojo-type="dijit.form.ValidationTextBox" id="sku" data-dojo-props="label:'SKU#', name:'sku', required:true, readonly:true"></div>
		<div data-dojo-type="dijit.form.Textarea" id="description" data-dojo-props="label:'Product Description', name:'description', required:false, maxLength:'30', cols:32"></div>
		<div data-dojo-type="dijit.form.NumberTextBox" id="retailPrice" data-dojo-props="label:'Retail Price', name:'retailPrice', required:true"></div>
		<div data-dojo-type="dijit.form.NumberTextBox" id="costPrice" data-dojo-props="label:'Cost Price', name:'costPrice', required:true"></div>
		<div data-dojo-type="dijit.form.NumberTextBox" id="stockQuantity" data-dojo-props="label:'Quantity In Stock', name:'stockQuantity', required:true"></div>
		<div data-dojo-type="dijit.form.NumberTextBox" id="committedQuantity" data-dojo-props="label:'Quantity Committed', name:'committedQuantity', readonly:true"></div>
		<div data-dojo-type="dijit.form.NumberTextBox" id="restockLevel" data-dojo-props="label:'Re-stock Level', name:'restockLevel', required:true"></div>
		<div data-dojo-type="dijit.form.NumberTextBox" id="reorderLevel" data-dojo-props="label:'Re-order Level', name:'reorderLevel', required:true"></div>
		<div data-dojo-type="dijit.form.ValidationTextBox" id="lastReceived" data-dojo-props="label:'Last Received', name:'lastReceived', required:false, readonly:true"></div>
		<div data-dojo-type="dijit.form.ValidationTextBox" id="lastSold" data-dojo-props="label:'Last Sold', name:'lastSold', required:false, readonly:true"></div>
	</div> --%>
	<table width="100%">
		<tr>
			<td width="87px">Aliases&nbsp;<img src="../images/add.gif" title="Add alias" onclick="dijit.byId('addAliasDialog').show()" style="cursor: pointer;"></td>
			<td id="aliasEl"></td>
		</tr>
		<tr class="dijitDialogPaneActionBar">
			<td colspan="2">
				<span id="itemupdatedmsg" style="display:none;"><b>Changes saved successfully</b></span>
				<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Save Changes',type:'submit'"></div>
				<div id="printLabelButton" data-dojo-type="dijit.form.Button" data-dojo-props="label:'Print Label',type:'button'"></div>
				<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Cancel',type:'reset'"></div>
			</td>
		</tr>
	</table>
</div>
</div>
<div class="dijitHidden">
<!-- <div data-dojo-type="dijit.Dialog" style="width:400px; height: 575px; overflow: auto;" data-dojo-props="title:'Edit Item Details'" id="editDialog">
	
</div> -->
<div data-dojo-type="dijit.Dialog" style="width:350px;" data-dojo-props="title:'Add Alias'" id="addAliasDialog">
	<div id="addAliasForm" style="width: 100%" data-dojo-type="dijit.form.Form" data-dojo-props="method:'post'">
	<table align="center">
		<tr>
			<td valign="top">New Alias&nbsp;&nbsp;</td>
			<td>
				<div data-dojo-type="dijit.form.ValidationTextBox" id="newAlias" data-dojo-props="name:'alias', required:true"></div>&nbsp;&nbsp;
			</td>
			<td>
				<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Add',type:'submit'"></div>
			</td>
		</tr>
	</table>
	</div>
</div>
<!-- <div data-dojo-type="dijit.Dialog" style="width:350px;" data-dojo-props="title:'Select Reason Code'" id="rcDialog">
	<table align="center">
		<tr>
			<td valign="top">Reason Code&nbsp;&nbsp;</td>
			<td>
				<div data-dojo-type="dijit.form.Select" id="reasonCode" data-dojo-props="required:true"></div>&nbsp;&nbsp;
			</td>
			<td>
				<div data-dojo-type="dijit.form.Button" id="rcButton" data-dojo-props="label:'Ok'"></div>
			</td>
		</tr>
	</table>
</div> -->
<!-- <div data-dojo-type="dijit.Dialog" style="width:320px;" data-dojo-props="title:'Delete Alias'" id="deleteAliasDialog">
	<div id="aliasGrid"></div>
</div> -->
</div>
<form action="../download/generateitemlabel.action" target="_label" id="labelForm" method="post">
	<input type="hidden" id="label_sku" name="item.sku">
	<input type="hidden" id="label_rp" name="item.retailPrice">
	<input type="hidden" id="label_desc" name="item.description">
	<input type="hidden" id="label_bl" name="item.binLocation">
	<input type="hidden" id="label_alias" name="item.aliases">
</form>
</body>
</html>