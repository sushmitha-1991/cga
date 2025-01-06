package com.dsp.cga.mapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.dsp.cga.assurance.dto.PartDetailsDTO;
import com.dsp.cga.assurance.dto.StatusDTO;
import com.dsp.cga.assurance.partdetails.entity.PartDetails;
import com.dsp.cga.response.GenericResponse;
@Component
public class PartDetailsMapper {
	
    private static final Logger log = LoggerFactory.getLogger(PartDetailsMapper.class);

	@Autowired
	 private RestTemplate restTemplate;

	@Value("${master.codeServiceUrl}")
	private String codeServiceUrl;


	public PartDetailsDTO toDTO(PartDetails partDetails) {
		
		StatusDTO statusDTO = null;
        if (partDetails.getStatusId() != null) {
            statusDTO = fetchStatusData(partDetails.getStatusId());
        }
        
        return PartDetailsDTO.builder()
                .id(partDetails.getId())
                .part(partDetails.getPart())
                .subject(partDetails.getSubject())
                .statusId(partDetails.getStatusId())
                .extentOfAssurance(partDetails.getExtentOfAssurance())
                .statusDTO(statusDTO)
                .build();
    }
	
	

    public static PartDetails toEntity(PartDetailsDTO dto) {
        return PartDetails.builder()
                .id(dto.getId())
                .part(dto.getPart())
                .subject(dto.getSubject())
                .statusId(dto.getStatusId())
                .extentOfAssurance(dto.getExtentOfAssurance())
                .build();
    }

    
    

	public void updatePartDetailsFromDTO(PartDetailsDTO partDetailsDTO, PartDetails partDetails) {
		 if (partDetailsDTO.getExtentOfAssurance() != null) {
		        partDetails.setExtentOfAssurance(partDetailsDTO.getExtentOfAssurance());
		    }
		    if (partDetailsDTO.getPart() != null) {
		        partDetails.setPart(partDetailsDTO.getPart());
		    }
		    if (partDetailsDTO.getSubject() != null) {
		        partDetails.setSubject(partDetailsDTO.getSubject());
		    }
		    if (partDetailsDTO.getStatusId() != null) {
		        partDetails.setStatusId(partDetailsDTO.getStatusId());
		    }
		
		
	}
	 private  StatusDTO fetchStatusData(Long statusId) {
	        String statusServiceUrl = codeServiceUrl + "/" + statusId;
	        log.debug("Calling master service URL for Status: {}", statusServiceUrl);

	        ResponseEntity<GenericResponse<StatusDTO>> response = restTemplate.exchange(
	                statusServiceUrl,
	                HttpMethod.GET,
	                null,
	                new ParameterizedTypeReference<GenericResponse<StatusDTO>>() {
	                }
	        );

	        if (!response.getStatusCode().is2xxSuccessful()) {
	            log.error("Failed to fetch data from master service for Status: {} - {}", response.getStatusCode(), response.getBody());
	            throw new RuntimeException("Failed to fetch data from master service for Status. Status: " + response.getStatusCode());
	        }

	        StatusDTO statusDTO = response.getBody().getData();
	        log.debug("Fetched StatusDTO: {}", statusDTO);

	        return statusDTO;
	    }
   
}


