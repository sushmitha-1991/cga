package com.dsp.cga.mapper;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.dsp.cga.assurance.dto.AssuranceDTO;
import com.dsp.cga.assurance.dto.MemberDTO;
import com.dsp.cga.assurance.dto.PartDetailsDTO;
import com.dsp.cga.assurance.dto.StatusDTO;
import com.dsp.cga.assurance.dto.SupplementaryDTO;
import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.partdetails.entity.PartDetails;
import com.dsp.cga.assurance.supplementry.entity.ClubbedMemberInfo;
import com.dsp.cga.assurance.supplementry.entity.Supplementary;
import com.dsp.cga.exception.ResourceNotFoundException;
import com.dsp.cga.response.GenericResponse;

@Component
public class AssuranceMapper {
	
    private static final Logger log = LoggerFactory.getLogger(AssuranceMapper.class);


	@Autowired
	private SupplementaryMapper supplementaryMapper;
	
	@Autowired
	private PartDetailsMapper partDetailsMapper;
	
	 @Autowired
	    private RestTemplate restTemplate;
	    
	    @Value("${master.codeServiceUrl}")
		private String codeServiceUrl;

	    @Value("${member.memberServiceUrl}")
	    private String memberServiceUrl;

	// Map Assurance entity to AssuranceDTO
	public AssuranceDTO toDTO(Assurance assurance) {
		if (assurance == null) {
			return null;
		}

		List<PartDetailsDTO> partDetailsDTOs = assurance.getPartDetails().stream().map(partDetailsMapper::toDTO)
				.collect(Collectors.toList());

		List<SupplementaryDTO> supplementaryDTOs = assurance.getSupplementary().stream().map(supplementaryMapper::toDTO)
				.collect(Collectors.toList());

		return AssuranceDTO.builder().id(assurance.getId()).lokSabhaNo(assurance.getLokSabhaNo())
				.lokSabhaSessionNo(assurance.getLokSabhaSessionNo())
				.rajyaSabhaSessionNo(assurance.getRajyaSabhaSessionNo()).assuranceDate(assurance.getAssuranceDate())
				.categoryId(assurance.getCategoryId()).questionNo(assurance.getQuestionNo())
				.assuranceTypeId(assurance.getAssuranceTypeId()).subject(assurance.getSubject())
				.memberId(assurance.getMemberId()).ministryId(assurance.getMinistryId())
				.departmentId(assurance.getDepartmentId()).pageNo(assurance.getPageNo()).fileNo(assurance.getFileNo())
				.statusId(assurance.getStatusId()).pendingDueTo(assurance.getPendingDueTo())
				.mpaRefDate(assurance.getMpaRefDate()).reportNo(assurance.getReportNo()).remarks(assurance.getRemarks())
				.dueDateOfImplementation(assurance.getDueDateOfImplementation())
				.dateOfImplementation(assurance.getDateOfImplementation())
				.detailsOfImplementation(assurance.getDetailsOfImplementation())
				.textOfAssurance(assurance.getTextOfAssurance()).supplementaryPageNo(assurance.getSupplementaryPageNo())
				.supplementaryMemberId(assurance.getSupplementaryMemberId()).draftStatus(assurance.getDraftStatus())
				.sourceId(assurance.getSourceId()).assuranceNo(assurance.getAssuranceNo())
				.supplement(assurance.getSupplement())
				.ansDate(assurance.getAnsDate())
				.questionType(assurance.getQuestionType())
				.pageNoCorrected(assurance.getPageNoCorrected()).pageNoUncorrected(assurance.getPageNoUncorrected())
				.clubbedMembers(assurance.getClubbedMembers())
				.assuranceFiles(assurance.getAssuranceFiles())
				.statusId(assurance.getStatusId())
                .ansDate(assurance.getAnsDate())
                .questionType(assurance.getQuestionType())
				.groupName(assurance.getGroupName()).mpaRefNo(assurance.getMpaRefNo())
				.otherPendingDueTo(assurance.getOtherPendingDueTo()).partDetails(partDetailsDTOs)
				.supplementary(supplementaryDTOs) // Set the List<SupplementaryDTO>
				.build();
	}

	// Map AssuranceDTO to Assurance entity
	public Assurance toEntity(AssuranceDTO assuranceDTO) {
		if (assuranceDTO == null) {
			return null;
		}

		Assurance assurance = Assurance.builder().id(assuranceDTO.getId()).lokSabhaNo(assuranceDTO.getLokSabhaNo())
				.lokSabhaSessionNo(assuranceDTO.getLokSabhaSessionNo())
				.rajyaSabhaSessionNo(assuranceDTO.getRajyaSabhaSessionNo())
				.assuranceDate(assuranceDTO.getAssuranceDate()).categoryId(assuranceDTO.getCategoryId())
				.questionNo(assuranceDTO.getQuestionNo()).assuranceTypeId(assuranceDTO.getAssuranceTypeId())
				.subject(assuranceDTO.getSubject()).memberId(assuranceDTO.getMemberId())
				.ministryId(assuranceDTO.getMinistryId()).departmentId(assuranceDTO.getDepartmentId())
				.pageNo(assuranceDTO.getPageNo()).fileNo(assuranceDTO.getFileNo()).statusId(assuranceDTO.getStatusId())
				.pendingDueTo(assuranceDTO.getPendingDueTo()).mpaRefDate(assuranceDTO.getMpaRefDate())
				.reportNo(assuranceDTO.getReportNo()).remarks(assuranceDTO.getRemarks())
				.dueDateOfImplementation(assuranceDTO.getDueDateOfImplementation())
				.dateOfImplementation(assuranceDTO.getDateOfImplementation())
				.detailsOfImplementation(assuranceDTO.getDetailsOfImplementation())
				.textOfAssurance(assuranceDTO.getTextOfAssurance())
				.supplementaryPageNo(assuranceDTO.getSupplementaryPageNo())
				.supplementaryMemberId(assuranceDTO.getSupplementaryMemberId())
				.supplement(assuranceDTO.getSupplement())
				.draftStatus(assuranceDTO.getDraftStatus()).assuranceNo(assuranceDTO.getAssuranceNo())
				.pageNoCorrected(assuranceDTO.getPageNoCorrected())
				.ansDate(assuranceDTO.getAnsDate())
				.questionType(assuranceDTO.getQuestionType())
				.assuranceFiles(assuranceDTO.getAssuranceFiles())
				.pageNoUncorrected(assuranceDTO.getPageNoUncorrected()).sourceId(assuranceDTO.getSourceId())
				.groupName(assuranceDTO.getGroupName()).mpaRefNo(assuranceDTO.getMpaRefNo())
				.otherPendingDueTo(assuranceDTO.getOtherPendingDueTo()).clubbedMembers(assuranceDTO.getClubbedMembers())
				.build();

		List<Long> clubbedMembers = Optional.ofNullable(assuranceDTO.getClubbedMembers())
				.orElse(Collections.emptyList());
		assurance.setClubbedMembers(clubbedMembers);
		// Handle PartDetails Mapping with Null Safety
		List<PartDetails> partDetailsEntities = Optional.ofNullable(assuranceDTO.getPartDetails())
				.orElse(Collections.emptyList()).stream().map(partDetailsDTO -> {
					PartDetails partDetails = PartDetailsMapper.toEntity(partDetailsDTO);
					partDetails.setAssurance(assurance); // Set parent reference
					return partDetails;
				}).collect(Collectors.toList());

		// Handle Supplementary Mapping with Null Safety
		List<Supplementary> supplementaryEntities = Optional.ofNullable(assuranceDTO.getSupplementary())
				.orElse(Collections.emptyList()).stream().map(supplementaryDTO -> {
					Supplementary supplementary = SupplementaryMapper.toEntity(supplementaryDTO);
					supplementary.setAssurance(assurance); // Set parent reference
					return supplementary;
				}).collect(Collectors.toList());

		// Set mapped entities in Assurance
		assurance.setPartDetails(partDetailsEntities);
		assurance.setSupplementary(supplementaryEntities);

		return assurance;

	}
	
	 public void updateAssuranceFromDTO(AssuranceDTO assuranceDTO, Assurance assurance) {
	        // Update the fields that can be changed
	        if (assuranceDTO.getLokSabhaNo() != null) {
	            assurance.setLokSabhaNo(assuranceDTO.getLokSabhaNo());
	        }
	        if (assuranceDTO.getLokSabhaSessionNo() != null) {
	            assurance.setLokSabhaSessionNo(assuranceDTO.getLokSabhaSessionNo());
	        }
	        if (assuranceDTO.getRajyaSabhaSessionNo() != null) {
	            assurance.setRajyaSabhaSessionNo(assuranceDTO.getRajyaSabhaSessionNo());
	        }
	        if (assuranceDTO.getAssuranceDate() != null) {
	            assurance.setAssuranceDate(assuranceDTO.getAssuranceDate());
	        }
	        if (assuranceDTO.getCategoryId() != null) {
	            assurance.setCategoryId(assuranceDTO.getCategoryId());
	        }
	        if (assuranceDTO.getQuestionNo() != null) {
	            assurance.setQuestionNo(assuranceDTO.getQuestionNo());
	        }
	        if (assuranceDTO.getAssuranceTypeId() != null) {
	            assurance.setAssuranceTypeId(assuranceDTO.getAssuranceTypeId());
	        }
	        if (assuranceDTO.getSubject() != null) {
	            assurance.setSubject(assuranceDTO.getSubject());
	        }
	        if (assuranceDTO.getMemberId() != null) {
	            assurance.setMemberId(assuranceDTO.getMemberId());
	        }
	        if (assuranceDTO.getMinistryId() != null) {
	            assurance.setMinistryId(assuranceDTO.getMinistryId());
	        }
	        if (assuranceDTO.getDepartmentId() != null) {
	            assurance.setDepartmentId(assuranceDTO.getDepartmentId());
	        }
	        if (assuranceDTO.getPageNo() != null) {
	            assurance.setPageNo(assuranceDTO.getPageNo());
	        }
	        if (assuranceDTO.getFileNo() != null) {
	            assurance.setFileNo(assuranceDTO.getFileNo());
	        }
	        if (assuranceDTO.getStatusId() != null) {
	            assurance.setStatusId(assuranceDTO.getStatusId());
	        }
	        if (assuranceDTO.getPendingDueTo() != null) {
	            assurance.setPendingDueTo(assuranceDTO.getPendingDueTo());
	        }
	        if (assuranceDTO.getMpaRefDate() != null) {
	            assurance.setMpaRefDate(assuranceDTO.getMpaRefDate());
	        }
	        if (assuranceDTO.getMpaRefNo() != null) {
	            assurance.setMpaRefNo(assuranceDTO.getMpaRefNo());
	        }
	        if (assuranceDTO.getReportNo() != null) {
	            assurance.setReportNo(assuranceDTO.getReportNo());
	        }
	        if (assuranceDTO.getRemarks() != null) {
	            assurance.setRemarks(assuranceDTO.getRemarks());
	        }
	        if (assuranceDTO.getDueDateOfImplementation() != null) {
	            assurance.setDueDateOfImplementation(assuranceDTO.getDueDateOfImplementation());
	        }
	        
	        if (assuranceDTO.getSupplement() != null) {
	            assurance.setSupplement(assuranceDTO.getSupplement());
	        }
	        if (assuranceDTO.getDateOfImplementation() != null) {
	            assurance.setDateOfImplementation(assuranceDTO.getDateOfImplementation());
	        }
	        if (assuranceDTO.getDetailsOfImplementation() != null) {
	            assurance.setDetailsOfImplementation(assuranceDTO.getDetailsOfImplementation());
	        }
	        if (assuranceDTO.getTextOfAssurance() != null) {
	            assurance.setTextOfAssurance(assuranceDTO.getTextOfAssurance());
	        }
	        if (assuranceDTO.getSupplementaryPageNo() != null) {
	            assurance.setSupplementaryPageNo(assuranceDTO.getSupplementaryPageNo());
	        }
	        if (assuranceDTO.getSupplementaryMemberId() != null) {
	            assurance.setSupplementaryMemberId(assuranceDTO.getSupplementaryMemberId());
	        }
	        if (assuranceDTO.getDraftStatus() != null) {
	            assurance.setDraftStatus(assuranceDTO.getDraftStatus());
	        }
	        if (assuranceDTO.getSourceId() != null) {
	            assurance.setSourceId(assuranceDTO.getSourceId());
	        }
	        
	        if (assuranceDTO.getAnsDate() != null) {
	            assurance.setAnsDate(assuranceDTO.getAnsDate());
	        }
	        if (assuranceDTO.getQuestionType() != null) {
	            assurance.setQuestionType(assuranceDTO.getQuestionType());
	        }
	        if (assuranceDTO.getOtherPendingDueTo() != null) {
	            assurance.setOtherPendingDueTo(assuranceDTO.getOtherPendingDueTo());
	        }
	        if (assuranceDTO.getGroupName() != null) {
	            assurance.setGroupName(assuranceDTO.getGroupName());
	        }
	        if (assuranceDTO.getAssuranceNo() != null) {
	            assurance.setAssuranceNo(assuranceDTO.getAssuranceNo());
	        }
	        if (assuranceDTO.getPageNoCorrected() != null) {
	            assurance.setPageNoCorrected(assuranceDTO.getPageNoCorrected());
	        }
	        if (assuranceDTO.getPageNoUncorrected() != null) {
	            assurance.setPageNoUncorrected(assuranceDTO.getPageNoUncorrected());
	        }
	        
	        List<Long> clubbedMembers = Optional.ofNullable(assuranceDTO.getClubbedMembers())
					.orElse(Collections.emptyList());
			assurance.setClubbedMembers(clubbedMembers);


			//return assurance;
	 }

	public PartDetails mapToPartEntity(PartDetailsDTO partDTO) {
		 return PartDetails.builder()
	                .part(partDTO.getPart())
	                .subject(partDTO.getSubject())
	                .statusId(partDTO.getStatusId())
	                .extentOfAssurance(partDTO.getExtentOfAssurance())
	                .build();
	    }

	public Supplementary mapToSupplementaryEntity(SupplementaryDTO suppDTO) {
		 return Supplementary.builder()
	                .supp(suppDTO.getSupp())
	                .memberId(suppDTO.getMemberId())
	                .part(suppDTO.getPart())
	                .subject(suppDTO.getSubject())
	                .statusId(suppDTO.getStatusId())
	                .clubbedMembers(suppDTO.getClubbedMembers().stream()
	                        .map(dto -> new ClubbedMemberInfo(dto.getClubbedMember(), dto.getPageNumber()))
	                        .collect(Collectors.toList()))
	                .extentOfAssurance(suppDTO.getExtentOfAssurance())
	                .build();
	    }

	public AssuranceDTO mapToDTO(Assurance updatedAssurance) {
		
	 
	        AssuranceDTO dto = new AssuranceDTO();
	        dto.setAssuranceNo(updatedAssurance.getAssuranceNo());
	        dto.setRajyaSabhaSessionNo(updatedAssurance.getRajyaSabhaSessionNo());
	        dto.setAssuranceDate(updatedAssurance.getAssuranceDate());
	        dto.setAssuranceTypeId(updatedAssurance.getAssuranceTypeId());
	        dto.setSourceId(updatedAssurance.getSourceId());
	        dto.setQuestionNo(updatedAssurance.getQuestionNo());
	        dto.setMinistryId(updatedAssurance.getMinistryId());
	        dto.setDepartmentId(updatedAssurance.getDepartmentId());
	        dto.setMemberId(updatedAssurance.getMemberId());
	        dto.setFileNo(updatedAssurance.getFileNo());
	        dto.setPageNoUncorrected(updatedAssurance.getPageNoUncorrected());
	        dto.setPageNoCorrected(updatedAssurance.getPageNoCorrected());
	        dto.setMpaRefNo(updatedAssurance.getMpaRefNo());
	        dto.setReportNo(updatedAssurance.getReportNo());
	        dto.setRemarks(updatedAssurance.getRemarks());
	        dto.setMpaRefDate(updatedAssurance.getMpaRefDate());
	        
	        // Set partDetails and supplementary
	        dto.setPartDetails(updatedAssurance.getPartDetails().stream()
	                .map(this::mapToPartDTO)
	                .collect(Collectors.toList()));
	        dto.setSupplementary(updatedAssurance.getSupplementary().stream()
	                .map(this::mapToSupplementaryDTO)
	                .collect(Collectors.toList()));

	        return dto;
	    }
	
	 public PartDetailsDTO mapToPartDTO(PartDetails partDetail) {
		 PartDetailsDTO dto = new PartDetailsDTO();
	        dto.setId(partDetail.getId());
	        dto.setPart(partDetail.getPart());
	        dto.setSubject(partDetail.getSubject());
	        dto.setStatusId(partDetail.getStatusId());
	        dto.setExtentOfAssurance(partDetail.getExtentOfAssurance());
	        return dto;
	    }

	    // Map Supplementary entity to DTO
	    public SupplementaryDTO mapToSupplementaryDTO(Supplementary supplementary) {
	    	
	    	List<MemberDTO> clubbedMemberDetails = supplementary.getClubbedMembers().stream()
	                .map(this::mapClubbedMemberInfoToMemberDTO)
	                .collect(Collectors.toList());
	        
	        MemberDTO memberDTO = null;
	        if (supplementary.getMemberId() != null) {
	            memberDTO = fetchMemberData(supplementary.getMemberId());
	        }

	        // Fetch StatusDTO
	        StatusDTO statusDTO = null;
	        if (supplementary.getStatusId() != null) {
	            statusDTO = fetchStatusData(supplementary.getStatusId());
	        }
	        SupplementaryDTO dto = new SupplementaryDTO();
	        dto.setSupp(supplementary.getSupp());
	        dto.setMemberId(supplementary.getMemberId());
	        dto.setPart(supplementary.getPart());
	        dto.setSubject(supplementary.getSubject());
	        dto.setStatusId(supplementary.getStatusId());
	       dto.setStatusDTO(statusDTO);
	       dto.setMemberDTO(memberDTO);
	        dto.setExtentOfAssurance(supplementary.getExtentOfAssurance());
	        return dto;
	    }
	    
	    // New method to map ClubbedMemberInfo to MemberDTO with pageNumber
	    private MemberDTO mapClubbedMemberInfoToMemberDTO(ClubbedMemberInfo clubbedMemberInfo) {
	        MemberDTO memberDTO = fetchMemberData(clubbedMemberInfo);
	      //  memberDTO.setPageNumber(clubbedMemberInfo.getPageNumber()); // Set pageNumber explicitly
	        return memberDTO;
	    }
	    
	    private StatusDTO fetchStatusData(Long statusId) {
			String statusServiceUrl = codeServiceUrl + statusId;
			log.debug("Calling master service URL for Status: {}", statusServiceUrl);

			ResponseEntity<GenericResponse<StatusDTO>> response = restTemplate.exchange(statusServiceUrl, HttpMethod.GET,
					null, new ParameterizedTypeReference<GenericResponse<StatusDTO>>() {
					});
			System.out.println(response);

			if (!response.getStatusCode().is2xxSuccessful()) {
				log.error("Failed to fetch data from master service for Status: {} - {}", response.getStatusCode(),
						response.getBody());
				throw new RuntimeException(
						"Failed to fetch data from master service for Status. Status: " + response.getStatusCode());
			}

			StatusDTO statusDTO = response.getBody().getData();
			log.debug("Fetched StatusDTO: {}", statusDTO);

			return statusDTO;
		}
	    
	    private MemberDTO fetchMemberData(Long memberId) {
			String url = memberServiceUrl + memberId;
			log.debug("Fetching Member by memberId from URL: {}", url);

			try {
				ResponseEntity<GenericResponse<MemberDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
						new ParameterizedTypeReference<GenericResponse<MemberDTO>>() {
						});

				if (response == null || response.getBody() == null || response.getBody().getData() == null) {
					log.error("Response or body is null when fetching Member for memberId: {}", memberId);
					throw new RuntimeException("Failed to fetch Member, response is null");
				}

				MemberDTO memberDTO = response.getBody().getData();
				 memberDTO.generateMemberName();
				log.debug("Fetched MemberDTO: {}", memberDTO);
				return memberDTO;

			} catch (ResourceNotFoundException e) {
				log.error("Resource not found for memberId: {}", memberId);
				throw e;
			} catch (Exception e) {
				log.error("Error fetching Member for memberId: {}", memberId, e);
				throw new RuntimeException("Failed to fetch Member data", e);
			}
		}

	    private MemberDTO fetchMemberData(ClubbedMemberInfo clubbedMemberInfo) {
	        if (clubbedMemberInfo == null) {
	            log.error("ClubbedMemberInfo is null");
	            throw new IllegalArgumentException("ClubbedMemberInfo cannot be null");
	        }
	        
	        Long memberId = clubbedMemberInfo.getClubbedMember();
	        if (memberId == null) {
	            log.error("ClubbedMemberInfo has null memberId");
	            throw new IllegalArgumentException("Member ID cannot be null");  
	        }

	        String url = memberServiceUrl + memberId;
	        log.debug("Fetching Member by memberId from URL: {}", url);

	        try {
	            ResponseEntity<GenericResponse<MemberDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
	                    new ParameterizedTypeReference<>() {});

	            if (response == null || response.getBody() == null || response.getBody().getData() == null) {
	                log.error("Response or body is null when fetching Member for memberId: {}", memberId);
	                throw new ResourceNotFoundException("Member not found for ID: " + memberId);
	            }
	            MemberDTO memberDTO = response.getBody().getData();
	            memberDTO.generateMemberName();  // Call generateMemberName to populate memberName
	            return memberDTO;

	          //  return response.getBody().getData();
	        } catch (ResourceNotFoundException e) {
	            log.error("Resource not found for memberId: {}", memberId);
	            throw e;
	        } catch (Exception e) {
	            log.error("Error fetching Member for memberId: {}", memberId, e);
	            throw new RuntimeException("Failed to fetch Member data", e);
	        }
	    }
}