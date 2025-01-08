package com.dsp.cga.assurance.repository;

import java.time.LocalDate;
import java.util.List;

import com.dsp.cga.assurance.entity.Assurance;

public interface AssuranceRepositoryCustom {

	List<Assurance> findFilteredAssurancesWithCriteria(LocalDate fromDate, LocalDate toDate, Long ministryId,
			Long memberId);

	List<Assurance> findFilteredAssurancesLsWithCriteria(LocalDate fromDate, LocalDate toDate, Long ministryId,
			Long memberId);
	
   // List<Assurance> findFilteredAssurancesWithCriteria(EntityManager em, LocalDate fromDate, LocalDate toDate, Long ministryId, Long memberId);


}
