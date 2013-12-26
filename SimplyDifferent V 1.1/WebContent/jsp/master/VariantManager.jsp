<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<style type="text/css">
	@IMPORT url("../../resources/dhtmlx/css/dhtmlxgrid.css");
	@IMPORT url("../../resources/dhtmlx/css/skins/dhtmlxgrid_dhx_skyblue.css");
</style>
<s:include value="../common/header.html" />
<script  src="../../resources/dhtmlx/js/dhtmlxcommon.js"></script>
<script  src="../../resources/dhtmlx/js/dhtmlxgrid.js"></script>        
<script  src="../../resources/dhtmlx/js/dhtmlxgridcell.js"></script>
<script type="text/javascript">
dojo.require("sd.PageLayout");
dojo.require("sd.master.VariantManager");
</script>
</head>
<body class="soria">
<div>
	<font style="font-weight: bold; font-size: 14px;"><s:property value="#request.parameters.breadcrum"/></font>
</div>
<br><br>
<form dojoType="dijit.form.Form" action="../../process/action/SaveVariant.action" method="post">
<input type="hidden" name="breadcrum" value="<s:property value="#request.parameters.breadcrum"/>">
<table>
	<col width="175px">
	<tr>
		<td>Select category</td>
		<td>
			<div dojoType="dijit.form.Select" id="categoryName" onChange="sd.master.VariantManager.getProductList" upperCase="true" required="true" name="categoryName" promptMessage="Kindly select category" invalidMessage="Kindly select category"></div>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td>Select product</td>
		<td>
			<div dojoType="dijit.form.Select" id="productId" onChange="sd.master.VariantManager.getVariantList" required="true" name="variantVO.productId"></div>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td>Enter variant name</td>
		<td>
			<div dojoType="dijit.form.ValidationTextBox" id="variantName" upperCase="true" required="true" name="variantVO.variantName" promptMessage="Kindly enter variant name"></div>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td>Enter purchase discount(in %)</td>
		<td>
			<div dojoType="dijit.form.NumberTextBox" id="purchaseDiscount" required="true" style="width: 75px;" name="variantVO.purchaseDiscount" promptMessage="Kindly enter purchase discount" value="0"></div>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td>Enter VAT(in %)</td>
		<td>
			<div dojoType="dijit.form.NumberTextBox" id="vat" required="true" style="width: 75px;" name="variantVO.vat" promptMessage="Kindly enter vat" value="0"></div>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td></td>
		<td>
			<button type="submit" dojoType="dijit.form.Button">Save/Modify variant</button>
			&nbsp;&nbsp;&nbsp;
			<button type="reset" dojoType="dijit.form.Button">Reset</button><br>
			<s:if test="result != null && !result.isEmpty()">
				<font color="red"><s:property value="result"/></font>
			</s:if>
		</td>
	</tr>
</table>
<script type="dojo/method" event="onSubmit" args="evt">
	return this.validate() && confirm('Are you sure, you want to save/update variant.');
</script>
</form>
<br><br>
<div id="VariantListDiv" style="width: 100%; height: 55%;"></div>
</body>
</html>