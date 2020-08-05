package com.crud.operations.Entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="code")
public class Code implements Serializable {

	@Id
	@Column(name="typeCode")
	private String typeCode;

	@OneToMany(targetEntity = SubTypeCodes.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "st_fk", referencedColumnName = "typeCode")
	private List<SubTypeCodes> subtypeCodes;

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public List<SubTypeCodes> getSubtypeCodes() {
		return subtypeCodes;
	}

	public void setSubtypeCodes(List<SubTypeCodes> subtypeCodes) {
		this.subtypeCodes = subtypeCodes;
	}

	@Override
	public String toString() {
		return "Code [typeCode=" + typeCode + ", subtypeCodes=" + subtypeCodes + "]";
	}


}
