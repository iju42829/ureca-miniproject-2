package com.ureca.miniproject.groupcode.service;

import java.util.List;

import com.ureca.miniproject.groupcode.dto.CommonCodeResultDto;

public interface CommonCodeService {
	CommonCodeResultDto getCommonCodeList(List<String> goupCodes);


}
