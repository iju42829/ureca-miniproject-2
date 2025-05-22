package com.ureca.miniproject.groupcode.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ureca.miniproject.groupcode.dto.CodeDto;
import com.ureca.miniproject.groupcode.dto.CodeResultDto;
import com.ureca.miniproject.groupcode.entity.Code;
import com.ureca.miniproject.groupcode.entity.CodeKey;
import com.ureca.miniproject.groupcode.repository.CodeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CodeServiceImpl implements CodeService {
	private final CodeRepository codeRepository;
		
	@Override
	public CodeResultDto listCode(String groupCode, int pageNumber, int pageSize) {
		CodeResultDto codeResultDto = new CodeResultDto();
		try {
			Pageable pageable = PageRequest.of(pageNumber, pageSize);
			Page<Code> page = codeRepository.findByGroupCode(groupCode,pageable);
			List<CodeDto> codeDtoList =new ArrayList<>();
			
			//Page<Code> -> List<CodeDto>
			page.toList().forEach(code -> codeDtoList.add(CodeDto.fromCode(code)));
			codeResultDto.setCodeDtoList(codeDtoList);
			
			Long count = codeRepository.count();
			codeResultDto.setCount(count);
			codeResultDto.setResult("success");
		}catch(Exception e) {
			e.printStackTrace();
			codeResultDto.setResult("fail");
		}
		return codeResultDto;
	}

	@Override
	public CodeResultDto detailCode(CodeKey codeKey) {
		CodeResultDto codeResultDto = new CodeResultDto();
		try {
			Optional<Code> optionalCode = codeRepository.findById(codeKey);
			optionalCode.ifPresentOrElse(
					detailCode->{
						codeResultDto.setCodeDto(CodeDto.fromCode(detailCode));
						codeResultDto.setResult("success");
					},
					() -> codeResultDto.setResult("fail")
				);						
		}catch(Exception e) {
			e.printStackTrace();
			codeResultDto.setResult("fail");
		}
		
		return codeResultDto;
	}

	@Override
	public CodeResultDto countCode() {
		// TODO Auto-generated method stub
		return null;
	}

}
