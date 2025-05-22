package com.ureca.miniproject.groupcode.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class CodeResultDto {	
	private  String result;
	private  List<CodeDto> codeDtoList;
	private  CodeDto codeDto;
	private  Long count;
	private List<GroupCodeDto> groupCodeDtoList;
	private GroupCodeDto groupCodeDto;
	public CodeResultDto() {
		
	}
}
