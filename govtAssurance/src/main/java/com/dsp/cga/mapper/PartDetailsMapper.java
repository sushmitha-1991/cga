package com.dsp.cga.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dsp.cga.assurance.dto.PartDetailsDTO;
import com.dsp.cga.assurance.partdetails.entity.PartDetails;
@Component
public class PartDetailsMapper {

	public static PartDetailsDTO toDTO(PartDetails partDetails) {
        return PartDetailsDTO.builder()
                .id(partDetails.getId())
                .part(partDetails.getPart())
                .subject(partDetails.getSubject())
                .statusId(partDetails.getStatusId())
                .extentOfAssurance(partDetails.getExtentOfAssurance())
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
}

