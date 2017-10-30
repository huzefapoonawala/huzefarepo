<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<s:include value="../common/header.html" />
<script type="text/javascript">
	require(['js/plugin/AddItemFromOrigillDB']);
</script>
</head>
<body>
<div id="searchForm" style="width: 100%; visibility: hidden;" data-dojo-type="dijit.form.Form" data-dojo-props="method:'post'">
	<br>
	<table>
		<tr>
			<td>
				Enter SKU To Add&nbsp;&nbsp;
				<div data-dojo-type="dijit.form.ValidationTextBox" id="ssku" data-dojo-props="label:'SKU', name:'sku', required:false"></div>
			</td>
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
		<table id="editTable">
			<tr>
				<td>&nbsp;</td>
				<td colspan="4"><span id="itemImage"></span></td>
			</tr>
			<tr>
				<td>SKU#</td>
				<td colspan="4"><div data-dojo-type="dijit.form.ValidationTextBox" id="sku" data-dojo-props="label:'SKU#', name:'sku', required:true, readonly:true"></div></td>
			</tr>
			<tr>
				<td>Product Description</td>
				<td colspan="4"><div data-dojo-type="dijit.form.Textarea" id="description" data-dojo-props="label:'Product Description', name:'description', required:false, readonly:true, maxLength:'30', cols:22"></div></td>
			</tr>
			<tr>
				<td>Retail Price</td>
				<td colspan="4"><div data-dojo-type="dijit.form.NumberTextBox" id="retailPrice" data-dojo-props="label:'Retail Price', name:'retailPrice', required:true, readonly:true"></div></td>
			</tr>
			<tr>
				<td>Cost Price</td>
				<td colspan="4"><div data-dojo-type="dijit.form.NumberTextBox" id="costPrice" data-dojo-props="label:'Cost Price', name:'costPrice', required:true, readonly:true"></div></td>
			</tr>
			<tr>
				<td>Department</td>
				<td colspan="4"><div data-dojo-type="dijit.form.ValidationTextBox" id="deptName" data-dojo-props="label:'Department', name:'deptName', required:true, readonly:true"></div></td>
			</tr>
			<tr>
				<td>Category</td>
				<td colspan="4"><div data-dojo-type="dijit.form.ValidationTextBox" id="categoryName" data-dojo-props="label:'Category', name:'categoryName', required:true, readonly:true"></div></td>
			</tr>
		</table>
		<table width="100%" id="addActionPane">
			<tr class="dijitDialogPaneActionBar">
				<td colspan="2">
					<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Add Item',type:'submit'"></div>
					<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Cancel',type:'reset'"></div>
				</td>
			</tr>
		</table>
	</div>
</div>
</body>
</html>