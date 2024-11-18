package com.dsp.cga.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.dsp.cga.assurance.dto.AssuranceDTO;
import com.dsp.cga.assurance.dto.PartDetailsDTO;
import com.dsp.cga.assurance.dto.SupplementaryDTO;
import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.partdetails.entity.PartDetails;
import com.dsp.cga.assurance.supplementry.entity.Supplementary;
@Component
public class AssuranceMapper {
	 // Map Assurance entity to AssuranceDTO
    public static AssuranceDTO toDTO(Assurance assurance) {
        if (assurance == null) {
            return null;
        }

        List<PartDetailsDTO> partDetailsDTOs = assurance.getPartDetails().stream()
            .map(PartDetailsMapper::toDTO)
            .collect(Collectors.toList());

        List<SupplementaryDTO> supplementaryDTOs = assurance.getSupplementary().stream()
            .map(SupplementaryMapper::toDTO)
            .collect(Collectors.toList());

        return AssuranceDTO.builder()
                .id(assurance.getId())
                .lokSabhaNo(assurance.getLokSabhaNo())
                .lokSabhaSessionNo(assurance.getLokSabhaSessionNo())
                .rajyaSabhaSessionNo(assurance.getRajyaSabhaSessionNo())
                .assuranceDate(assurance.getAssuranceDate())
                .categoryId(assurance.getCategoryId())
                .questionNo(assurance.getQuestionNo())
                .assuranceTypeId(assurance.getAssuranceTypeId())
                .subject(assurance.getSubject())
                .memberId(assurance.getMemberId())
                .ministryId(assurance.getMinistryId())
                .departmentId(assurance.getDepartmentId())
                .pageNo(assurance.getPageNo())
                .fileNo(assurance.getFileNo())
                .statusId(assurance.getStatusId())
                .pendingDueTo(assurance.getPendingDueTo())
                .mpaRefDate(assurance.getMpaRefDate())
                .reportNo(assurance.getReportNo())
                .remarks(assurance.getRemarks())
                .dueDateOfImplementation(assurance.getDueDateOfImplementation())
                .dateOfImplementation(assurance.getDateOfImplementation())
                .detailsOfImplementation(assurance.getDetailsOfImplementation())
                .textOfAssurance(assurance.getTextOfAssurance())
                .supplementaryPageNo(assurance.getSupplementaryPageNo())
                .supplementaryMemberId(assurance.getSupplementaryMemberId())
                .draftStatus(assurance.getDraftStatus())
                .source(assurance.getSource())
                .assuranceNo(assurance.getAssuranceNo())
                .pageNoCorrected(assurance.getPageNoCorrected())
                .pageNoUncorrected(assurance.getPageNoUncorrected())
                .clubbedMembers(assurance.getClubbedMembers())
                .partDetails(partDetailsDTOs)
                .supplementary(supplementaryDTOs)  // Set the List<SupplementaryDTO>
                .build();
    }

    // Map AssuranceDTO to Assurance entity
    public static Assurance toEntity(AssuranceDTO assuranceDTO) {
        if (assuranceDTO == null) {
            return null;
        }

        Assurance assurance = Assurance.builder()
                .id(assuranceDTO.getId())
                .lokSabhaNo(assuranceDTO.getLokSabhaNo())
                .lokSabhaSessionNo(assuranceDTO.getLokSabhaSessionNo())
                .rajyaSabhaSessionNo(assuranceDTO.getRajyaSabhaSessionNo())
                .assuranceDate(assuranceDTO.getAssuranceDate())
                .categoryId(assuranceDTO.getCategoryId())
                .questionNo(assuranceDTO.getQuestionNo())
                .assuranceTypeId(assuranceDTO.getAssuranceTypeId())
                .subject(assuranceDTO.getSubject())
                .memberId(assuranceDTO.getMemberId())
                .ministryId(assuranceDTO.getMinistryId())
                .departmentId(assuranceDTO.getDepartmentId())
                .pageNo(assuranceDTO.getPageNo())
                .fileNo(assuranceDTO.getFileNo())
                .statusId(assuranceDTO.getStatusId())
                .pendingDueTo(assuranceDTO.getPendingDueTo())
                .mpaRefDate(assuranceDTO.getMpaRefDate())
                .reportNo(assuranceDTO.getReportNo())
                .remarks(assuranceDTO.getRemarks())
                .dueDateOfImplementation(assuranceDTO.getDueDateOfImplementation())
                .dateOfImplementation(assuranceDTO.getDateOfImplementation())
                .detailsOfImplementation(assuranceDTO.getDetailsOfImplementation())
                .textOfAssurance(assuranceDTO.getTextOfAssurance())
                .supplementaryPageNo(assuranceDTO.getSupplementaryPageNo())
                .supplementaryMemberId(assuranceDTO.getSupplementaryMemberId())
                .draftStatus(assuranceDTO.getDraftStatus())
                .assuranceNo(assuranceDTO.getAssuranceNo())
                .pageNoCorrected(assuranceDTO.getPageNoCorrected())
                .pageNoUncorrected(assuranceDTO.getPageNoUncorrected())
                .source(assuranceDTO.getSource())
                .clubbedMembers(assuranceDTO.getClubbedMembers())
                .build();

        List<Long> clubbedMembers = Optional.ofNullable(assuranceDTO.getClubbedMembers())
                .orElse(Collections.emptyList());
assurance.setClubbedMembers(clubbedMembers);
     // Handle PartDetails Mapping with Null Safety
        List<PartDetails> partDetailsEntities = Optional.ofNullable(assuranceDTO.getPartDetails())
                .orElse(Collections.emptyList())
                .stream()
                .map(partDetailsDTO -> {
                    PartDetails partDetails = PartDetailsMapper.toEntity(partDetailsDTO);
                    partDetails.setAssurance(assurance); // Set parent reference
                    return partDetails;
                })
                .collect(Collectors.toList());
		
        // Handle Supplementary Mapping with Null Safety
        List<Supplementary> supplementaryEntities = Optional.ofNullable(assuranceDTO.getSupplementary())
                .orElse(Collections.emptyList())
                .stream()
                .map(supplementaryDTO -> {
                    Supplementary supplementary = SupplementaryMapper.toEntity(supplementaryDTO);
                    supplementary.setAssurance(assurance); // Set parent reference
                    return supplementary;
                })
                .collect(Collectors.toList());

        // Set mapped entities in Assurance
        assurance.setPartDetails(partDetailsEntities);
        assurance.setSupplementary(supplementaryEntities);

        return assurance;

}
    }