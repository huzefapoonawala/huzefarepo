package com.simplydifferent.action;

import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import com.opensymphony.xwork2.ActionSupport;
import com.simplydifferent.dao.intr.MasterDAO;
import com.simplydifferent.vo.CategoryVO;

public class CategoryManager extends ActionSupport {

	private static final long serialVersionUID = 8157956677456702200L;
	private List<CategoryVO> list;
	private MasterDAO masterDAO;
	private CategoryVO categoryVO;
	private String result;
	
	public String categoryList() {
		list = masterDAO.getCategoryList();
		return SUCCESS;
	}
	
	public String save() {
		try {
			masterDAO.saveCategory(categoryVO.extractSuper());
			result = "Category saved successfully...";
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
		}
		return SUCCESS;
	}
	
	@JSON(name="items")
	public List<CategoryVO> getList() {
		return list;
	}
	public void setList(List<CategoryVO> list) {
		this.list = list;
	}

	public void setMasterDAO(MasterDAO masterDAO) {
		this.masterDAO = masterDAO;
	}

	public CategoryVO getCategoryVO() {
		return categoryVO;
	}

	public void setCategoryVO(CategoryVO categoryVO) {
		this.categoryVO = categoryVO;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
