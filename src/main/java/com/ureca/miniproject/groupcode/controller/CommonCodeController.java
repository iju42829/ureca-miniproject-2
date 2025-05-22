package com.ureca.miniproject.groupcode.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ureca.miniproject.groupcode.dto.CommonCodeResultDto;
import com.ureca.miniproject.groupcode.service.CommonCodeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommonCodeController {
    private final CommonCodeService commonCodeService;
    
    @PostMapping("/commoncodes")
    public CommonCodeResultDto getCommonCodeList(@RequestBody List<String> groupCodes) {
        return commonCodeService.getCommonCodeList(groupCodes);
    }
}