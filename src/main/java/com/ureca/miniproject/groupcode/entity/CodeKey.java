package com.ureca.miniproject.groupcode.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Embeddable
public class CodeKey implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String groupCode;
	private String code;
	
	public CodeKey(String groupCode, String code) {
		this.groupCode = groupCode;
		this.code = code;
	}
	
	public CodeKey() {
		
	}
}
