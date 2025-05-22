package com.ureca.miniproject.groupcode.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ureca.miniproject.groupcode.dto.CodeResultDto;

import com.ureca.miniproject.groupcode.entity.CodeKey;
import com.ureca.miniproject.groupcode.service.CodeService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CodeController {
	private final CodeService codeService;
	
	// 모두 
	@GetMapping("/codes")
	public CodeResultDto listCode(
				@RequestParam("groupCode") String groupCode,
				@RequestParam("pageNumber") int pageNumber,
				@RequestParam("pageSize") int pageSize
			) {
		return codeService.listCode(groupCode,pageNumber, pageSize);
	}

	// 상세
	@GetMapping("/codes/{groupCode}/{code}")
	public CodeResultDto detailCode(@PathVariable("groupCode") String groupCode, @PathVariable("code") String code) {
	    CodeKey codeKey = new CodeKey(groupCode, code);
	    return codeService.detailCode(codeKey);
	}
	// count
	@GetMapping("/codes/count")
	public CodeResultDto countCode() {
	    return codeService.countCode();
	}
	


}
