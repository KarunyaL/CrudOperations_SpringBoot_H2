package com.crud.operations.model;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CodeModel{

	private String typeCode;
	private List<SubTypeCodesModel> subtypeCodesModel;

	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public List<SubTypeCodesModel> getSubtypeCodes() {
		return subtypeCodesModel;
	}
	public void setSubtypeCodes(List<SubTypeCodesModel> subtypeCodes) {
		this.subtypeCodesModel = subtypeCodesModel;
	}

	@Override
	public String toString() {
		return "CodeModel [ typeCode=" + typeCode + ", subtypeCodesModel=" + subtypeCodesModel + "]";
	}


}
