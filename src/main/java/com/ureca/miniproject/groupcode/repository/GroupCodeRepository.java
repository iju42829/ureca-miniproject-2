package com.ureca.miniproject.groupcode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ureca.miniproject.groupcode.entity.GroupCode;

@Repository
public interface GroupCodeRepository extends JpaRepository<GroupCode,String> {
	
}
