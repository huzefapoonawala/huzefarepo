package com.jh.etl.datastore.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.jh.etl.common.enums.Supplier;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@ToString(includeFieldNames=true)
public class Category {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private int id;
	private String name;
	private String code;
	@Enumerated(EnumType.STRING) private Supplier supplier;
	@ManyToOne @Cascade({CascadeType.SAVE_UPDATE}) @JoinColumn(name="PARENT_ID") private Category parent;
//	@OneToMany(mappedBy="parent", fetch=FetchType.LAZY) private List<Category> childCategories;
	
	public String concatCodeAndSupplier() {
		return String.format("%s,%s", this.code,this.supplier);
	}
}
