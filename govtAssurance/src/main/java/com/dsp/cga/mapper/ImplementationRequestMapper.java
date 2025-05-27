package com.dsp.cga.mapper;

import org.springframework.stereotype.Component;

import com.dsp.cga.assurance.dto.AssuranceDTO;
import com.dsp.cga.assurance.implementationrequest.dto.ImplementationRequestDTO;
import com.dsp.cga.assurance.implementationrequest.entity.ImplementationRequest;

@Component
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
                .draftStatus(entity.getDraftStatus())
               .implementationReportFiles(entity.getImplementationReportFiles())
                .partReportFiles(entity.getPartReportFiles())
                .dueDateOfImplementation(entity.getDueDateOfImplementation())
                .dateOfImplementation(entity.getDateOfImplementation())
                .detailsOfImplementation(entity.getDetailsOfImplementation())
                .remarks(entity.getRemarks())
               .otherFiles(entity.getOtherFiles())
              .implementationTypeId(entity.getImplementationTypeId())
               .session(entity.getSession())
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
                .dueDateOfImplementation(dto.getDueDateOfImplementation())
                .detailsOfImplementation(dto.getDetailsOfImplementation())
                .dateOfImplementation(dto.getDateOfImplementation())
                .remarks(dto.getRemarks())
                .draftStatus(dto.getDraftStatus())
                .implementationReportFiles(dto.getImplementationReportFiles())
                .partReportFiles(dto.getPartReportFiles())
                .otherFiles(dto.getOtherFiles())
                .implementationTypeId(dto.getImplementationTypeId())
                .session(dto.getSession())
                .applicationId(dto.getApplicationId())
                .fromMinistry(true)
	            .statusForMinistry("Submitted")
                .build();
    }
    
    public static void updateEntityFromDTO(ImplementationRequestDTO dto, ImplementationRequest entity) {
        if (dto == null || entity == null) {
            return;
        }

        // Update fields that are modifiable
        entity.setCommitteeDecisionId(dto.getCommitteeDecisionId());
        entity.setLayingDate(dto.getLayingDate());
        entity.setStatementNo(dto.getStatementNo());
        entity.setSupplementaryId(dto.getSupplementaryId());
        entity.setPartId(dto.getPartId());
        entity.setReason(dto.getReason());
        entity.setDraftStatus(dto.getDraftStatus());
        entity.setImplementationReportFiles(dto.getImplementationReportFiles());
        entity.setPartReportFiles(dto.getPartReportFiles());
        entity.setSession(dto.getSession());
        entity.setImplementationTypeId(dto.getImplementationTypeId());
        entity.setOtherFiles(dto.getOtherFiles());
        entity.setDateOfImplementation(dto.getDateOfImplementation());
        entity.setDetailsOfImplementation(dto.getDetailsOfImplementation());
        entity.setDueDateOfImplementation(dto.getDueDateOfImplementation());
        entity.setRemarks(dto.getRemarks());
        
        
        // Note: createdBy and createdDate will remain unchanged as per JPA logic
    }
    
    public static ImplementationRequest toImplementationRequest(AssuranceDTO dto) {
        if (dto == null) return null;

        return ImplementationRequest.builder()
            // assuranceId will be set later after saving Assurance entity
            .dueDateOfImplementation(dto.getDueDateOfImplementation())
            .dateOfImplementation(dto.getDateOfImplementation())
            .detailsOfImplementation(dto.getDetailsOfImplementation())
            .remarks(dto.getRemarks())
            .draftStatus(false) // or use dto.getDraftStatus() if needed
            .build();
    }

}

