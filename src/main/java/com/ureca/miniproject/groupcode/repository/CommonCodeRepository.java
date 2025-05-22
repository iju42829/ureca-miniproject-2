package com.ureca.miniproject.groupcode.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ureca.miniproject.groupcode.entity.Code;
import com.ureca.miniproject.groupcode.entity.CodeKey;

@Repository
public interface CommonCodeRepository extends JpaRepository<Code,CodeKey> {
    @Query("select c from Code c where c.id.groupCode in :groupCodes order by c.id.groupCode, c.orderNo")
    List<Code> findByGroupCodes(@Param("groupCodes") List<String> groupCodes);

}
