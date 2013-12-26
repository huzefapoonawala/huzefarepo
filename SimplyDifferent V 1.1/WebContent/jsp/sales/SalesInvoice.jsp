<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<s:include value="../common/header.html" />
<script type="text/javascript">
	require(["sd/sales/SalesInvoice","dojo/domReady!"]);
</script>
</head>
<body>
<div id="createInvoiceTab" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design:'headline',style:{width:'100%',height:'100%'},title:'Create Invoice'">
	<div data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design: 'headline',region:'top',style:{width:'100%',height:'22%'},gutters:false">
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
					Invoice Number<br>
					<div dojoType="dijit.form.ValidationTextBox" id="invoiceNumber" placeHolder="Enter Invoice Number" readonly="true" required="true" name="invoice.invoiceNumber" style="width: 130px;"></div><br>
					<div dojoType="dijit.form.ValidationTextBox" id="customInvoiceNumber" placeHolder="Enter Custom Invoice Number" required="false" name="invoice.customInvoiceNumber" style="width: 130px;"></div>
				</td>
				<td valign="top" align="center"><table><tr><td>
					Invoice Date<br>
					<div dojoType="dijit.form.DateTextBox" required="true" style="text-align: center; width: 120px;" name="invoice.invoiceDate" label="Invoice Date" constraints="{selector: 'date', datePattern: 'dd-MMM-yyyy', locale: 'en-us'}"></div>
				</td></tr></table></td>
				<td valign="top" align="center"><table><tr><td>
					Customer Name<br>
					<div dojoType="dijit.form.FilteringSelect" id="customerId" required="true" placeHolder="Enter Customer Name" searchAttr="name" name="invoice.customerId" style="width: 200px; text-align: right;" maxHeight="200" label="Customer Name"></div>
				</td></tr></table></td>
				<td valign="top" align="center"><table><tr><td>
					PO Number<br>
					<div dojoType="dijit.form.ValidationTextBox" id="poNumber" placeHolder="Enter PO Number" required="false" name="invoice.poNumber" style="width: 130px;"></div>
				</td></tr></table></td>
				<td valign="top" align="right"><table><tr><td>
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
				<button dojoType="dijit.form.Button" id="deletePEditButton" disabled="true">Delete Selected Particular</button>
			</div>
		</div>
		<div id="editForm" data-dojo-type="dijit.form.Form" data-dojo-props="region: 'center',splitter: false" style="width: 100%;">
			<input type="hidden" id="editParticularsData" name="invoice.invoiceDetails">
			<table width="100%">
			<tr>
				<td>
					Customer Name<br>
					<div dojoType="dijit.form.FilteringSelect" id="editCustomerId" required="true" placeHolder="Enter Customer Name" searchAttr="name" name="invoice.customerId" style="width: 200px; text-align: right;" maxHeight="200" label="Customer Name"></div>
				</td>
				<td align="center"><table><tr><td>
					Invoice Number<br>
					<div dojoType="dijit.form.FilteringSelect" id="editInvoiceId" placeHolder="Enter Invoice Number" required="true" searchAttr="invoiceNumber" name="invoice.invoiceId" style="width: 130px;" maxHeight="200"></div>
					<img alt="" width="16px" height="16px" src="../images/load.png" style="cursor: pointer;" title="Load Invoice" onclick="dojo.publish('si/invoice/load',[]);">
					<img alt="" src="../images/pdf.gif" title="Generate Invoice PDF" style="cursor: pointer;" onclick="dojo.publish('si/invoice/generatepdf',[]);">
				</td></tr></table></td>
				<td align="center"><table><tr><td>
					Invoice Date<br>
					<div dojoType="dijit.form.DateTextBox" id="editInvoiceDate" required="true" style="text-align: center; width: 120px;" name="invoice.invoiceDate" label="Invoice Date" constraints="{selector: 'date', datePattern: 'dd-MMM-yyyy', locale: 'en-us'}"></div>
				</td></tr></table></td>
				<td align="center"><table><tr><td>
					PO Number<br>
					<div dojoType="dijit.form.ValidationTextBox" id="editPoNumber" placeHolder="Enter PO Number" required="false" name="invoice.poNumber" style="width: 130px;"></div>
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
<div data-dojo-type="dijit.Dialog" style="width:500px;" data-dojo-props="title:'Add Particulars To Invoice'" id="addPDialog">
<div id="pformDiv" data-dojo-type="dijit.form.Form" >
<input type="hidden" id="batchNo" name="batchNumber">
<div data-dojo-type="dojox.layout.TableContainer" data-dojo-props="cols:2,orientation:'horiz'" id="pformTable">
	<div colspan="2" dojoType="dijit.form.FilteringSelect" id="brandDiv" placeHolder="Enter Brand Name" searchAttr="brandName" required="true" name="brandId" style="width: 200px;" maxHeight="200" label="Brand Name"></div>
	<div align="left" dojoType="dijit.form.FilteringSelect" id="productDiv" placeHolder="Enter Product Name" searchAttr="productName" required="true" name="productId" style="width: 200px;" maxHeight="200" label="Product Name"></div>
	<div data-dojo-type="dojox.layout.TableContainer" align="right" data-dojo-props="cols:1,orientation:'horiz'">
		<span dojoType="dijit.form.NumberTextBox" value="0" required="false" readonly="true" id="productStock" style="width: 90px;" label="Stock"></span>
	</div>
	<div dojoType="dijit.form.FilteringSelect" id="batchNumber" placeHolder="Enter Batch Number" required="true" searchAttr="batchExpiry" style="width: 150px;" maxHeight="200" label="Batch Number"></div>
	<div data-dojo-type="dojox.layout.TableContainer" align="right" data-dojo-props="cols:1,orientation:'horiz'">
		<span dojoType="dijit.form.NumberTextBox" value="0" required="false" readonly="true" id="productBatchStock" style="width: 90px;" label="Stock"></span>
	</div>
	<div colspan="2" dojoType="dijit.form.NumberTextBox" value="0" required="true" id="ppDiv" name="salesPrice" style="width: 90px;" label="Sales Price (per kg/ltr)"></div>
	<div colspan="2" dojoType="dijit.form.NumberTextBox" value="0" required="true" id="qtyDiv" name="quantity" style="width: 90px;" label="Quantity"></div>
	<div colspan="2" dojoType="dijit.form.NumberTextBox" value="0" required="true" id="vatDiv" name="vat" style="width: 90px;" label="VAT (in %)"></div>
	<div colspan="2" dojoType="dijit.form.ValidationTextBox" required="false" id="utDiv" name="unitType" style="width: 90px;" readonly="true" label="Unit Type"></div>
	<div colspan="2" dojoType="dijit.form.ValidationTextBox" required="false" readonly="true" id="expiryDate" name="expiryDate" label="Expiry Date" style="width: 150px;" ></div>
	<div colspan="2" data-dojo-type="dijit.layout.ContentPane">
		<button type="submit" dojoType="dijit.form.Button">Add To Invoice</button>
		&nbsp;
		<button type="reset" dojoType="dijit.form.Button">Reset</button>
	</div>
</div>
</div>
</div>
<form data-dojo-type="dijit.form.Form" action="../download/generateSalesInvoicePdf.action" method="post" id="downloadForm" target="_salesInvoice">
	<input type="hidden" name="invoice.invoiceId" id="downloadInvoiceId">
</form>
</body>
</html>