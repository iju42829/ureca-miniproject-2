package com.ureca.miniproject.groupcode.service;

import com.ureca.miniproject.groupcode.dto.CodeResultDto;
import com.ureca.miniproject.groupcode.entity.Code;
import com.ureca.miniproject.groupcode.entity.CodeKey;

public interface CodeService {
	
	CodeResultDto listCode(String groupCode, int pageNumber, int pageSize);
	CodeResultDto detailCode(CodeKey codeKey);
	CodeResultDto countCode();
}
