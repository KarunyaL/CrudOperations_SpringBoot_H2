package com.crud.operations.model;


import org.springframework.stereotype.Component;

@Component
public class SubTypeCodesModel {

	private String subtypeCode;
	private String description;

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


}
