package com.jh.etl.datastore.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.jh.etl.common.enums.Supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name="category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames=true)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Category {

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private int id;
	
	private String name;
	
	private String code;
	
	@Enumerated(EnumType.STRING) 
	private Supplier supplier;
	
	@ManyToOne @Cascade({CascadeType.SAVE_UPDATE}) 
	@JoinColumn(name="PARENT_ID") 
	private Category parent;
	
//	@OneToMany(mappedBy="parent", fetch=FetchType.LAZY) private List<Category> childCategories;
	@Column(name = "CREATED_DATE", nullable = false, updatable = false)
	@CreatedDate 
	private Date createdDate;
	
	@Column(name = "MODIFIED_DATE")
	@LastModifiedDate
	private Date modifiedDate;
	
	public String concatCodeAndSupplier() {
		return String.format("%s,%s", this.code,this.supplier);
	}
}
