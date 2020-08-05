package com.crud.operations.model;


import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.stereotype.Component;

import com.crud.operations.Entity.Code;


@Component
public class CodeDataModel {

	@NotEmpty 
	private String id;

	private List<Code> code;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Code> getCode() {
		return code;
	}
	public void setCode(List<Code> code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "CodeDataModel [id=" + id + ", code=" + code + "]";
	}



}
