package com.simplydifferent.action;

import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import com.opensymphony.xwork2.ActionSupport;
import com.simplydifferent.dao.intr.MasterDAO;
import com.simplydifferent.vo.VariantVO;

public class VariantManager extends ActionSupport {

	private static final long serialVersionUID = -1651918389392626195L;
	private MasterDAO masterDAO;
	private String result;
	private List<VariantVO> list;
	private VariantVO variantVO;
	
	public String variantList() {
		list = masterDAO.getVariantList(variantVO != null && variantVO.getProductId() != null ? variantVO.getProductId() : null);
		return SUCCESS;
	}
	
	public String save() {
		try {
			masterDAO.saveVariant(variantVO.extractSuper());
			result = "Variant saved/update successfully...";
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
		}
		return SUCCESS;
	}
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	@JSON(name="items")
	public List<VariantVO> getList() {
		return list;
	}
	public void setList(List<VariantVO> list) {
		this.list = list;
	}
	public VariantVO getVariantVO() {
		return variantVO;
	}
	public void setVariantVO(VariantVO variantVO) {
		this.variantVO = variantVO;
	}
	public void setMasterDAO(MasterDAO masterDAO) {
		this.masterDAO = masterDAO;
	}
}
