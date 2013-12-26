<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<s:include value="../common/header.html" />
<script type="text/javascript">
	require(["sd/master/BrandManager","dojo/domReady!"]);
</script>
</head>
<body>
<div id="brandLayoutDiv" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design: 'headline',design: 'headline',style:{width:'100%',	height:'550px'}">
	<div id="formDiv" data-dojo-type="dijit.form.Form" data-dojo-props="region:'left',splitter: false,style:{	width:'300px'}">
		<table>
			<tr>
				<td>Brand Name</td>
				<td>
					<div dojoType="dijit.form.ValidationTextBox" upperCase="true" placeHolder="Enter Brand Name" id="brandDiv" required="true" name="brand.brandName" style="width: 200px;"></div>
				</td>
			</tr>
			<tr>
				<td>Description</td>
				<td>
					<div dojoType="dijit.form.Textarea" name="brand.description" style="width: 200px;"></div>
				</td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td colspan="2" align="left">
					<button type="submit" dojoType="dijit.form.Button">Save Brand</button>
					&nbsp;
					<button type="reset" dojoType="dijit.form.Button">Reset</button>
				</td>
			</tr>
		</table>
	</div>
</div>
<div data-dojo-type="dijit.Dialog" style="width:300px;" data-dojo-props="title:'Edit Brand'" id="editDialog">
	<div id="editFormDiv" data-dojo-type="dijit.form.Form" data-dojo-props="">
		<input type="hidden" id="editBrandId" name="brand.brandId">
		<table>
			<tr>
				<td>Brand Name</td>
				<td>
					<div dojoType="dijit.form.ValidationTextBox" upperCase="true" placeHolder="Enter Brand Name" id="editBrandName" required="true" name="brand.brandName" style="width: 200px;"></div>
				</td>
			</tr>
			<tr>
				<td>Description</td>
				<td>
					<div dojoType="dijit.form.Textarea" id="editDescription" name="brand.description" style="width: 200px;"></div>
				</td>
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