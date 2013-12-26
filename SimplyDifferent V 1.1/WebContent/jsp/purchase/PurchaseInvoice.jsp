<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<s:include value="../common/header.html" />
<script type="text/javascript">
	require(["sd/purchase/PurchaseInvoice","dojo/domReady!"]);
</script>
</head>
<body>
<div id="createInvoiceTab" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design:'headline',style:{width:'100%',height:'100%'},title:'Create Invoice'">
	<div data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design: 'headline',region:'top',style:{width:'100%',height:'20%'},gutters:false">
		<div data-dojo-type="dojox.layout.TableContainer" data-dojo-props="cols:2,orientation:'vert',region:'top'" style="width: 100%;" id="buttonTable">
			<div data-dojo-type="dijit.layout.ContentPane">
				<button dojoType="dijit.form.Button" id="saveInvoiceButton">Save Invoice</button>
			</div>
			<div data-dojo-type="dijit.layout.ContentPane" align="right">
				<button dojoType="dijit.form.Button" id="addPButton">Add Particular</button>
				<button dojoType="dijit.form.Button" id="deletePButton">Delete Selected Particular</button>
			</div>
		</div>
		<div id="mainForm" data-dojo-type="dijit.form.Form" data-dojo-props="region: 'center',splitter: false" style="width: 100%;">
			<input type="hidden" id="particularsData" name="invoice.invoiceDetails">
			<table width="100%">
			<tr>
				<td>
					Supplier Name<br>
					<div dojoType="dijit.form.FilteringSelect" id="supplierDiv" required="true" placeHolder="Enter Supplier Name" searchAttr="supplierName" name="invoice.supplierId" style="width: 200px; text-align: right;" maxHeight="200" label="Supplier Name"></div>
				</td>
				<td align="center"><table><tr><td>
					Invoice Number<br>
					<div dojoType="dijit.form.ValidationTextBox" id="invoiceDiv" placeHolder="Enter Invoice Number" required="true" name="invoice.invoiceNumber" style="width: 150px;"></div>
					</td></tr></table></td>
				<td align="center"><table><tr><td>
					Invoice Date<br>
					<div dojoType="dijit.form.DateTextBox" required="true" style="text-align: center;" name="invoice.invoiceDate" label="Invoice Date" constraints="{selector: 'date', datePattern: 'dd-MMM-yyyy', locale: 'en-us'}"></div>
				</td></tr></table></td>
				<td align="right"><table><tr><td>
					Total Amount<br>
					<div dojoType="dijit.form.NumberTextBox" id="totalAmount" name="invoice.totalAmount" style="width: 120px; font-size: 15px;" readonly="true"></div>
				</td></tr></table></td>
			</tr>
			</table>
		</div>
	</div>
</div>
<div id="editInvoiceTab" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design:'headline',style:{width:'100%',height:'100%'},title:'View/Edit Invoice'">
	<div data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design: 'headline',region:'top',style:{width:'100%',height:'20%'},gutters:false">
		<div data-dojo-type="dojox.layout.TableContainer" data-dojo-props="cols:2,orientation:'vert',region:'top'" style="width: 100%;">
			<div data-dojo-type="dijit.layout.ContentPane">
				<button dojoType="dijit.form.Button" id="saveChangesButton" disabled="true">Save Changes</button>
				<button dojoType="dijit.form.Button" id="eResetButton">Reset</button>
			</div>
			<div data-dojo-type="dijit.layout.ContentPane" align="right">
				<button dojoType="dijit.form.Button" id="addPEditButton" disabled="true">Add Particular</button>
				<button dojoType="dijit.form.Button" id="deletePEditButton" style="display: none;">Delete Selected Particular</button>
			</div>
		</div>
		<div id="editForm" data-dojo-type="dijit.form.Form" data-dojo-props="region: 'center',splitter: false" style="width: 100%;">
			<input type="hidden" id="editParticularsData" name="invoice.invoiceDetails">
			<table width="100%">
			<tr>
				<td>
					Supplier Name<br>
					<div dojoType="dijit.form.FilteringSelect" id="editSupplierDiv" required="true" placeHolder="Enter Supplier Name" searchAttr="supplierName" name="invoice.supplierId" style="width: 200px; text-align: right;" maxHeight="200" label="Supplier Name"></div>
				</td>
				<td align="center"><table><tr><td>
					Invoice Number<br>
					<div dojoType="dijit.form.FilteringSelect" id="editInvoiceDiv" placeHolder="Enter Invoice Number" searchAttr="invoiceNumber" required="true" name="invoice.invoiceId" style="width: 150px;" maxHeight="200" label="Enter Invoice Number" autoComplete="false"></div><a href="javascript:void(0);" onclick="dojo.publish('pi/invoice/load',[]);">Load Invoice</a>
				</td></tr></table></td>
				<td align="center"><table><tr><td>
					Invoice Date<br>
					<div dojoType="dijit.form.DateTextBox" id="editInvoiceDate" required="true" style="text-align: center;" name="invoice.invoiceDate" label="Invoice Date" constraints="{selector: 'date', datePattern: 'dd-MMM-yyyy', locale: 'en-us'}"></div>
				</td></tr></table></td>
				<td align="right"><table><tr><td>
					Total Amount<br>
					<div dojoType="dijit.form.NumberTextBox" id="editTotalAmount" name="invoice.totalAmount" style="width: 120px; font-size: 15px;" readonly="true"></div>
				</td></tr></table></td>
			</tr>
			</table>
		</div>
	</div>
</div>
<div data-dojo-type="dijit.Dialog" style="width:450px;" data-dojo-props="title:'Add Particulars To Invoice'" id="addPDialog">
<div id="pformDiv" data-dojo-type="dijit.form.Form" >
<div data-dojo-type="dojox.layout.TableContainer" data-dojo-props="cols:1,orientation:'horiz'" id="pformTable">
	<div dojoType="dijit.form.FilteringSelect" id="brandDiv" placeHolder="Enter Brand Name" searchAttr="brandName" required="true" name="brandId" style="width: 200px;" maxHeight="200" label="Brand Name"></div>
	<div dojoType="dijit.form.FilteringSelect" id="productDiv" placeHolder="Enter Product Name" searchAttr="productName" required="true" name="productId" style="width: 200px;" maxHeight="200" label="Product Name"></div>
	<div dojoType="dijit.form.ValidationTextBox" placeHolder="Enter Batch Number" required="true" name="batchNumber" value="default" style="width: 150px;" label="Batch Number"></div>
	<div dojoType="dijit.form.NumberTextBox" value="0" required="true" id="ppDiv" name="purchasePrice" style="width: 90px;" label="Purchase Price (per kg/ltr)"></div>
	<div dojoType="dijit.form.NumberTextBox" value="0" required="true" id="qtyDiv" name="quantity" style="width: 90px;" label="Quantity"></div>
	<div dojoType="dijit.form.NumberTextBox" value="0" required="true" id="vatDiv" name="vat" style="width: 90px;" label="VAT (in %)"></div>
	<div dojoType="dijit.form.DateTextBox" required="true" id="expiryDate" name="expiryDate" label="Expiry Date" displayedValue="01-Apr-2012" style="width: 150px;" constraints="{selector: 'date', datePattern: 'dd-MMM-yyyy', locale: 'en-us'}"></div>
	<div dojoType="dijit.form.ValidationTextBox" required="false" id="utDiv" name="unitType" style="width: 90px;" readonly="true" label="Unit Type"></div>
	<div data-dojo-type="dijit.layout.ContentPane">
		<button type="submit" dojoType="dijit.form.Button">Add To Invoice</button>
		&nbsp;
		<button type="reset" dojoType="dijit.form.Button">Reset</button>
	</div>
</div>
</div>
</div>
</body>
</html>