package com.dsp.cga.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.cga.entity.RequestHistory;

@Repository
public interface RequestHistoryRepository extends JpaRepository<RequestHistory,Long>{

	List<RequestHistory> findByRequestId(@Param("requestId")Long requestId);
	
	RequestHistory findTop1ByRequestIdOrderByCreatedAtDesc(@Param("requestId")Long requestId);
}
