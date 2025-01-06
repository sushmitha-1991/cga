package com.dsp.cga.mapper;

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
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.dsp.cga.assurance.dto.MemberDTO;
import com.dsp.cga.assurance.dto.StatusDTO;
import com.dsp.cga.assurance.dto.SupplementaryDTO;
import com.dsp.cga.assurance.supplementry.entity.ClubbedMemberInfo;
import com.dsp.cga.assurance.supplementry.entity.Supplementary;
import com.dsp.cga.exception.ResourceNotFoundException;
import com.dsp.cga.response.GenericResponse;
@Component
public class SupplementaryMapper {

    private static final Logger log = LoggerFactory.getLogger(SupplementaryMapper.class);

    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${master.codeServiceUrl}")
	private String codeServiceUrl;

    @Value("${member.memberServiceUrl}")
    private String memberServiceUrl;

    public SupplementaryDTO toDTO(Supplementary supplementary) {
        if (supplementary == null) {
            return null;
        }

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

        return SupplementaryDTO.builder()
                .id(supplementary.getId())
                .supp(supplementary.getSupp())
                .memberId(supplementary.getMemberId())
                .clubbedMembersData(clubbedMemberDetails)
                .part(supplementary.getPart())
                .subject(supplementary.getSubject())
                .statusId(supplementary.getStatusId())
                .memberDTO(memberDTO)
                .statusDTO(statusDTO)
                .extentOfAssurance(supplementary.getExtentOfAssurance())
                .build();
    }

    // New method to map ClubbedMemberInfo to MemberDTO with pageNumber
    private MemberDTO mapClubbedMemberInfoToMemberDTO(ClubbedMemberInfo clubbedMemberInfo) {
        MemberDTO memberDTO = fetchMemberData(clubbedMemberInfo);
      //  memberDTO.setPageNumber(clubbedMemberInfo.getPageNumber()); // Set pageNumber explicitly
        return memberDTO;
    }


    public static Supplementary toEntity(SupplementaryDTO supplementaryDTO) {
        if (supplementaryDTO == null) {
            return null;
        }
 
        return Supplementary.builder()
                .id(supplementaryDTO.getId())
                .supp(supplementaryDTO.getSupp())
                .memberId(supplementaryDTO.getMemberId())
                .clubbedMembers(supplementaryDTO.getClubbedMembers().stream()
                        .map(dto -> new ClubbedMemberInfo(dto.getClubbedMember(), dto.getPageNumber()))
                        .collect(Collectors.toList()))
                .part(supplementaryDTO.getPart())
                .subject(supplementaryDTO.getSubject())
                .statusId(supplementaryDTO.getStatusId())
                .extentOfAssurance(supplementaryDTO.getExtentOfAssurance())
                .build();
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

    public void updateSupplementaryFromDTO(SupplementaryDTO supplementaryDTO, Supplementary supplementary) {
        // Update supplementary fields
        if (supplementaryDTO.getSupp() != null) {
            supplementary.setSupp(supplementaryDTO.getSupp());
        }
        if (supplementaryDTO.getMemberId() != null) {
            supplementary.setMemberId(supplementaryDTO.getMemberId());
        }
        if (supplementaryDTO.getClubbedMembers() != null) {
            supplementary.setClubbedMembers(supplementaryDTO.getClubbedMembers().stream()
                    .map(dto -> new ClubbedMemberInfo(dto.getClubbedMember(), dto.getPageNumber()))
                    .collect(Collectors.toList()));
        }
        if (supplementaryDTO.getPart() != null) {
            supplementary.setPart(supplementaryDTO.getPart());
        }
        if (supplementaryDTO.getSubject() != null) {
            supplementary.setSubject(supplementaryDTO.getSubject());
        }
        if (supplementaryDTO.getStatusId() != null) {
            supplementary.setStatusId(supplementaryDTO.getStatusId());
        }
        if (supplementaryDTO.getExtentOfAssurance() != null) {
            supplementary.setExtentOfAssurance(supplementaryDTO.getExtentOfAssurance());
        }
    }

   
    

}
