package com.simplydifferent.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="generic_sequence")
public class NextSalesInvoiceNumber {

	@Id private String type;
	@Column(name="NEXT_NUMBER") private int nextInvoiceNumber;
	@Column(name="MODIFIED_DATE") @Version private Date modifiedDate;

	public int getNextInvoiceNumber() {
		return nextInvoiceNumber;
	}

	public void setNextInvoiceNumber(int nextInvoiceNumber) {
		this.nextInvoiceNumber = nextInvoiceNumber;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
