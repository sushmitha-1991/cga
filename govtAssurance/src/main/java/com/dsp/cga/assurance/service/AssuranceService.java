package com.dsp.cga.assurance.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.dsp.cga.assurance.dto.AssuranceDTO;
import com.dsp.cga.assurance.dto.AssuranceListDTO;
import com.dsp.cga.assurance.dto.AssuranceRequestCountDTO;
import com.dsp.cga.assurance.implementationrequest.dto.AssuranceWithImplementationRequestDTO;

public interface AssuranceService {

	AssuranceDTO createAssurance(AssuranceDTO assuranceDTO);

	List<AssuranceDTO> getAllAssurance();

	AssuranceDTO getAssuranceById(Long id);

	//List<AssuranceDTO> getFilteredAssurances(LocalDate fromDate, LocalDate toDate, Long ministryId, Long memberId);

//	AssuranceDTO updateAssurance(Long id, AssuranceDTO assuranceDTO);

	AssuranceDTO updateAssuranceById(Long id, AssuranceDTO assuranceDTO);

	List<AssuranceListDTO> getAllAssurancesList();

	List<AssuranceDTO> getFilteredAssurances(LocalDate fromDate, LocalDate toDate, Long ministryId, Long rajyaSabhaSessionNo, Long memberId);

	List<AssuranceDTO> getAllLokSabhaAssurance();

	List<AssuranceDTO> getFilteredAssurancesLs(LocalDate fromDate, LocalDate toDate, Long ministryId,Long lokSabhaNo, Long lokSabhaSessionNo, Long memberId);

	

	List<AssuranceListDTO> getAssuranceNos(Integer ministryCode, Long sessionNo, Long houseId);

	void createAssuranceWithImplementation(AssuranceWithImplementationRequestDTO dto);

	AssuranceDTO getByAssuranceNo(String assuranceNo);

	//AssuranceRequestCountDTO getAssuranceRequestCounts(Long sessionNo, Integer ministryCode);

	

	
}
