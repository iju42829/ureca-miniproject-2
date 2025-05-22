package com.ureca.miniproject.groupcode.dto;

import com.ureca.miniproject.groupcode.entity.GroupCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GroupCodeDto {
	private String groupCode;
	private String groupCodeName;
	private String groupCodeDesc;
	
	public static GroupCodeDto fromGroupCode(GroupCode groupCode) {
		return GroupCodeDto.builder()
			.groupCode(groupCode.getGroupCode())
			.groupCodeName(groupCode.getGroupCodeName())
			.groupCodeDesc(groupCode.getGroupCodeDesc())
			.build();
	}

}
