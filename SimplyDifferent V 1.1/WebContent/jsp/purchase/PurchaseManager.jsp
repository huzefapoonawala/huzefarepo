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
	.label{
		width:100px;
	}
	.label2{
		width:105px;
	}
</style>
<s:include value="../common/header.html" />
<script  src="../../resources/dhtmlx/js/dhtmlxcommon.js"></script>
<script  src="../../resources/dhtmlx/js/dhtmlxgrid.js"></script>        
<script  src="../../resources/dhtmlx/js/dhtmlxgridcell.js"></script>
<script type="text/javascript">
dojo.require("sd.PageLayout");
dojo.require("sd.purchase.PurchaseManager");
</script>
</head>
<body class="soria">
<div>
	<font style="font-weight: bold; font-size: 14px;"><s:property value="#request.parameters.breadcrum"/></font>
</div>
<br><br>
<form dojoType="dijit.form.Form" action="../../process/action/SaveItems.action" id="MainFrm" method="post">
<input type="hidden" name="breadcrum" value="<s:property value="#request.parameters.breadcrum"/>">
<input type="hidden" name="itemList" id="itemList">
<script type="dojo/method">
	dojo.subscribe('topic/pm/SaveItems',this,function(isValid){
		if(confirm('Are you sure, you want to save all the added items.')){
			dojo.attr('itemList','value',dojo.toJson(sd.purchase.PurchaseManager.dataArray,false));
			this.submit();
		}
	});
</script>
</form>
<form dojoType="dijit.form.Form" action="javascript:void(0);" id="Form2" method="post">
<input type="hidden" name="productName" id="productName">
<input type="hidden" name="variantName" id="variantName">
<input type="hidden" name="supplierName" id="supplierName">
<table width="95%" align="center">
	<tr>
		<td><table width="100%">
			<tr>
				<td class="label">Select supplier&nbsp;</td>
				<td>
					<div dojoType="dijit.form.Select" label="Select supplier" id="supplierId" name="supplierId">
						<script type="dojo/method" event="onChange" args="value">
							sd.purchase.PurchaseManager.labelNameChange(this,'supplierName',value);
						</script>
					</div>
				</td>
				<td class="label">Enter pr number&nbsp;</td>
				<td>
					<div dojoType="dijit.form.ValidationTextBox" label="Enter pr number" id="prNumber" name="prNumber" required="true" promptMessage="Enter purchase receipt number"></div>
				</td>
				<td class="label">Select pr date&nbsp;</td>
				<td>
					<div dojoType="dijit.form.DateTextBox" label="Select pr date" id="prDate" name="prDate" required="true" promptMessage="Select purchase receipt date" style="width: 90px;"></div>
				</td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td class="label">Select category&nbsp;</td>
				<td>
					<div dojoType="dijit.form.Select" label="Select category" id="categoryName" onChange="sd.purchase.PurchaseManager.getProductList" required="true" name="categoryName"></div>
				</td>
				<td class="label">Select product&nbsp;</td>
				<td>
					<div dojoType="dijit.form.Select" label="Select product" id="productId" required="true" name="productId">
						<script type="dojo/method" event="onChange" args="value">
							sd.purchase.PurchaseManager.labelNameChange(this,'productName',value);
							sd.purchase.PurchaseManager.getVariantList(value);
						</script>
					</div>
				</td>
				<td class="label">Select variant&nbsp;</td>
				<td>
					<div dojoType="dijit.form.Select" label="Select variant" id="variantId" required="true" name="variantId">
						<script type="dojo/method" event="onChange" args="value">
							sd.purchase.PurchaseManager.labelNameChange(this,'variantName',value);
						</script>
					</div>
				</td>
			</tr>
		</table></td>
	</tr>
	<tr><td><hr style="color: black;"/></td></tr>
	<tr>
		<td><table width="100%">
			<tr>
				<td class="label2">Enter item number</td>
				<td>
					<div dojoType="dijit.form.ValidationTextBox" id="itemNumber" name="itemNumber" promptMessage="Enter item number"></div>
				</td>
				<td class="label2">Enter quantity</td>
				<td>
					<div dojoType="dijit.form.NumberTextBox" required="true" id="quantity" name="quantity" promptMessage="Enter quantity" style="width: 75px;"></div>
					<div dojoType="dijit.form.Select" id="quantityType" options="[{value:'Kgs',label:'Kgs'},{value:'Ltrs',label:'Ltrs'},{value:'Pcs',label:'Pcs'}]" name="quantityType"></div>
				</td>
				<td class="label2">Select expiry date&nbsp;</td>
				<td>
					<div dojoType="dijit.form.DateTextBox" id="expiryDate" name="expiryDate" required="true" promptMessage="Select expiry date" style="width: 90px;"></div>
				</td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td class="label2">Enter purchase price</td>
				<td>
					<div dojoType="dijit.form.NumberTextBox" required="true" id="purchasePrice" name="purchasePrice" promptMessage="Enter purchase price" style="width: 75px;"></div>
				</td>
				<td class="label2">Enter purchase discount(in %)</td>
				<td>
					<div dojoType="dijit.form.NumberTextBox" required="true" id="purchaseDiscount" name="purchaseDiscount" promptMessage="Enter purchase discount" style="width: 75px;"></div>
				</td>
			</tr>
			<tr><td>&nbsp;</td></tr>
		</table></td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td><table width="100%">
			<tr>
				<td align="left">
					<button type="submit" dojoType="dijit.form.Button">Add item</button>
					&nbsp;&nbsp;&nbsp;
					<button type="button" dojoType="dijit.form.Button" disabled="true" onClick="dojo.publish('topic/pm/DeleteItem',[]);">Delete selected item
						<script type="dojo/method">
							dojo.subscribe('topic/pm/ManageDeleteItemButton',this,function(isValid){this.attr('disabled',!isValid);});
						</script>
					</button>
					&nbsp;&nbsp;&nbsp;
					<button type="reset" dojoType="dijit.form.Button">Reset</button><br>
					<s:if test="result != null && !result.isEmpty()">
						<font color="red"><s:property value="result"/></font>
					</s:if>
				</td>
				<td align="right">
					<button type="button" dojoType="dijit.form.Button" disabled="true" onClick="dojo.publish('topic/pm/SaveItems',[]);">Save added items
						<script type="dojo/method">
							dojo.subscribe('topic/pm/ManageDeleteItemButton',this,function(isValid){this.attr('disabled',!isValid);});
						</script>
					</button>
				</td>
			</tr>
		</table></td>
	</tr>
</table>
<script type="dojo/method" event="onSubmit" args="evt">
	if(this.validate())
		dojo.publish('topic/pm/AddItem',[this.attr('id')]);
</script>
</form>
<br>
<div id="ItemListDiv" style="width: 99%; height: 350px;"></div>
</body>
</html>