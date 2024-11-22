package com.dsp.cga.mapper;

import com.dsp.cga.assurance.implementationrequest.dto.ImplementationRequestDTO;
import com.dsp.cga.assurance.implementationrequest.entity.ImplementationRequest;

public class ImplementationRequestMapper {
	
	public static ImplementationRequestDTO toDTO(ImplementationRequest entity) {
        if (entity == null) {
            return null;
        }

        return ImplementationRequestDTO.builder()
                .id(entity.getId())
                .assuranceId(entity.getAssuranceId())
                .committeeDecisionId(entity.getCommitteeDecisionId())
                .layingDate(entity.getLayingDate())
                .statementNo(entity.getStatementNo())
                .supplementaryId(entity.getSupplementaryId())
                .partId(entity.getPartId())
                .reason(entity.getReason())
                .implementationReportFiles(entity.getImplementationReportFiles())
                .partReportFiles(entity.getPartReportFiles())
                .otherFiles(entity.getOtherFiles())
                .build();
    }

    public static ImplementationRequest toEntity(ImplementationRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return ImplementationRequest.builder()
                .id(dto.getId())
                .assuranceId(dto.getAssuranceId())
                .committeeDecisionId(dto.getCommitteeDecisionId())
                .layingDate(dto.getLayingDate())
                .statementNo(dto.getStatementNo())
                .supplementaryId(dto.getSupplementaryId())
                .partId(dto.getPartId())
                .reason(dto.getReason())
                .implementationReportFiles(dto.getImplementationReportFiles())
                .partReportFiles(dto.getPartReportFiles())
                .otherFiles(dto.getOtherFiles())
                .build();
    }
}

