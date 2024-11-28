package com.dsp.cga.mapper;

import org.springframework.stereotype.Component;

import com.dsp.cga.assurance.droppingrequest.dto.DroppingRequestDTO;
import com.dsp.cga.assurance.droppingrequest.entity.DroppingRequest;

@Component
public class DroppingRequestMapper {
	
	public static DroppingRequestDTO toDTO(DroppingRequest entity) {
        if (entity == null) {
            return null;
        }

        return DroppingRequestDTO.builder()
                .id(entity.getId())
                .assuranceId(entity.getAssuranceId())
                .droppingType(entity.getDroppingType())
                .supplementaryId(entity.getSupplementaryId())
                .partId(entity.getPartId())
                .dropRequestDate(entity.getDropRequestDate())
                .actualDropDate(entity.getActualDropDate())
                .dropStatusId(entity.getDropStatusId())
                .remarks(entity.getRemarks())
                .draftStatus(entity.getDraftStatus())
                .build();
    }
	
	public static DroppingRequest toEntity(DroppingRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return DroppingRequest.builder()
                .id(dto.getId())
                .assuranceId(dto.getAssuranceId())
                .droppingType(dto.getDroppingType())
                .supplementaryId(dto.getSupplementaryId())
                .partId(dto.getPartId())
                .dropRequestDate(dto.getDropRequestDate())
                .actualDropDate(dto.getActualDropDate())
                .dropStatusId(dto.getDropStatusId())
                .remarks(dto.getRemarks())
                .draftStatus(dto.getDraftStatus())
                .build();
    }

	public static void updateEntityFromDTO(DroppingRequestDTO dto, DroppingRequest entity) {
        if (dto != null && entity != null) {
            entity.setDroppingType(dto.getDroppingType());
            entity.setSupplementaryId(dto.getSupplementaryId());
            entity.setPartId(dto.getPartId());
            entity.setDropRequestDate(dto.getDropRequestDate());
            entity.setActualDropDate(dto.getActualDropDate());
            entity.setDropStatusId(dto.getDropStatusId());
            entity.setRemarks(dto.getRemarks());
            entity.setDraftStatus(dto.getDraftStatus());
        }
    }
}
