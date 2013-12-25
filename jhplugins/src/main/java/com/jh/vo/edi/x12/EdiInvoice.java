package com.jh.vo.edi.x12;

public class EdiInvoice {

	private String poNumber;
	private String shippingReferenceId;
	private String shippingDescription;
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public String getShippingReferenceId() {
		return shippingReferenceId;
	}
	public void setShippingReferenceId(String shippingReferenceId) {
		this.shippingReferenceId = shippingReferenceId;
	}
	public String getShippingDescription() {
		return shippingDescription;
	}
	public void setShippingDescription(String shippingDescription) {
		this.shippingDescription = shippingDescription;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new StringBuilder().append("poNumber=").append(poNumber).append(", shippingReferenceId=").append(shippingReferenceId).append(", shippingDescription=").append(shippingDescription).toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean isValid =  super.equals(obj);
		if (!isValid && obj.getClass() == this.getClass() && obj != null) {
			EdiInvoice tmp = (EdiInvoice)obj;
			if (this.poNumber != null && tmp.poNumber != null) {
				isValid = this.poNumber.equalsIgnoreCase(tmp.poNumber);
			}
		}
		return isValid;
	}
}
