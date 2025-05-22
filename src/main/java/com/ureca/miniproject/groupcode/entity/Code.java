package com.ureca.miniproject.groupcode.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Code {

    @EmbeddedId
    private CodeKey codeKey;

    @Column(name = "code_name", nullable = false)
    private String codeName;

    @Column(name = "code_name_brief")
    private String codeNameBrief;

    @Column(name = "order_no")
    private int orderNo;
    
    
}
