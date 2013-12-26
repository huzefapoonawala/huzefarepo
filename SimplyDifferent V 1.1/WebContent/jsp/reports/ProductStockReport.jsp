<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<s:include value="../common/header.html" />
<script type="text/javascript">
	require(["sd/reports/ProductStockReport","dojo/domReady!"]);
</script>
</head>
<body>
<div id="reportDiv" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design:'headline',style:{width:'100%',height:'550px'}">
	<div id="searchForm" data-dojo-type="dijit.form.Form" data-dojo-props="region: 'top',splitter: false" style="width: 100%; height: 22%;">
		<table width="100%">
			<tr>
				<td align="left"><table>
					<tr><td>Brand Name</td></tr>
					<tr><td>
						<div dojoType="dijit.form.FilteringSelect" id="brandId" required="false" placeHolder="Enter Brand Name" searchAttr="brandName" name="request.brandId" style="width: 200px; text-align: right;" maxHeight="200"></div>
					</td></tr>
				</table></td>
				<td align="left"><table>
					<tr><td>Product Name</td></tr>
					<tr><td>
						<div dojoType="dijit.form.ValidationTextBox" placeHolder="Enter Product Name" required="false" name="request.productName" style="width: 200px;"></div>
					</td></tr>
				</table></td>
				<td align="center">&nbsp;</td>
				<td align="center">&nbsp;</td>			
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