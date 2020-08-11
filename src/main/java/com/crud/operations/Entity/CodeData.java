package com.crud.operations.Entity;


import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Table(name="codeData")
public class CodeData implements Serializable{

	@Id
	@Column(name="_id")
	private String id;

	@OneToMany(targetEntity = Code.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "cd_fk", referencedColumnName = "_id")
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
		return "CodeData [id=" + id + ", code=" + code + "]";
	}


}
