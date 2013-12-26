<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<s:include value="../common/header.html" />
<script type="text/javascript">
	require(["sd/sales/SalesReceipt","dojo/domReady!"]);
</script>
</head>
<body>
<div id="salesReceiptTab" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design:'headline',style:{width:'100%',height:'100%'},title:'Create Sales Receipt'">
	<div data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design: 'headline',region:'top',style:{width:'100%',height:'20%'},gutters:false">
		<div id="searchForm" data-dojo-type="dijit.form.Form" data-dojo-props="region: 'center',splitter: false" style="width: 100%;">
			<table>
			<tr>
				<td>
					Customer Name<br>
					<div dojoType="dijit.form.FilteringSelect" id="customerId" required="false" placeHolder="Enter Customer Name" searchAttr="name" name="invoice.customerId" style="width: 200px; text-align: right;" maxHeight="200" label="Customer Name"></div>
				</td>
				<td width="50px">&nbsp;</td>
				<td align="center"><table><tr><td>
					Invoice Number<br>
					<div dojoType="dijit.form.FilteringSelect" id="salesInvoiceId" required="false" placeHolder="Enter Invoice Number" searchAttr="invoiceNumber" name="invoice.invoiceId" style="width: 130px;" maxHeight="200"></div>
				</td></tr></table></td>
			</tr>
			</table>
		</div>
		<div data-dojo-type="dojox.layout.TableContainer" data-dojo-props="cols:2,orientation:'vert',region:'bottom'" style="width: 100%;">
			<div data-dojo-type="dijit.layout.ContentPane" align="right">
				<button dojoType="dijit.form.Button" id="searchButton">Search</button>
				<button dojoType="dijit.form.Button" id="sResetButton">Reset</button>
			</div>
		</div>
	</div>
</div>
<div data-dojo-type="dijit.Dialog" style="width:430px;" data-dojo-props="title:'Create Sales Receipt'" id="createReceiptDialog">
<div id="csidformDiv" data-dojo-type="dijit.form.Form" >
<input type="hidden" id="rIdx" name="rIdx">
<input type="hidden" id="csrInvoiceId" name="sr.invoiceId">
<div data-dojo-type="dojox.layout.TableContainer" data-dojo-props="cols:2,orientation:'horiz', labelWidth:160" id="createSRTable">
	<div colspan="2" dojoType="dijit.form.ValidationTextBox" required="true" id="cCustomerName" name="customerName" style="width: 200px;" readonly="true" label="Customer Name"></div>
	<div colspan="2" dojoType="dijit.form.ValidationTextBox" required="true" id="cInvoiceNumber" name="invoiceNumber" style="width: 200px;" readonly="true" label="Invoice Number"></div>
	<div colspan="2" dojoType="dijit.form.NumberTextBox" value="0" required="true" id="cTotalAmount" name="totalAmount" style="width: 150px;" readonly="true" label="Total Billed Amount"></div>
	<div colspan="2" dojoType="dijit.form.NumberTextBox" value="0" required="true" id="cBalanceAmount" name="balanceAmount" style="width: 150px;" readonly="true" label="Amount Balance"></div>
	<div colspan="2" dojoType="dijit.form.NumberTextBox" value="0" required="true" id="cReceivedAmount" name="sr.receivedAmount" style="width: 150px;" label="Amount Received"></div>
	<div colspan="2" dojoType="dijit.form.DateTextBox" required="true" id="cReceivedDate" name="sr.receivedDate" constraints="{selector: 'date', datePattern: 'dd-MMM-yyyy', locale: 'en-us'}" style="width: 150px;" label="Received Date"></div>
	<div colspan="2" dojoType="dijit.form.Select" required="true" id="cPaymentType" name="sr.paymentType" style="width: 150px;" label="Payment Type" options="[{value:'cash',label:'Cash'},{value:'cheque',label:'Cheque'},{value:'dd',label:'DD'},{value:'cc',label:'Credit Card'}]"></div>
	<div colspan="2" dojoType="dijit.form.ValidationTextBox" required="false" id="cRefNumber" name="sr.paymentRefNumber" style="width: 200px;" label="Cheque/DD/Credit Card Number (if applicable)"></div>
	<div colspan="2" dojoType="dijit.form.ValidationTextBox" required="false" id="cBankName" name="sr.paymentBankName" label="Bank Name (if applicable)" style="width: 200px;" ></div>
	<div colspan="2" data-dojo-type="dijit.layout.ContentPane">
		<button type="submit" dojoType="dijit.form.Button">Create Receipt</button>
		&nbsp;
		<button type="reset" dojoType="dijit.form.Button">Cancel</button>
	</div>
</div>
</div>
</div>
</body>
</html>