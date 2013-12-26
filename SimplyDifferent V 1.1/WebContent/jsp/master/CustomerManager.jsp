<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<s:include value="../common/header.html" />
<script type="text/javascript">
	require(["sd/master/CustomerManager","dojo/domReady!"]);
</script>
</head>
<body>
<div id="customerLayoutDiv" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design: 'headline',design: 'headline',style:{width:'100%',	height:'550px'}">
	<div id="formDiv" data-dojo-type="dijit.form.Form" data-dojo-props="region:'left',splitter: false,style:{	width:'300px'}">
		<table>
			<tr>
				<td>Customer Name</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="cName"	upperCase="true" required="true" name="customer.name"></div></td>
			</tr>
			<tr>
				<td>Address</td>
				<td><div dojoType="dijit.form.Textarea" id="cAddress" upperCase="true" name="customer.address"></div></td>
			</tr>
			<tr>
				<td>Contact Person</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="cPerson" upperCase="true" name="customer.contactPerson"></div></td>
			</tr>
			<tr>
				<td>Contact Number</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="cContact" upperCase="true" name="customer.contactNumber"></div></td>
			</tr>
			<tr>
				<td>Email</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="cEmail" name="customer.email"></div></td>
			</tr>
			<tr>
				<td>VAT Number</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="cVat" upperCase="true" name="customer.vatNumber"></div></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td colspan="2" align="left">
					<button type="submit" dojoType="dijit.form.Button">Save Customer</button>
					&nbsp;
					<button type="reset" dojoType="dijit.form.Button">Reset</button>
				</td>
			</tr>
		</table>
	</div>
</div>
<div data-dojo-type="dijit.Dialog" style="width:300px;" data-dojo-props="title:'Edit Details'" id="editDialog">
	<div id="editFormDiv" data-dojo-type="dijit.form.Form" data-dojo-props="">
		<input type="hidden" id="eCId" name="customer.id">
		<table>
			<tr>
				<td>Customer Name</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="eName"	upperCase="true" required="true" name="customer.name"></div></td>
			</tr>
			<tr>
				<td>Address</td>
				<td><div dojoType="dijit.form.Textarea" id="eAddress" upperCase="true" name="customer.address"></div></td>
			</tr>
			<tr>
				<td>Contact Person</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="ecPerson" upperCase="true" name="customer.contactPerson"></div></td>
			</tr>
			<tr>
				<td>Contact Number</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="eContact" upperCase="true" name="customer.contactNumber"></div></td>
			</tr>
			<tr>
				<td>Email</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="eEmail" name="customer.email"></div></td>
			</tr>
			<tr>
				<td>VAT Number</td>
				<td><div dojoType="dijit.form.ValidationTextBox" id="eVat" upperCase="true" name="customer.vatNumber"></div></td>
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