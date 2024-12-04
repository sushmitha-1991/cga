package com.dsp.cga.assurance.service;

import java.time.LocalDate;
import java.util.List;

import com.dsp.cga.assurance.dto.AssuranceDTO;
import com.dsp.cga.assurance.dto.AssuranceListDTO;

public interface AssuranceService {

	AssuranceDTO createAssurance(AssuranceDTO assuranceDTO);

	List<AssuranceDTO> getAllAssurance();

	AssuranceDTO getAssuranceById(Long id);

	List<AssuranceDTO> getFilteredAssurances(LocalDate fromDate, LocalDate toDate, Long ministryId, Long memberId);

	AssuranceDTO updateAssurance(Long id, AssuranceDTO assuranceDTO);

	AssuranceDTO updateAssuranceById(Long id, AssuranceDTO assuranceDTO);

	List<AssuranceListDTO> getAllAssurancesList();

	
}
