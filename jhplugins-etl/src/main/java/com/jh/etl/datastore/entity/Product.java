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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.jh.etl.common.enums.Supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder=true)
@ToString(includeFieldNames=true)
@Entity(name="product")
@EntityListeners(AuditingEntityListener.class)
public class Product {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private int id;
	private String sku;
	@ManyToOne @JoinColumn(name="CATEGORY_ID") private Category category;
	private String title;
	private String description;
	private Float width;
	private Float height;
	private Float length;
	private Float weight;
	private String upc;
	private Double retailPrice;
	private String retailUnit;
	private String imageLink;
	@Enumerated(EnumType.STRING) private Supplier supplier;
	
	@Column(name = "CREATED_DATE", nullable = false, updatable = false)
	@CreatedDate 
	private Date createdDate;
	
	@Column(name = "MODIFIED_DATE")
	@LastModifiedDate
	private Date modifiedDate;
}