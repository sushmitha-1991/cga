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
import com.dsp.cga.assurance.dto.SupplementaryDTO;
import com.dsp.cga.assurance.supplementry.entity.Supplementary;
import com.dsp.cga.exception.ResourceNotFoundException;
import com.dsp.cga.response.GenericResponse;
@Component
public class SupplementaryMapper {
	
	private static final Logger log = LoggerFactory.getLogger(SupplementaryMapper.class);

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${member.memberServiceUrl}")
	private String memberServiceUrl;

	 public SupplementaryDTO toDTO(Supplementary supplementary) {
		 
		 List<MemberDTO> clubbedMemberDetails = supplementary.getClubbedMembers().stream()
		            .map(this::fetchMemberData)  // Fetch details for each clubbed member
		            .collect(Collectors.toList());
		 
	        return SupplementaryDTO.builder()
	                .id(supplementary.getId())
	                .supp(supplementary.getSupp())
	                .memberId(supplementary.getMemberId())
	                .clubbedMembersData(clubbedMemberDetails)	       
	                .part(supplementary.getPart())
	                .subject(supplementary.getSubject())
	                .statusId(supplementary.getStatusId())
	                .extentOfAssurance(supplementary.getExtentOfAssurance())
	                .build();
	    }

	    public static Supplementary toEntity(SupplementaryDTO supplementaryDTO) {
	        if (supplementaryDTO == null) {
	            return null;
	        }

	        return Supplementary.builder()
	                .id(supplementaryDTO.getId())
	                .supp(supplementaryDTO.getSupp())
	                .memberId(supplementaryDTO.getMemberId())
	                .clubbedMembers(supplementaryDTO.getClubbedMembers())
	                .part(supplementaryDTO.getPart())
	                .subject(supplementaryDTO.getSubject())
	                .statusId(supplementaryDTO.getStatusId())
	                .extentOfAssurance(supplementaryDTO.getExtentOfAssurance())
	                .build();
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
	}