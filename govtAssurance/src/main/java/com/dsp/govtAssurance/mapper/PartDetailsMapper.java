package com.dsp.govtAssurance.mapper;

import org.springframework.stereotype.Component;

import com.dsp.govtAssurance.dto.PartDetailsDTO;
import com.dsp.govtAssurance.partDetails.entity.PartDetails;
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
}

