<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<s:include value="../common/header.html" />
<script type="text/javascript">
	require(["sd/master/SupplierManager","dojo/domReady!"]);
</script>
</head>
<body>
<div id="supplierLayoutDiv" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design: 'headline',design: 'headline',style:{width:'100%',	height:'550px'}">
	<div id="formDiv" data-dojo-type="dijit.form.Form" data-dojo-props="region:'left',splitter: false,style:{	width:'300px'}">
		<table>
			<tr>
				<td>Supplier Name</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="sName"	upperCase="true" required="true" name="supplier.supplierName"></div></td>
			</tr>
			<tr>
				<td>Supplier Address</td>
				<td><div dojoType="dijit.form.Textarea" id="sAddress" upperCase="true" name="supplier.supplierAddress"></div></td>
			</tr>
			<tr>
				<td>Supplier Contact Number</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="sContact" upperCase="true" name="supplier.supplierContact"></div></td>
			</tr>
			<tr>
				<td>Supplier Email</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="sEmail" name="supplier.supplierEmail"></div></td>
			</tr>
			<tr>
				<td>Supplier Website</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="sWebsite" name="supplier.supplierWebsite"></div></td>
			</tr>
			<tr>
				<td>VAT Number</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="sVat" upperCase="true" name="supplier.vatNumber"></div></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td colspan="2" align="left">
					<button type="submit" dojoType="dijit.form.Button">Save Supplier</button>
					&nbsp;
					<button type="reset" dojoType="dijit.form.Button">Reset</button>
				</td>
			</tr>
		</table>
	</div>
</div>
<div data-dojo-type="dijit.Dialog" style="width:300px;" data-dojo-props="title:'Edit Details'" id="editDialog">
	<div id="editFormDiv" data-dojo-type="dijit.form.Form" data-dojo-props="">
		<input type="hidden" id="editSupplierId" name="supplier.id">
		<table>
			<tr>
				<td>Supplier Name</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="esName" upperCase="true" required="true" name="supplier.supplierName"></div></td>
			</tr>
			<tr>
				<td>Supplier Address</td>
				<td><div dojoType="dijit.form.Textarea" id="esAddress" upperCase="true" name="supplier.supplierAddress"></div></td>
			</tr>
			<tr>
				<td>Supplier Contact Number</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="esContact" upperCase="true" name="supplier.supplierContact"></div></td>
			</tr>
			<tr>
				<td>Supplier Email</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="esEmail" name="supplier.supplierEmail"></div></td>
			</tr>
			<tr>
				<td>Supplier Website</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="esWebsite" name="supplier.supplierWebsite"></div></td>
			</tr>
			<tr>
				<td>VAT Number</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="esVat" upperCase="true" name="supplier.vatNumber"></div></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td colspan="2" align="center">
					<button type="submit" dojoType="dijit.form.Button">Save Changes</button>
				</td>
			</tr>
		</table>
	</div>
</div>
</body>
</html>