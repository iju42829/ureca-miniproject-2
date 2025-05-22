package com.ureca.miniproject.groupcode.service;

import com.ureca.miniproject.groupcode.dto.CodeResultDto;
import com.ureca.miniproject.groupcode.entity.GroupCode;

public interface GroupCodeService {	
	
	CodeResultDto listGroupCode(int pageNumber, int pageSize);
	CodeResultDto detailGroupCode(String groupCode);
	CodeResultDto countGroupCode();
	
}
