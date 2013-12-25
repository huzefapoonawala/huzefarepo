package com.jh.vo;


/*import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;*/

//@Document(collection="categories")
public class WebCategory {

//	@Id
	private String categoryId;
	private String categoryName;
//	@DBRef private WebCategory parent;
	private String parentId;
	private int hierarchyLevel;
	private Integer webCategoryId;
	private Integer webParentId;
	public WebCategory() {
		super();
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public int getHierarchyLevel() {
		return hierarchyLevel;
	}
	public void setHierarchyLevel(int hierarchyLevel) {
		this.hierarchyLevel = hierarchyLevel;
	}
	public Integer getWebCategoryId() {
		return webCategoryId;
	}
	public void setWebCategoryId(Integer webCategoryId) {
		this.webCategoryId = webCategoryId;
	}
	public Integer getWebParentId() {
		return webParentId;
	}
	public void setWebParentId(Integer webParentId) {
		this.webParentId = webParentId;
	}
	
	/*@Override
	public boolean equals(Object obj) {
		boolean isEqual = super.equals(obj);
		if (!isEqual && obj != null && obj.getClass() == this.getClass()) {
			WebCategory wc = (WebCategory)obj;
			if (this.getId() != null && wc.getId() != null) {
				isEqual = this.getId().equalsIgnoreCase(wc.getId());
			}
		}
		return isEqual;
	}*/
	
}
