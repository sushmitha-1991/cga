package com.dsp.cga.assurance.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dsp.cga.assurance.dto.AssuranceDTO;
import com.dsp.cga.assurance.dto.AssuranceListDTO;
import com.dsp.cga.assurance.dto.DashboardAssurancesCountResponseDTO;
import com.dsp.cga.assurance.dto.DashboardAssurancesDetailsResponseDTO;
import com.dsp.cga.assurance.dto.DocumentMetadataDTO;
import com.dsp.cga.assurance.implementationrequest.dto.AssuranceWithImplementationRequestDTO;
import com.dsp.cga.entity.RequestHistory;

public interface AssuranceService {

	AssuranceDTO createAssurance(AssuranceDTO assuranceDTO);

	List<AssuranceDTO> getAllAssurance();
//	****** added pagination to the above service ****************
	Page<AssuranceDTO> getAllAssuranceV2(Pageable pageable,Long sessionNo,LocalDate fromDate,LocalDate toDate,Long memberId,Long ministryId);

	AssuranceDTO getAssuranceById(Long id);

	//List<AssuranceDTO> getFilteredAssurances(LocalDate fromDate, LocalDate toDate, Long ministryId, Long memberId);

//	AssuranceDTO updateAssurance(Long id, AssuranceDTO assuranceDTO);

	AssuranceDTO updateAssuranceById(Long id, AssuranceDTO assuranceDTO);

	List<AssuranceListDTO> getAllAssurancesList();

	List<AssuranceDTO> getFilteredAssurances(LocalDate fromDate, LocalDate toDate, Long ministryId, Long rajyaSabhaSessionNo, Long memberId);

	List<AssuranceDTO> getAllLokSabhaAssurance();
//	****** added pagination to the above service ****************
	Page<AssuranceDTO> getAllLokSabhaAssuranceV2(Pageable pageable,Long lokSabhaNo,Long sessionNo,LocalDate fromDate,LocalDate toDate,Long memberId,Long ministryId);

	List<AssuranceDTO> getFilteredAssurancesLs(LocalDate fromDate, LocalDate toDate, Long ministryId,Long lokSabhaNo, Long lokSabhaSessionNo, Long memberId);

	

	List<AssuranceListDTO> getAssuranceNos(Integer ministryCode, Long sessionNo, Long houseId);

	void createAssuranceWithImplementation(AssuranceWithImplementationRequestDTO dto);

	AssuranceDTO getByAssuranceNo(String assuranceNo);

	//AssuranceRequestCountDTO getAssuranceRequestCounts(Long sessionNo, Integer ministryCode);

	public List<RequestHistory> getInputHistoryByRequestId(Long requestId);
	public List<DocumentMetadataDTO> getDocuments(Long requestId);
	public DashboardAssurancesCountResponseDTO getMgmtDashboardAssurancesCount(Long lokSabhaNoId, Long sessionNoId,
    		Long ministryId,Long memberId,Long statusId, Long categoryId);
	 public List<DashboardAssurancesDetailsResponseDTO> getMgmtDashboardAssurancesDetails(Long lokSabhaNoId, 
			 Long sessionNoId, LocalDate fromDate, LocalDate toDate,
			 Long categoryId, Long memberId, Long ministryId,
			 Long departmentId, Long questionTypeId, Long statusId);
	
}
