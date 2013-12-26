<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<s:include value="../common/header.html" />
<script type="text/javascript">
	require(["sd/master/ProductManager","dojo/domReady!"]);
</script>
</head>
<body>
<div id="formDiv" style="display: none;">
<table>
	<tr>
		<td>Brand Name</td>
		<td>
			<div dojoType="dijit.form.FilteringSelect" placeHolder="Enter Brand Name" id="brandDiv" searchAttr="brandName" required="true" name="product.brandId" style="width: 200px;" maxHeight="200"></div>
		</td>
	</tr>
	<tr>
		<td>Product Name</td>
		<td>
			<div dojoType="dijit.form.ValidationTextBox" upperCase="true" required="true" name="product.productName" style="width: 200px;"></div>
		</td>
	</tr>
	<tr>
		<td>Purchase Price (Per Kg/Ltr)</td>
		<td>
			<div dojoType="dijit.form.NumberTextBox" value="0" upperCase="true" required="false" name="product.purchasePrice" style="width: 90px;"></div>
		</td>
	</tr>
	<tr>
		<td>VAT (In %)</td>
		<td>
			<div dojoType="dijit.form.NumberTextBox" value="0" upperCase="true" required="false" name="product.vat" style="width: 90px;"></div>
		</td>
	</tr>
	<tr>
		<td>Unit Type</td>
		<td>
			<div dojoType="dijit.form.Select" required="true" name="product.unitType" style="width: 90px;" options="[{value:'Kg/s',label:'Kg/s',selected:true},{value:'Gms',label:'Gms'},{value:'Ltr/s',label:'Ltr/s'},{value:'Ml',label:'Ml'},{value:'Pc/s',label:'Pc/s'},{value:'Tin/s',label:'Tin/s'},{value:'Box/s',label:'Box/s'}]"></div>
		</td>
	</tr>
	<tr>
		<td>Reorder Limit</td>
		<td>
			<div dojoType="dijit.form.NumberTextBox" value="0" required="false" name="product.reorderLimit" style="width: 90px;"></div>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td colspan="2" align="left">
			<button type="submit" dojoType="dijit.form.Button">Save Product</button>
			&nbsp;
			<button type="reset" dojoType="dijit.form.Button">Reset</button>
		</td>
	</tr>
</table>
</div>
<div data-dojo-type="dijit.Dialog" style="width:450px;" data-dojo-props="title:'Edit Product'" id="editDialog">
<div id="editFormDiv">
<input type="hidden" id="editBrandId" name="product.brandId">
<input type="hidden" id="editProductId" name="product.id">
<table>
	<tr>
		<td>Brand Name</td>
		<td>
			<div dojoType="dijit.form.ValidationTextBox" id="editBrandName" style="width: 200px;" readonly="true"></div>
		</td>
	</tr>
	<tr>
		<td>Product Name</td>
		<td>
			<div dojoType="dijit.form.ValidationTextBox" id="editProductName" upperCase="true" name="product.productName" style="width: 200px;"></div>
		</td>
	</tr>
	<tr>
		<td>Purchase Price (Per Kg/Ltr)</td>
		<td>
			<div dojoType="dijit.form.NumberTextBox" id="editPurchasePrice" value="0" required="false" name="product.purchasePrice" style="width: 90px;"></div>
		</td>
	</tr>
	<tr>
		<td>VAT (In %)</td>
		<td>
			<div dojoType="dijit.form.NumberTextBox" id="editVat" value="0" required="false" name="product.vat" style="width: 90px;"></div>
		</td>
	</tr>
	<tr>
		<td>Unit Type</td>
		<td>
			<div dojoType="dijit.form.Select" id="editUnitType" required="true" name="product.unitType" style="width: 90px;" options="[{value:'Kg/s',label:'Kg/s',selected:true},{value:'Gms',label:'Gms'},{value:'Ltr/s',label:'Ltr/s'},{value:'Ml',label:'Ml'},{value:'Pc/s',label:'Pc/s'},{value:'Tin/s',label:'Tin/s'},{value:'Box/s',label:'Box/s'}]"></div>
		</td>
	</tr>
	<tr>
		<td>Reorder Limit</td>
		<td>
			<div dojoType="dijit.form.NumberTextBox" id="editRLimit" value="0" required="false" name="product.reorderLimit" style="width: 90px;"></div>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td colspan="2" align="left">
			<button type="submit" dojoType="dijit.form.Button">Save Changes</button>
		</td>
	</tr>
</table>
</div>
</div>
</body>
</html>