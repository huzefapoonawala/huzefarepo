<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<s:include value="../common/header.html" />
<script type="text/javascript">
	require(['js/plugin/PurchaseOrder']);
</script>
</head>
<body>
<div id="fpanel" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'Filter Panel'">
<div id="poForm" style="width: 100%" data-dojo-type="dijit.form.Form" data-dojo-props="method:'post'">
	<div id="formTable" data-dojo-type="dojox.layout.TableContainer" data-dojo-props="cols:3,orientation:'vert',showLabels:true">
		<div id="fromDate1" data-dojo-type="dijit.form.DateTextBox" data-dojo-props="label:'From Date 1',constraints:{selector: 'date', datePattern: 'dd-MMM-yyyy', locale: 'en-us'},style:'width:120px;', name:'fromDate1', required:true, value:dojo.date.add(new Date(),'year',-1)"></div>
		<div id="toDate1" data-dojo-type="dijit.form.DateTextBox" data-dojo-props="label:'To Date 1',constraints:{selector: 'date', datePattern: 'dd-MMM-yyyy', locale: 'en-us'},style:'width:120px;', name:'toDate1', required:true, value:dojo.date.add(dojo.date.add(new Date(),'year',-1),'week',2)"></div>
		<div data-dojo-type="dijit.form.FilteringSelect" id="supplierId" data-dojo-props="label:'Supplier',placeHolder:'Enter Supplier Name', maxHeight:'200', style:'width: 200px;', searchAttr:'supplierName', highlightMatch:'any', autoComplete:false, name:'supplierId', required:true"></div>
		<div id="fromDate2" data-dojo-type="dijit.form.DateTextBox" data-dojo-props="label:'From Date 2',constraints:{selector: 'date', datePattern: 'dd-MMM-yyyy', locale: 'en-us'},style:'width:120px;', name:'fromDate2', required:true, value:dojo.date.add(new Date(),'week',-2)"></div>
		<div id="toDate2" data-dojo-type="dijit.form.DateTextBox" data-dojo-props="label:'To Date 2',constraints:{selector: 'date', datePattern: 'dd-MMM-yyyy', locale: 'en-us'},style:'width:120px;', name:'toDate2', required:true, value:dojo.date.add(new Date(),'day',1)"></div>
		<div data-dojo-type="dijit.layout.ContentPane">
		</div>
		<div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="colspan:3" align="right">
			<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Search',type:'submit'"></div>
			<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Reset',type:'reset'"></div>
		</div>
	</div>
</div>
</div>
<div id="gridForm" style="width: 100%;" data-dojo-type="dijit.form.Form" data-dojo-props="method:'post',action:'../download/generatePO.action',target:'_po'">
	<div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="" align="center">
		<div data-dojo-type="dijit.form.CheckBox" data-dojo-props="name:'savePO',value:'true'" id="savePO" title="Save PO Details"></div>
		Check To Save PO Details&nbsp;&nbsp;&nbsp;&nbsp;
		<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Generate Purchase Order',type:'submit'"></div>
		<!-- <div data-dojo-type="dijit.form.Button" id="saveChangesButton" data-dojo-props="label:'Save Changes',type:'button'"></div> -->
	</div>
	<input type="hidden" name="supplierId" id="poSupplierId">
	<input type="hidden" name="supplierName" id="poSupplierName">
	<input type="hidden" id="poData" name="poData">
</div>
<div class="dijitHidden">
<div data-dojo-type="dijit.Dialog" style="width:630px;" data-dojo-props="title:'Edit Item Data'" id="editDialog">
	<div id="editForm" style="width: 100%" data-dojo-type="dijit.form.Form" data-dojo-props="method:'post'">
	<input type="hidden" id="rIdx" value="rIdx">
	<input type="hidden" id="id" name="id">
	<input type="hidden" id="reasonCodeId" name="reasonCodeId">
	<input type="hidden" id="changeQuantity" name="changeQuantity">
	<%-- <div id="editTable" data-dojo-type="dojox.layout.TableContainer" data-dojo-props="cols:1,orientation:'horiz',showLabels:true">
		<div data-dojo-type="dijit.layout.ContentPane"><span id="itemImage"></span></div>
		<div data-dojo-type="dijit.form.ValidationTextBox" id="sku" data-dojo-props="label:'SKU#', name:'sku', required:true, readonly:true"></div>
		<div data-dojo-type="dijit.form.NumberTextBox" id="retailPrice" data-dojo-props="label:'Retail Price', name:'retailPrice', required:true"></div>
		<div data-dojo-type="dijit.form.NumberTextBox" id="costPrice" data-dojo-props="label:'Cost Price', name:'costPrice', required:true"></div>
		<div data-dojo-type="dijit.form.NumberTextBox" id="stockQuantity" data-dojo-props="label:'Quantity In Stock', name:'stockQuantity', required:true"></div>
		<div data-dojo-type="dijit.form.NumberTextBox" id="restockLevel" data-dojo-props="label:'Re-stock Level', name:'restockLevel', required:true"></div>
		<div data-dojo-type="dijit.form.NumberTextBox" id="reorderLevel" data-dojo-props="label:'Re-order Level', name:'reorderLevel', required:true"></div>
		<div data-dojo-type="dijit.form.NumberTextBox" id="orderQuantity" data-dojo-props="label:'Quantity To Order', name:'orderQuantity', required:true"></div>
		<div data-dojo-type="dijit.form.Textarea" id="description" data-dojo-props="label:'Product Description', name:'description', required:false, maxLength:'30'"></div>
		<div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="colspan:1" align="left">
			<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Save Changes',type:'submit'"></div>
			<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Cancel',type:'reset'"></div>
		</div>
	</div> --%>
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
			<td>Quantity On Hand</td>
			<td><div data-dojo-type="dijit.form.NumberTextBox" id="stockQuantity" data-dojo-props="label:'Quantity In Stock', name:'stockQuantity', required:true"></div></td>
			<td width="30px">&nbsp;</td>
			<td>Quantity Committed</td>
			<td><div data-dojo-type="dijit.form.NumberTextBox" id="committedQuantity" data-dojo-props="label:'Quantity Committed', name:'committedQuantity', readonly:true"></div></td>
		</tr>
		<tr>
			<td>Quantity To Order</td>
			<td><div data-dojo-type="dijit.form.NumberTextBox" id="orderQuantity" data-dojo-props="label:'Quantity To Order', name:'orderQuantity', required:true"></div></td>
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
		<tr>
			<td>Aliases&nbsp;<img src="../images/add.gif" title="Add alias" onclick="dijit.byId('addAliasDialog').show()" style="cursor: pointer;"></td>
			<td id="aliasEl"></td>
			<td colspan="3">&nbsp;</td>
		</tr>
		<tr class="dijitDialogPaneActionBar">
			<td colspan="5">
				<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Save Changes',type:'submit'"></div>
				<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Cancel',type:'reset'"></div>
			</td>
		</tr>
	</table>
	</div>
</div>
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
<div data-dojo-type="dijit.Dialog" style="width:350px;" data-dojo-props="title:'Select Reason Code'" id="rcDialog">
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
</div>
</div>
</body>
</html>