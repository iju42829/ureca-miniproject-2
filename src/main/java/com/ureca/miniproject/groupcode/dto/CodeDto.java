package com.ureca.miniproject.groupcode.dto;



import com.ureca.miniproject.groupcode.entity.Code;
import com.ureca.miniproject.groupcode.entity.CodeKey;

import lombok.Data;

@Data
public class CodeDto {
    private String groupCode;
    private String code;
    private String codeName;
    private String codeNameBrief;
    private int orderNo;

    public static CodeDto fromCode(Code code) {
        CodeDto dto = new CodeDto();
        CodeKey key = code.getCodeKey();
        dto.setGroupCode(key.getGroupCode());
        dto.setCode(key.getCode());
        dto.setCodeName(code.getCodeName());
        dto.setCodeNameBrief(code.getCodeNameBrief());
        dto.setOrderNo(code.getOrderNo());
        return dto;
    }
}
