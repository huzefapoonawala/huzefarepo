<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<s:include value="../common/header.html" />
<script type="text/javascript">
	require(["sd/reports/PurchaseReport","dojo/domReady!"]);
</script>
</head>
<body>
<div id="reportDiv" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design:'headline',style:{width:'100%',height:'550px'}">
	<div id="searchForm" data-dojo-type="dijit.form.Form" data-dojo-props="region: 'top',splitter: false" style="width: 100%; height: 22%;">
		<table width="100%">
			<tr>
				<td align="left"><table>
					<tr><td>Supplier Name</td></tr>
					<tr><td>
						<div dojoType="dijit.form.FilteringSelect" id="supplierId" required="false" placeHolder="Enter Supplier Name" searchAttr="supplierName" name="request.supplierId" style="width: 200px; text-align: right;" maxHeight="200" label="Supplier Name"></div>
					</td></tr>
				</table></td>
				<td align="left"><table>
					<tr><td>Invoice Number</td></tr>
					<tr><td>
						<div dojoType="dijit.form.ValidationTextBox" id="invoiceNumber" align="center" placeHolder="Enter Invoice Number" required="false" name="request.invoiceNumber" style="width: 150px;" label="Invoice Number"></div>
					</td></tr>
				</table></td>
				<td align="center"><table>
					<tr><td>Invoice Date-From</td></tr>
					<tr><td>
						<div dojoType="dijit.form.DateTextBox" required="false" align="center" name="request.fromDate" constraints="{selector: 'date', datePattern: 'dd-MMM-yyyy', locale: 'en-us'}" style="width: 110px;" label="Invoice Date-From"></div>
					</td></tr>
				</table></td>
				<td align="right"><table>
					<tr><td>Invoice Date-To</td></tr>
					<tr><td>
						<div dojoType="dijit.form.DateTextBox" required="false" align="right" name="request.toDate" constraints="{selector: 'date', datePattern: 'dd-MMM-yyyy', locale: 'en-us'}" style="width: 110px;" label="Invoice Date-To"></div>
					</td></tr>
				</table></td>			
			</tr>
			<tr><td colspan="4" align="right">&nbsp;</td></tr>
			<tr>
				<td colspan="4" align="right">
					<button dojoType="dijit.form.Button" type="submit">Search</button>
					<button dojoType="dijit.form.Button" type="reset">Reset</button>
				</td>
			</tr>
		</table>
	</div>
</div>
</body>
</html>