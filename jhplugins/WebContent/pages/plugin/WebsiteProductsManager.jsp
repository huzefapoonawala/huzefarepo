<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<s:include value="../common/header.html" />
<script type="text/javascript">
	require(['js/plugin/WebsiteProductsManager']);
</script>
</head>
<body>
<!-- <form id="uploadForm" style="width: 100%; visibility: hidden;" data-dojo-type="dijit.form.Form" method="post" enctype="multipart/form-data">
	<br>
	<table>
		<tr>
			<td colspan="3">
				<div data-dojo-type="dijit.form.Button" id="ftpButton" data-dojo-props="type:'submit',label:'Display Products To Delete'"></div>
			</td>
		</tr>
	</table>
	<br>
</form> -->
<form id="uploadForm" style="width: 100%; visibility: hidden;" data-dojo-type="dijit.form.Form" method="post" enctype="multipart/form-data">
	<br>
	<table>
		<tr style="display: none;">
			<td>Step 1:</td>
			<td colspan="3">
				<div data-dojo-type="dijit.form.Button" id="dCateButton" data-dojo-props="type:'button',label:'Download Categories From Orgill FTP'"></div>
				<span id="WaitingDownloadCategoryMessage" style="display:none;">Please wait categories are being downloaded from orgill FTP...</span>
				<span id="CompleteDownloadCategoryMessage" style="display:none;">Completed downloading categories from orgill FTP</span>
			</td>
		</tr>
		<tr style="display: none;"><td>&nbsp;</td></tr>
		<tr style="display: none;">
			<td>Step 2:</td>
			<td colspan="3">
				<div data-dojo-type="dijit.form.Button" id="showDept2AddButton" data-dojo-props="type:'button',label:'Show Departments To Add'"></div>
				<div data-dojo-type="dijit.form.Button" id="showCate2AddButton" data-dojo-props="type:'button',label:'Show Categories To Add'"></div>
			</td>
		</tr>
		<tr style="display: none;"><td>&nbsp;</td></tr>
		<tr>
			<td>Step 1:</td>
			<td colspan="3">
				<div data-dojo-type="dijit.form.Button" id="ftpButton" data-dojo-props="type:'submit',label:'Download Items From Orgill FTP'"></div>
				<span id="WaitingDownloadFtpMessage" style="display:none;">Please wait files are being downloaded from orgill FTP...</span>
				<span id="CompleteDownloadFtpMessage" style="display:none;">Completed downloading files from orgill FTP</span>
			</td>
		</tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td>Step 2:</td>
			<td colspan="3">
				<div data-dojo-type="dijit.form.Button" id="item2DeleteButton" data-dojo-props="type:'button',label:'Show Items To Deactivate'"></div>
				&nbsp;&nbsp;&nbsp;
				<div data-dojo-type="dijit.form.Button" id="item2ActivateButton" data-dojo-props="type:'button',label:'Show Items To Activate'"></div>
				&nbsp;&nbsp;&nbsp;
				<div data-dojo-type="dijit.form.Button" id="item2AddButton" data-dojo-props="type:'button',label:'Show Items To Add'" style="display: none;"></div>
			</td>
		</tr>
	</table>
	<br>
</form>
<form id="manageForm" style="width: 100%; display: none;" data-dojo-type="dijit.form.Form" >
	<br>
	<div id="newImageDiv" style="display: none;">Enter New Images Path <div data-dojo-type="dijit.form.ValidationTextBox" id="imagePath" data-dojo-props=""></div></div>
	<div id="dataGrid"></div>
	<div align="center">
		<div data-dojo-type="dijit.form.Button" id="deleteItemsButton" data-dojo-props="label:'Deactivate Selected Items',type:'button'" style="display: none;"></div>
		<div data-dojo-type="dijit.form.Button" id="activateItemsButton" data-dojo-props="label:'Activate Selected Items',type:'button'" style="display: none;"></div>
		<div data-dojo-type="dijit.form.Button" id="addItemsButton" data-dojo-props="label:'Add Selected Items',type:'button'" style="display: none;"></div>
	</div>
	<div align="center" id="AddingLoadingItemsMessageDiv" style="display: none;">Please wait while adding items...</div>
</form>
<form id="manageCateForm" style="width: 100%; display: none;" data-dojo-type="dijit.form.Form" >
	<div id="cateGrid"></div>
	<div align="center">
		<div data-dojo-type="dijit.form.Button" id="addCateButton" data-dojo-props="label:'Add Selected Categories',type:'button'" style="display: :none;"></div>
	</div>
</form>
<form id="manageDeptForm" style="width: 100%; display: none;" data-dojo-type="dijit.form.Form" >
	<div id="deptGrid"></div>
	<div align="center">
		<div data-dojo-type="dijit.form.Button" id="addDeptButton" data-dojo-props="label:'Add Selected Departments',type:'button'" style="display: :none;"></div>
	</div>
</form>
</body>
</html>