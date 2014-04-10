<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<s:include value="../common/header.html" />
<script type="text/javascript">
	require(['js/common/ExtensionConfigs']);
</script>
</head>
<body>
<form id="saveConfigsForm" style="width: 100%; visibility: hidden;" data-dojo-type="dijit.form.Form" >
	<br>
	<div id="appPropWarningDiv" style="display: none;">
		<h2 style="color: red;"><img alt="" src="../images/red-siren-icon.png">&nbsp;Application properties file path (system variable = jhplugins.path.properties) is not set !!!</h2>
	</div>
	<table>
		<tr style="display: none;">
			<td>
				<font color="red">*</font>Application Properties File Path&nbsp;&nbsp;
			</td>
			<td>
				<div data-dojo-type="dijit.form.ValidationTextBox" id="prop0_value" data-dojo-props="placeHolder:'', style:'width: 400px;', name:'properties[0].propertyValue', required:true"></div>
				<input type="hidden" name="properties[0].propertyKey" id="prop0_key" value="jhplugins.path.properties">
				<input type="hidden" name="properties[0].propertyType" id="prop0_type" value="SYS_ENV">
			</td>
		</tr>
		<tr>
			<td>
				<font color="red">*</font>Dynamics Product Image Path&nbsp;&nbsp;
			</td>
			<td>
				<div data-dojo-type="dijit.form.ValidationTextBox" id="prop1_value" data-dojo-props="placeHolder:'', style:'width: 400px;', name:'properties[1].propertyValue', required:true"></div>
				<input type="hidden" name="properties[1].propertyKey" id="prop1_key" value="file.systempath.image">
			</td>
		</tr>
		<tr>
			<td>
				<font color="red">*</font>Dynamics Database IP Address/Host Name&nbsp;&nbsp;
			</td>
			<td>
				<div data-dojo-type="dijit.form.ValidationTextBox" id="prop2_value" data-dojo-props="placeHolder:'', name:'properties[2].propertyValue', required:true"></div>
				<input type="hidden" name="properties[2].propertyKey" id="prop2_key" value="dynamics.database.ip">
			</td>
		</tr>
		<tr>
			<td>
				<font color="red">*</font>Dynamics Database Name&nbsp;&nbsp;
			</td>
			<td>
				<div data-dojo-type="dijit.form.ValidationTextBox" id="prop3_value" data-dojo-props="placeHolder:'', name:'properties[3].propertyValue', required:true"></div>&nbsp;(if SQL Express append ';instance=SQLEXPRESS' after database name)
				<input type="hidden" name="properties[3].propertyKey" id="prop3_key" value="dynamics.database.name">
			</td>
		</tr>
		<tr>
			<td>
				<font color="red">*</font>Dynamics Database Username&nbsp;&nbsp;
			</td>
			<td>
				<div data-dojo-type="dijit.form.ValidationTextBox" id="prop4_value" data-dojo-props="placeHolder:'', name:'properties[4].propertyValue', required:true"></div>
				<input type="hidden" name="properties[4].propertyKey" id="prop4_key" value="dynamics.database.username">
			</td>
		</tr>
		<tr>
			<td>
				<font color="red">*</font>Dynamics Database Password&nbsp;&nbsp;
			</td>
			<td>
				<div data-dojo-type="dijit.form.ValidationTextBox" id="prop5_value" data-dojo-props="placeHolder:'', name:'properties[5].propertyValue', required:true"></div>
				<input type="hidden" name="properties[5].propertyKey" id="prop5_key" value="dynamics.database.password">
				&nbsp;&nbsp;<div data-dojo-type="dijit.form.Button" id="chkDbConnButton" data-dojo-props="label:'Check Database Connection'"></div>
			</td>
		</tr>
		<tr>
			<td>
				Orgill Webfiles FTP Username&nbsp;&nbsp;
			</td>
			<td>
				<div data-dojo-type="dijit.form.ValidationTextBox" id="prop6_value" data-dojo-props="placeHolder:'', name:'properties[6].propertyValue', required:false"></div>
				<input type="hidden" name="properties[6].propertyKey" id="prop6_key" value="jhplugins.username.webfiles.orgill">
			</td>
		</tr>
		<tr>
			<td>
				Orgill Webfiles FTP Password&nbsp;&nbsp;
			</td>
			<td>
				<div data-dojo-type="dijit.form.ValidationTextBox" id="prop7_value" data-dojo-props="placeHolder:'', name:'properties[7].propertyValue', required:false"></div>
				<input type="hidden" name="properties[7].propertyKey" id="prop7_key" value="jhplugins.password.webfiles.orgill">
				&nbsp;&nbsp;<div data-dojo-type="dijit.form.Button" id="chkWebFtpConnButton" data-dojo-props="label:'Check Webfile FTP Connection'"></div>
			</td>
		</tr>
		<tr>
			<td>
				Orgill Purchase Invoice FTP Username&nbsp;&nbsp;
			</td>
			<td>
				<div data-dojo-type="dijit.form.ValidationTextBox" id="prop8_value" data-dojo-props="placeHolder:'', name:'properties[8].propertyValue', required:false"></div>
				<input type="hidden" name="properties[8].propertyKey" id="prop8_key" value="jhplugins.username.pi.orgill">
			</td>
		</tr>
		<tr>
			<td>
				Orgill Purchase Invoice FTP Password&nbsp;&nbsp;
			</td>
			<td>
				<div data-dojo-type="dijit.form.ValidationTextBox" id="prop9_value" data-dojo-props="placeHolder:'', name:'properties[9].propertyValue', required:false"></div>
				<input type="hidden" name="properties[9].propertyKey" id="prop9_key" value="jhplugins.password.pi.orgill">
				&nbsp;&nbsp;<div data-dojo-type="dijit.form.Button" id="chkPoFtpConnButton" data-dojo-props="label:'Check Invoice FTP Connection'"></div>
			</td>
		</tr>
		<tr>
			<td>
				Store Name for Product Label&nbsp;&nbsp;
			</td>
			<td>
				<div data-dojo-type="dijit.form.ValidationTextBox" id="prop10_value" data-dojo-props="placeHolder:'', name:'properties[10].propertyValue', required:false, maxLength:11"></div>
				<input type="hidden" name="properties[10].propertyKey" id="prop10_key" value="jhplugins.productlabel.storename">
			</td>
		</tr>
		<tr>
			<td colspan="2" align="left">
				<div data-dojo-type="dijit.form.Button" id="updtPropButton" data-dojo-props="label:'Update Properties',type:'submit'"></div>
				<!-- <div data-dojo-type="dijit.form.Button" data-dojo-props="label:'Clear',type:'reset'"></div> -->
			</td>
		</tr>
	</table>
	<br><br>
	<div>
		<font color="red">*</font> indicates that property is mandatory.
	</div>
</form>
</body>
</html>