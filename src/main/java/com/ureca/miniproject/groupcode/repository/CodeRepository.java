package com.ureca.miniproject.groupcode.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ureca.miniproject.groupcode.entity.Code;
import com.ureca.miniproject.groupcode.entity.CodeKey;

public interface CodeRepository  extends JpaRepository<Code,CodeKey> {
	
	//Code의 CRUD외에
	//groupCode 기준 코드 조회 <- JPQL
	@Query("select c from Code c where c.id.groupCode = :groupCode order by c.orderNo")
	Page<Code> findByGroupCode(@Param("groupCode") String groupCode,Pageable pageable);
}
