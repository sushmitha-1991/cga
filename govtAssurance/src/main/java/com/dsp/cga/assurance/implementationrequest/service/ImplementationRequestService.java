package com.dsp.cga.assurance.implementationrequest.service;

import java.time.LocalDate;

import java.util.List;
import java.util.Map;

import com.dsp.cga.assurance.implementationrequest.dto.AssuranceImplementationDTO;
import com.dsp.cga.assurance.implementationrequest.dto.ImplementationRequestDTO;
import com.dsp.cga.response.GenericResponse;

public interface ImplementationRequestService {

	ImplementationRequestDTO createImplementationRequest(ImplementationRequestDTO implementationRequestDTO);

	GenericResponse<ImplementationRequestDTO> createOrUpdateImplementationRequest(
			ImplementationRequestDTO implementationRequestDTO);

	ImplementationRequestDTO getImplementationRequestByAssuranceId(Long id);

	List<AssuranceImplementationDTO> getAssuranceImplementations(Long sessionNo, LocalDate layingDate,
			Long implementationTypeId, Long statusId, Integer ministryCode);

	AssuranceImplementationDTO getImplementationDetailById(Long id);

	AssuranceImplementationDTO updateImplementationRequest(Long id, ImplementationRequestDTO dto);

//	Object getRequestCounts(Integer ministryCode, Long houseId);

	Map<String, Object> getRequestCounts(Integer ministryCode, Long houseId);

	

	List<AssuranceImplementationDTO> getAllImplementationDetails(String houseAbbr);

	AssuranceImplementationDTO updateImplementationRequestforMinistryStatusById(Long id, ImplementationRequestDTO dto);

	//List<AssuranceImplementationDTO> getAllImplementationDetails();

//	Map<String, Object> getRequestCounts(Integer ministryCode);

}
