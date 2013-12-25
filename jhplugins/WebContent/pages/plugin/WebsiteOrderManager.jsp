<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<s:include value="../common/header.html" />
<script type="text/javascript">
	require(['js/plugin/WebsiteOrderManager']);
</script>
</head>
<body>
<form id="confirmForm" style="width: 100%; visibility: hidden;" data-dojo-type="dijit.form.Form" >
	<br>
	<div data-dojo-type="dijit.form.Button" id="showOrderButton" data-dojo-props="label:'Display Unprocessed Website Orders'"></div>
	<br><br>
	<table width="100%">
		<tr>
			<td>
				Select from where should the selected orders be processed&nbsp;&nbsp;
				<div data-dojo-type="dijit.form.Select" id="ordersToProcessFrom" data-dojo-props="name:'ordersToProcessFrom', options:[{value:'store', label:'Store', selected:true},{value:'orgill',label:'Orgill'}]"></div>
			</td>
		</tr>
	</table>
	<div id="dataGrid"></div>
	<div align="center">
		<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Process Selected Orders',type:'submit'"></div>
		<div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Clear',type:'reset'"></div>
	</div>
</form>
<div class="dijitHidden">
	<div data-dojo-type="dijit.Dialog" style="width:820px; height: 500px;" data-dojo-props="title:'Selected Order Details', align:'center'" id="orderDetailDialog">
		<div id="detailsDataGrid"></div>
	</div>
	<div data-dojo-type="dijit.Dialog" style="width:340px; height: 140px;" data-dojo-props="title:'Update Invoice Number', align:'center'" id="updateInvoiceDialog">
		<form id="updateInoiceForm" style="width: 100%; height: 100%" data-dojo-type="dijit.form.Form" >
			<input type="hidden" id="rIdx">
			<table width="100%">
				<tr>
					<td>Order Id&nbsp;&nbsp;</td>
					<td><div data-dojo-type="dijit.form.ValidationTextBox" id="uinOrderId" data-dojo-props="name:'orderId', readonly:true"></div></td>
				</tr>
				<tr>
					<td>PO Number&nbsp;&nbsp;</td>
					<td><div data-dojo-type="dijit.form.ValidationTextBox" id="uinPoNumber" data-dojo-props="name:'poNumber', required:true"></div></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Update Invoice Number',type:'submit'"></div></td>
				</tr>
			</table>
		</form>
	</div>
</div>
</body>
</html>