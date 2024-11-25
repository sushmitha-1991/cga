package com.dsp.cga.assurance.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.dsp.cga.assurance.dto.AssuranceDTO;
import com.dsp.cga.assurance.dto.AssuranceTypeDTO;
import com.dsp.cga.assurance.dto.DepartmentsDTO;
import com.dsp.cga.assurance.dto.LokSabhaNoDTO;
import com.dsp.cga.assurance.dto.LoksabhaSessionNoDTO;
import com.dsp.cga.assurance.dto.MemberDTO;
import com.dsp.cga.assurance.dto.MinistryDTO;
import com.dsp.cga.assurance.dto.PendingDueToDTO;
import com.dsp.cga.assurance.dto.RajyaSabhaSessionNoDTO;
import com.dsp.cga.assurance.dto.SourceDTO;
import com.dsp.cga.assurance.dto.StatusDTO;
import com.dsp.cga.assurance.entity.Assurance;
//import com.dsp.cga.assurance.external.service.ExternalService;
import com.dsp.cga.assurance.partdetails.repository.PartDetailsRepository;
import com.dsp.cga.assurance.repository.AssuranceRepository;
import com.dsp.cga.assurance.service.AssuranceService;
import com.dsp.cga.assurance.supplementry.repository.SupplementaryRepository;
import com.dsp.cga.exception.ResourceNotFoundException;
import com.dsp.cga.mapper.AssuranceMapper;
import com.dsp.cga.response.GenericResponse;

@Service
public class AssuranceServiceImpl implements AssuranceService {

	private static final Logger log = LoggerFactory.getLogger(AssuranceServiceImpl.class);

	@Value("${member.memberServiceUrl}")
	private String memberServiceUrl;

	@Value("${master.departmentServiceUrl}")
	private String departmentServiceUrl;

//	@Value("${master.assuranceTypeServiceUrl}")
//	private String assuranceTypeServiceUrl;

	@Value("${master.codeServiceUrl}")
    private String codeServiceUrl;

	@Value("${master.lokSabhaNoServiceUrl}")
	private String lokSabhaNoServiceUrl;

	@Value("${master.ministryServiceUrl}")
	private String ministryServiceUrl;

	@Value("${master.lokSabhaSessionNoServiceUrl}")
	private String lokSabhaSessionNoServiceUrl;

	@Value("${master.rajyaSabhaServiceUrl}")
    private String rajyaSabhaServiceUrl;

	@Autowired
	private AssuranceRepository assuranceRepository;

	@Autowired
	private PartDetailsRepository partDetailsRepository;

	@Autowired
	private SupplementaryRepository supplementaryRepository;

	@Autowired
	private AssuranceMapper assuranceMapper;

//	@Autowired
//    private ExternalService externalService;

	@Autowired
	private RestTemplate restTemplate;

	@Transactional
	@Override
	public AssuranceDTO createAssurance(AssuranceDTO assuranceDTO) {
		// Fetch external names using the external service

		validateAssuranceFields(assuranceDTO);

		// Map the AssuranceDTO to Assurance entity
		Assurance assurance = assuranceMapper.toEntity(assuranceDTO);
		// assurance.setClubbedMembers(assuranceDTO.getClubbedMembers());
		assurance.getSupplementary().forEach(supplementary -> {
			supplementary.setAssurance(assurance); // Link Assurance to Supplementary
		});

		// Save Assurance entity
		Assurance savedAssurance = assuranceRepository.save(assurance); // Save once, to make it effectively final

		// Map entity back to DTO to return response
		AssuranceDTO savedAssuranceDTO = assuranceMapper.toDTO(assurance);

		return savedAssuranceDTO;
	}

	public void validateAssuranceFields(AssuranceDTO dto) {
		// Check if Rajya Sabha Session Number is provided
		boolean hasRajyaSabhaSessionNo = dto.getRajyaSabhaSessionNo() != null;

		// Check if both Lok Sabha Number and Session Number are provided
		boolean hasLokSabhaNoAndSessionNo = dto.getLokSabhaNo() != null && dto.getLokSabhaSessionNo() != null;

		// Validate that either Rajya Sabha Session Number is provided,
		// or both Lok Sabha Number and Session Number are provided together.
		if (!(hasRajyaSabhaSessionNo || hasLokSabhaNoAndSessionNo)) {
			throw new IllegalArgumentException("Invalid data: Either Rajya Sabha Session Number must be provided, "
					+ "or both Lok Sabha Number and Session Number must be provided.");
		}

		// Check that if Lok Sabha Number is provided, Session Number must also be
		// provided.
		if (dto.getLokSabhaNo() != null && dto.getLokSabhaSessionNo() == null) {
			throw new IllegalArgumentException(
					"Invalid data: If Lok Sabha Number is provided, Session Number must also be provided.");
		}

		// Check that if Session Number is provided, Lok Sabha Number must also be
		// provided.
		if (dto.getLokSabhaSessionNo() != null && dto.getLokSabhaNo() == null) {
			throw new IllegalArgumentException(
					"Invalid data: If Session Number is provided, Lok Sabha Number must also be provided.");
		}
	}

	@Override
	@Transactional
	public List<AssuranceDTO> getAllAssurance() {
	    log.debug("Entering getAllAssurance");

	    // Fetch only assurances where rajyaSabhaSessionNo is not null
	    List<Assurance> assurances = assuranceRepository.findAssuranceWithRajyaSabhaSessionNotNullAndLokSabhaSessionNull();
	    log.debug("Fetched Assurances with non-null rajyaSabhaSessionNo: {}", assurances);

	    List<AssuranceDTO> assuranceDTOs = new ArrayList<>();
	    for (Assurance assurance : assurances) {
	        log.debug("Processing Assurance: {}", assurance);
	        AssuranceDTO dto = assuranceMapper.toDTO(assurance);

	        // Populate additional data
	        dto.setMemberDTO(fetchMemberData(assurance.getMemberId()));
	        dto.setMinistryDTO(fetchMinistryData(assurance.getMinistryId()));
	        dto.setDepartmentDTO(fetchDepartmentData(assurance.getDepartmentId()));
	        dto.setAssuranceTypeDTO(fetchAssuranceTypeData(assurance.getAssuranceTypeId()));
	        dto.setRajyaSabhaSessionNoDTO(fetchRajyaSabhaSessionData(assurance.getRajyaSabhaSessionNo()));
	        dto.setPendingDueToDTO(fetchPendingDueToData(assurance.getPendingDueTo()));
	        dto.setSourceDTO(fetchSourceData(assurance.getSourceId()));
	        List<MemberDTO> clubbedMembersData = fetchClubbedMembersData(assurance.getClubbedMembers());
	        dto.setClubbedMembersData(clubbedMembersData);
	        assuranceDTOs.add(dto);
	    }

	    log.debug("Returning list of AssuranceDTOs: {}", assuranceDTOs);
	    return assuranceDTOs;
	}


	private MemberDTO fetchMemberData(Long memberId) {
		String url = memberServiceUrl  + memberId;
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

	private List<MemberDTO> fetchClubbedMembersData(List<Long> clubbedMembers) {
	    // Check if the list is not empty
	    if (clubbedMembers == null || clubbedMembers.isEmpty()) {
	        return new ArrayList<>();  // Return an empty list if no members
	    }

	    List<MemberDTO> memberDTOs = new ArrayList<>();

	    // Iterate over each member ID in the clubbedMembers list
	    for (Long memberId : clubbedMembers) {
	        // Fetch the MemberDTO for each memberId
	        MemberDTO memberDTO = fetchMemberData(memberId);
	        if (memberDTO != null) {
	            memberDTOs.add(memberDTO);  // Add the memberDTO to the list
	        }
	    }

	    return memberDTOs;  // Return the list of MemberDTOs
	}

	
	private MinistryDTO fetchMinistryData(Long ministryId) {
		String url = ministryServiceUrl + "/getMinistry/" + ministryId;
		log.debug("Fetching Ministry by ministryId from URL: {}", url);

		try {
			ResponseEntity<GenericResponse<MinistryDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<GenericResponse<MinistryDTO>>() {
					});

			if (response == null || response.getBody() == null || response.getBody().getData() == null) {
				log.error("Response or body is null when fetching Ministry for ministryId: {}", ministryId);
				throw new RuntimeException("Failed to fetch Ministry, response is null");
			}

			MinistryDTO ministryDTO = response.getBody().getData();
			log.debug("Fetched MinistryDTO: {}", ministryDTO);
			return ministryDTO;

		} catch (ResourceNotFoundException e) {
			log.error("Resource not found for ministryId: {}", ministryId);
			throw e;
		} catch (Exception e) {
			log.error("Error fetching Ministry for ministryId: {}", ministryId, e);
			throw new RuntimeException("Failed to fetch Ministry data", e);
		}
	}

	private DepartmentsDTO fetchDepartmentData(Long departmentId) {
		String url = departmentServiceUrl + "/getDepartment/" + departmentId;
		log.debug("Fetching Department by departmentId from URL: {}", url);

		try {
			ResponseEntity<GenericResponse<DepartmentsDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<GenericResponse<DepartmentsDTO>>() {
					});

			if (response == null || response.getBody() == null || response.getBody().getData() == null) {
				log.error("Response or body is null when fetching Department for departmentId: {}", departmentId);
				throw new RuntimeException("Failed to fetch Department, response is null");
			}

			DepartmentsDTO departmentDTO = response.getBody().getData();
			log.debug("Fetched DepartmentDTO: {}", departmentDTO);
			return departmentDTO;

		} catch (ResourceNotFoundException e) {
			log.error("Resource not found for departmentId: {}", departmentId);
			throw e;
		} catch (Exception e) {
			log.error("Error fetching Department for departmentId: {}", departmentId, e);
			throw new RuntimeException("Failed to fetch Department data", e);
		}
	}

	private StatusDTO fetchStatusData(Long statusId) {
	    String statusServiceUrl = codeServiceUrl + statusId;
	    log.debug("Calling master service URL for Status: {}", statusServiceUrl);

	    ResponseEntity<GenericResponse<StatusDTO>> response = restTemplate.exchange(
	        statusServiceUrl,
	        HttpMethod.GET,
	        null,
	        new ParameterizedTypeReference<GenericResponse<StatusDTO>>() {}
	    );
	    System.out.println(response);

	    if (!response.getStatusCode().is2xxSuccessful()) {
	        log.error("Failed to fetch data from master service for Status: {} - {}", response.getStatusCode(), response.getBody());
	        throw new RuntimeException("Failed to fetch data from master service for Status. Status: " + response.getStatusCode());
	    }

	    StatusDTO statusDTO = response.getBody().getData();
	    log.debug("Fetched StatusDTO: {}", statusDTO);

	    return statusDTO;
	}


	

	
	private SourceDTO fetchSourceData(Long sourceId) {
	    String sourceServiceUrl = codeServiceUrl + sourceId;
//		String url = sourceServiceUrl + sourceId;
	    log.debug("Calling master service URL: {}", sourceServiceUrl);

	    ResponseEntity<GenericResponse<SourceDTO>> response = restTemplate.exchange(
	        sourceServiceUrl,
	        HttpMethod.GET,
	        null,
	        new ParameterizedTypeReference<GenericResponse<SourceDTO>>() {}
	    );
System.out.println(response);
	    if (!response.getStatusCode().is2xxSuccessful()) {
	        log.error("Failed to fetch data from master service: {} - {}", response.getStatusCode(), response.getBody());
	        throw new RuntimeException("Failed to fetch data from master service. Status: " + response.getStatusCode());
	    }

	    SourceDTO sourceDTO = response.getBody().getData();
	    log.debug("Fetched SourceDTO: {}", sourceDTO);

	    return sourceDTO;
	}


	private AssuranceTypeDTO fetchAssuranceTypeData(Long assuranceTypeId) {
	    String assuranceTypeServiceUrl = codeServiceUrl + assuranceTypeId;
	    log.debug("Calling master service URL for Assurance Type: {}", assuranceTypeServiceUrl);

	    ResponseEntity<GenericResponse<AssuranceTypeDTO>> response = restTemplate.exchange(
	        assuranceTypeServiceUrl,
	        HttpMethod.GET,
	        null,
	        new ParameterizedTypeReference<GenericResponse<AssuranceTypeDTO>>() {}
	    );
	    System.out.println(response);

	    if (!response.getStatusCode().is2xxSuccessful()) {
	        log.error("Failed to fetch data from master service for Assurance Type: {} - {}", response.getStatusCode(), response.getBody());
	        throw new RuntimeException("Failed to fetch data from master service for Assurance Type. Status: " + response.getStatusCode());
	    }

	    AssuranceTypeDTO assuranceTypeDTO = response.getBody().getData();
	    log.debug("Fetched AssuranceTypeDTO: {}", assuranceTypeDTO);

	    return assuranceTypeDTO;
	}

	private PendingDueToDTO fetchPendingDueToData(Long pendingDueToId) {
	    String pendingDueToServiceUrl = codeServiceUrl + pendingDueToId;
	    log.debug("Calling master service URL for Pending Due To: {}", pendingDueToServiceUrl);

	    ResponseEntity<GenericResponse<PendingDueToDTO>> response = restTemplate.exchange(
	        pendingDueToServiceUrl,
	        HttpMethod.GET,
	        null,
	        new ParameterizedTypeReference<GenericResponse<PendingDueToDTO>>() {}
	    );
	   
	    if (!response.getStatusCode().is2xxSuccessful()) {
	        log.error("Failed to fetch data from master service for Pending Due To: {} - {}", response.getStatusCode(), response.getBody());
	        throw new RuntimeException("Failed to fetch data from master service for Pending Due To. Status: " + response.getStatusCode());
	    }

	    PendingDueToDTO pendingDueToDTO = response.getBody().getData();
	    log.debug("Fetched PendingDueToDTO: {}", pendingDueToDTO);

	    return pendingDueToDTO;
	}


	private LoksabhaSessionNoDTO fetchLokSabhaSessionData(Long sessionNo) {
		String url = lokSabhaSessionNoServiceUrl + "/session/" + sessionNo;
		log.debug("Fetching LokSabhaSession by sessionNo from URL: {}", url);

		try {
			ResponseEntity<GenericResponse<LoksabhaSessionNoDTO>> response = restTemplate.exchange(url, HttpMethod.GET,
					null, new ParameterizedTypeReference<GenericResponse<LoksabhaSessionNoDTO>>() {
					});

			if (response == null || response.getBody() == null || response.getBody().getData() == null) {
				log.error("Response or body is null when fetching LokSabhaSession for sessionNo: {}", sessionNo);
				throw new RuntimeException("Failed to fetch LokSabhaSession, response is null");
			}

			LoksabhaSessionNoDTO lokSabhaSessionDTO = response.getBody().getData();
			log.debug("Fetched LokSabhaSessionDTO: {}", lokSabhaSessionDTO);
			return lokSabhaSessionDTO;

		} catch (ResourceNotFoundException e) {
			log.error("Resource not found for sessionNo: {}", sessionNo);
			throw e;
		} catch (Exception e) {
			log.error("Error fetching LokSabhaSession for sessionNo: {}", sessionNo, e);
			throw new RuntimeException("Failed to fetch LokSabhaSession data", e);
		}
	}

	private LokSabhaNoDTO fetchLokSabhaData(Long lokSabhaNo) {
		String url = lokSabhaNoServiceUrl + "/" + lokSabhaNo;
		log.debug("Fetching LokSabha by lokSabhaNo from URL: {}", url);

		try {
			ResponseEntity<GenericResponse<LokSabhaNoDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<GenericResponse<LokSabhaNoDTO>>() {
					});

			if (response == null || response.getBody() == null || response.getBody().getData() == null) {
				log.error("Response or body is null when fetching LokSabha for lokSabhaNo: {}", lokSabhaNo);
				throw new RuntimeException("Failed to fetch LokSabha, response is null");
			}

			LokSabhaNoDTO lokSabhaDTO = response.getBody().getData();
			log.debug("Fetched LokSabhaDTO: {}", lokSabhaDTO);
			return lokSabhaDTO;

		} catch (ResourceNotFoundException e) {
			log.error("Resource not found for lokSabhaNo: {}", lokSabhaNo);
			throw e;
		} catch (Exception e) {
			log.error("Error fetching LokSabha for lokSabhaNo: {}", lokSabhaNo, e);
			throw new RuntimeException("Failed to fetch LokSabha data", e);
		}
	}

	private RajyaSabhaSessionNoDTO fetchRajyaSabhaSessionData(Long rajyaSabhaSessionNo) {
	    String url = rajyaSabhaServiceUrl + "getById/" + rajyaSabhaSessionNo;
	    log.debug("Fetching RajyaSabhaSession by sessionNo from URL: {}", url);

	    try {
	        ResponseEntity<GenericResponse<RajyaSabhaSessionNoDTO>> response = restTemplate.exchange(
	            url,
	            HttpMethod.GET,
	            null,
	            new ParameterizedTypeReference<GenericResponse<RajyaSabhaSessionNoDTO>>() {}
	        );

	        if (response == null || response.getBody() == null || response.getBody().getData() == null) {
	            log.error("Response or body is null when fetching RajyaSabhaSession for sessionNo: {}", rajyaSabhaSessionNo);
	            throw new RuntimeException("Failed to fetch RajyaSabhaSession, response is null");
	        }

	        RajyaSabhaSessionNoDTO rajyaSabhaSessionDTO = response.getBody().getData();
	        log.debug("Fetched RajyaSabhaSessionDTO: {}", rajyaSabhaSessionDTO);
	        return rajyaSabhaSessionDTO;

	    } catch (ResourceNotFoundException e) {
	        log.error("Resource not found for sessionNo: {}", rajyaSabhaSessionNo);
	        throw e;
	    } catch (Exception e) {
	        log.error("Error fetching RajyaSabhaSession for sessionNo: {}", rajyaSabhaSessionNo, e);
	        throw new RuntimeException("Failed to fetch RajyaSabhaSession data", e);
	    }
	}

	@Override
	@Transactional
	public AssuranceDTO getAssuranceById(Long id) {
	    log.debug("Entering getAssuranceById with ID: {}", id);

	    // Fetch assurance by ID
	    Assurance assurance = assuranceRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Assurance not found with ID: " + id));
	    log.debug("Fetched Assurance: {}", assurance);

	    // Map Assurance entity to DTO
	    AssuranceDTO dto = assuranceMapper.toDTO(assurance);

	    // Populate additional data
	    dto.setMemberDTO(fetchMemberData(assurance.getMemberId()));
	    dto.setMinistryDTO(fetchMinistryData(assurance.getMinistryId()));
	    dto.setDepartmentDTO(fetchDepartmentData(assurance.getDepartmentId()));
	    dto.setAssuranceTypeDTO(fetchAssuranceTypeData(assurance.getAssuranceTypeId()));
	    dto.setRajyaSabhaSessionNoDTO(fetchRajyaSabhaSessionData(assurance.getRajyaSabhaSessionNo()));
	    dto.setPendingDueToDTO(fetchPendingDueToData(assurance.getPendingDueTo()));
	    dto.setSourceDTO(fetchSourceData(assurance.getSourceId()));

	    // Fetch and set clubbed members
	    List<MemberDTO> clubbedMembersData = fetchClubbedMembersData(assurance.getClubbedMembers());
	    dto.setClubbedMembersData(clubbedMembersData);

	    log.debug("Returning AssuranceDTO: {}", dto);
	    return dto;
	}

	@Override
	public List<AssuranceDTO> getFilteredAssurances(LocalDate fromDate, LocalDate toDate, Long ministryId,
			Long memberId) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	@Transactional
//	public List<AssuranceDTO> getFilteredAssurances(
//	        LocalDate fromDate, LocalDate toDate, Long ministryId, Long memberId) {
//	    log.debug("Entering getFilteredAssurances with filters - From: {}, To: {}, MinistryId: {}, MemberId: {}", 
//	              fromDate, toDate, ministryId, memberId);
//
//	    // Fetch filtered assurances
//	    List<Assurance> assurances = assuranceRepository.findFilteredAssurances(fromDate, toDate, ministryId, memberId);
//	    log.debug("Fetched filtered assurances: {}", assurances);
//
//	    // Map assurances to DTOs and populate additional data
//	    return assurances.stream().map(assurance -> {
//	        AssuranceDTO dto = assuranceMapper.toDTO(assurance);
//	        dto.setMemberDTO(fetchMemberData(assurance.getMemberId()));
//	        dto.setMinistryDTO(fetchMinistryData(assurance.getMinistryId()));
//	        dto.setDepartmentDTO(fetchDepartmentData(assurance.getDepartmentId()));
//	        dto.setAssuranceTypeDTO(fetchAssuranceTypeData(assurance.getAssuranceTypeId()));
//	        dto.setRajyaSabhaSessionNoDTO(fetchRajyaSabhaSessionData(assurance.getRajyaSabhaSessionNo()));
//	        dto.setPendingDueToDTO(fetchPendingDueToData(assurance.getPendingDueTo()));
//	        dto.setSourceDTO(fetchSourceData(assurance.getSourceId()));
//	        dto.setClubbedMembersData(fetchClubbedMembersData(assurance.getClubbedMembers()));
//	        return dto;
//	    }).collect(Collectors.toList());
//	}



}
