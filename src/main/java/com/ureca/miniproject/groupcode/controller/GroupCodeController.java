package com.ureca.miniproject.groupcode.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ureca.miniproject.groupcode.dto.CodeResultDto;
import com.ureca.miniproject.groupcode.service.GroupCodeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GroupCodeController {

    private final GroupCodeService groupCodeService;

    @GetMapping("/groupcodes")
    public CodeResultDto listGroupCode(@RequestParam int pageNumber, @RequestParam int pageSize) {
        return groupCodeService.listGroupCode(pageNumber, pageSize);
    }

    @GetMapping("/groupcodes/{groupCode}")
    public CodeResultDto detailGroupCode(@PathVariable("groupCode") String groupCode) {
        return groupCodeService.detailGroupCode(groupCode);
    }
    
    @GetMapping("/groupcodes/count")
    public CodeResultDto countGroupCode() {
        return groupCodeService.countGroupCode();
    }
    
}