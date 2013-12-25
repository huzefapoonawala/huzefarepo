<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<s:include value="../common/header.html" />
<script type="text/javascript">
	require(['js/plugin/InvoiceUploader']);
</script>
</head>
<body>
<form id="uploadForm" style="width: 100%; visibility: hidden;" data-dojo-type="dijit.form.Form" method="post" enctype="multipart/form-data">
	<br>
	<table>
		<tr>
			<td>
				Select File To Upload&nbsp;&nbsp;
				<input type="file" id="invoiceFile" name="invoiceFile" >
			</td>
			<td width="10px">&nbsp;</td>
			<td>
				<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Show Invoices',type:'submit'"></div>			
				<!-- <div data-dojo-type="dijit.form.Button" id="uploadFormReset" data-dojo-props="label:'Reset',type:'reset'" ></div> -->
			</td>
		</tr>
		<tr>
			<td colspan="3">
				OR<br>
				<div data-dojo-type="dijit.form.Button" id="ftpButton" data-dojo-props="label:'Browse Files From FTP'"></div>
			</td>
		</tr>
	</table>
	<br>
</form>
<form id="saveForm" style="width: 100%; visibility: hidden;" data-dojo-type="dijit.form.Form" >
	<br>
	<table width="100%">
		<tr>
			<td>
				Enter Supplier&nbsp;&nbsp;
				<div data-dojo-type="dijit.form.FilteringSelect" id="supplierId" data-dojo-props="placeHolder:'Enter Supplier Name', maxHeight:'200', style:'width: 200px;', searchAttr:'supplierName', highlightMatch:'any', autoComplete:false, name:'supplierId', required:true"></div>
			</td>
			<!-- <td width="10px" align="center">&nbsp;</td> -->
			<td>
				Enter Requisitioner&nbsp;&nbsp;
				<div data-dojo-type="dijit.form.ValidationTextBox" id="requisitioner" data-dojo-props="placeHolder:'Enter Requisitioner Name', style:'width: 200px;', name:'requisitioner', required:true"></div>
			</td>
			<!-- <td width="10px" align="center">&nbsp;</td> -->
			<td>
				Enter Cost Price Multiplier&nbsp;&nbsp;
				<div data-dojo-type="dijit.form.NumberTextBox" id="multiplier" data-dojo-props="placeHolder:'', style:'width: 60px;', name:'multiplier', required:false, value:6"></div>%
			</td>
			<!-- <td width="10px">&nbsp;</td> -->
		</tr>
	</table>
	<div id="dataGrid"></div>
	<div align="center">
		<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Save Selected Invoices',type:'submit'"></div>
		<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Clear',type:'reset'"></div>
	</div>
</form>
<div class="dijitHidden">
<div data-dojo-type="dijit.Dialog" style="width:820px; height: 500px;" data-dojo-props="title:'Selected Invoice Details', align:'center'" id="invoiceDetailDialog">
	<div align="right" id="warningEl" style="display: none;"><a href="javascript:void(0);" onclick="dojo.publish('jh/pi/skusna',[])" style="text-align: right; color: red; font-size: 14px;">SEE SKU WARNING!!!</a></div>
	<div align="right" id="aliasWarningEl" style="display: none;"><a href="javascript:void(0);" onclick="dojo.publish('jh/pi/aliasna',[])" style="text-align: right; color: red; font-size: 14px;">SEE ALIAS WARNING!!!</a></div>
	<div align="left" style="padding-left: 10px; padding-bottom: 5px;">
		<div data-dojo-type="dijit.form.CheckBox" data-dojo-props="title:'Select/Deselect All Update Retail Price Checkboxes', checked:true" id="updateAllCb"></div>
	</div>
	<div id="detailsDataGrid"></div>
	<div style="font-size: 10px;" align="left">&nbsp;Note: 'Double click' on the row of an item to edit its details.</div>
</div>
<div data-dojo-type="dijit.Dialog" style="width:630px;" data-dojo-props="title:'Edit Item Data'" id="editDialog">
	<div id="editForm" style="width: 100%" data-dojo-type="dijit.form.Form" data-dojo-props="method:'post'">
	<input type="hidden" id="rIdx" value="rIdx">
	<input type="hidden" id="id" name="id">
	<input type="hidden" id="reasonCodeId" name="reasonCodeId">
	<input type="hidden" id="changeQuantity" name="changeQuantity">
	<%-- <div id="editTable" data-dojo-type="dojox.layout.TableContainer" data-dojo-props="cols:1,orientation:'horiz',showLabels:true">
		<div data-dojo-type="dijit.layout.ContentPane"><span id="itemImage"></span></div>
		<div data-dojo-type="dijit.form.ValidationTextBox" id="lineNumber" data-dojo-props="label:'Line Number', name:'lineNumber', required:false, readonly:true"></div>
		<div data-dojo-type="dijit.form.ValidationTextBox" id="sku" data-dojo-props="label:'SKU', name:'sku', required:false, readonly:true"></div>
		<div data-dojo-type="dijit.form.NumberTextBox" id="retailPrice" data-dojo-props="label:'Retail Price', name:'retailPrice', required:true"></div>
		<div data-dojo-type="dijit.form.NumberTextBox" id="costPrice" data-dojo-props="label:'Cost Price', name:'costPrice', required:true"></div>
		<div data-dojo-type="dijit.form.NumberTextBox" id="quantity" data-dojo-props="label:'Quantity', name:'quantity', required:true"></div>
		<div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="colspan:1" align="left">
			<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Update',type:'submit'"></div>
			<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Cancel',type:'reset'"></div>
		</div>
	</div> --%>
	<table id="editTable">
		<tr>
			<td>&nbsp;</td>
			<td colspan="4"><span id="itemImage"></span></td>
		</tr>
		<tr>
			<td>Line Number</td>
			<td><div data-dojo-type="dijit.form.ValidationTextBox" id="lineNumber" data-dojo-props="label:'Line Number', name:'lineNumber', required:false, readonly:true"></div></td>
			<td colspan="3">&nbsp;</td>
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
			<td>Quantity Received</td>
			<td><div data-dojo-type="dijit.form.NumberTextBox" id="quantity" data-dojo-props="label:'Quantity', name:'quantity', required:true"></div></td>
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
			<td>Aliases&nbsp;<img src="../images/add.gif" id="addAliasIcon" title="Add alias" onclick="dijit.byId('addAliasDialog').show()" style="cursor: pointer;"></td>
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
<div data-dojo-type="dijit.Dialog" style="width:200px;" data-dojo-props="title:'Skus Not Existing'" id="skusnaDialog">
	<div id="skusnaDataGrid"></div>
</div>
<div data-dojo-type="dijit.Dialog" style="width:350px;" data-dojo-props="title:'Aliases Not Existing'" id="aliasnaDialog">
	<div id="aliasesnaDataGrid"></div>
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
<div data-dojo-type="dijit.Dialog" style="width:600px;" data-dojo-props="title:'Invoices From FTP'" id="ftpDialog">
	<div id="ftpDataGrid"></div>
	<div class="dijitDialogPaneActionBar">
		<div data-dojo-type="dijit.form.Button" id="showFtpButton" data-dojo-props="label:'Show Invoices From Selected File'"></div>
	</div>
</div>
</div>
</body>
</html>