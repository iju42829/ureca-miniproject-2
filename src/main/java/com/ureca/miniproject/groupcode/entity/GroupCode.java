package com.ureca.miniproject.groupcode.entity;
import org.springframework.data.domain.Persistable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
 
@Entity
@Data
@Table(name = "group_code")
public class GroupCode implements Persistable<String> {

    @Id
    @Column(name = "group_code")
    private String groupCode;

    @Column(name = "group_code_name")
    private String groupCodeName;

    @Column(name = "group_code_desc")
    private String groupCodeDesc;

    @Transient
    private boolean isNew = false;  // INSERT 여부를 명시적으로 지정

    @Override
    public String getId() {
        return groupCode;  // 주의: 기존에는 null 리턴 → 반드시 실제 ID 리턴 필요
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }
}
