package com.crud.operations.Entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "subtypeCodes")
public class SubTypeCodes {

	@Id
	@Column(name = "subtypeCode")
	private String subtypeCode;

	@Column(name = "description")
	private String description;

	//@JsonFormat(pattern = "dd-MM-yyyy")
	@Temporal(TemporalType.DATE)
	@Column(name="updatedOn")	
	private Date updatedOn = new Date();

	public String getSubtypeCode() {
		return subtypeCode;
	}

	public void setSubtypeCode(String subtypeCode) {
		this.subtypeCode = subtypeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

}
