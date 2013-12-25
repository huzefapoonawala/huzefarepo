package com.jh.vo.edi.x12.type997;

public class EdiAcknowledgement {

	private String type;
	private String typeId;
	private String status;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append("{type=").append(type).append(", typeId=").append(typeId).append(", status=").append(status).append("}").toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean isEqual = super.equals(obj);
		if (!isEqual && obj.getClass() == this.getClass()) {
			EdiAcknowledgement ack = (EdiAcknowledgement) obj;
			isEqual = ack.getType().equalsIgnoreCase(this.getType()) && ack.getTypeId().equalsIgnoreCase(this.getTypeId());
		}
		return isEqual;
	}
}
