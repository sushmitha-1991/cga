package com.dsp.cga.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dsp.cga.assurance.dto.AssuranceDTO;
import com.dsp.cga.assurance.dto.PartDetailsDTO;
import com.dsp.cga.assurance.dto.SupplementaryDTO;
import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.partdetails.entity.PartDetails;
import com.dsp.cga.assurance.supplementry.entity.Supplementary;

@Component
public class AssuranceMapper {

	@Autowired
	private SupplementaryMapper supplementaryMapper;

	// Map Assurance entity to AssuranceDTO
	public AssuranceDTO toDTO(Assurance assurance) {
		if (assurance == null) {
			return null;
		}

		List<PartDetailsDTO> partDetailsDTOs = assurance.getPartDetails().stream().map(PartDetailsMapper::toDTO)
				.collect(Collectors.toList());

		List<SupplementaryDTO> supplementaryDTOs = assurance.getSupplementary().stream().map(supplementaryMapper::toDTO)
				.collect(Collectors.toList());

		return AssuranceDTO.builder().id(assurance.getId()).lokSabhaNo(assurance.getLokSabhaNo())
				.lokSabhaSessionNo(assurance.getLokSabhaSessionNo())
				.rajyaSabhaSessionNo(assurance.getRajyaSabhaSessionNo()).assuranceDate(assurance.getAssuranceDate())
				.categoryId(assurance.getCategoryId()).questionNo(assurance.getQuestionNo())
				.assuranceTypeId(assurance.getAssuranceTypeId()).subject(assurance.getSubject())
				.memberId(assurance.getMemberId()).ministryId(assurance.getMinistryId())
				.departmentId(assurance.getDepartmentId()).pageNo(assurance.getPageNo()).fileNo(assurance.getFileNo())
				.statusId(assurance.getStatusId()).pendingDueTo(assurance.getPendingDueTo())
				.mpaRefDate(assurance.getMpaRefDate()).reportNo(assurance.getReportNo()).remarks(assurance.getRemarks())
				.dueDateOfImplementation(assurance.getDueDateOfImplementation())
				.dateOfImplementation(assurance.getDateOfImplementation())
				.detailsOfImplementation(assurance.getDetailsOfImplementation())
				.textOfAssurance(assurance.getTextOfAssurance()).supplementaryPageNo(assurance.getSupplementaryPageNo())
				.supplementaryMemberId(assurance.getSupplementaryMemberId()).draftStatus(assurance.getDraftStatus())
				.sourceId(assurance.getSourceId()).assuranceNo(assurance.getAssuranceNo())
				.supplement(assurance.getSupplement())
				.pageNoCorrected(assurance.getPageNoCorrected()).pageNoUncorrected(assurance.getPageNoUncorrected())
				.clubbedMembers(assurance.getClubbedMembers())
				.assuranceFiles(assurance.getAssuranceFiles())
				.statusId(assurance.getStatusId())

				.groupName(assurance.getGroupName()).mpaRefNo(assurance.getMpaRefNo())
				.otherPendingDueTo(assurance.getOtherPendingDueTo()).partDetails(partDetailsDTOs)
				.supplementary(supplementaryDTOs) // Set the List<SupplementaryDTO>
				.build();
	}

	// Map AssuranceDTO to Assurance entity
	public static Assurance toEntity(AssuranceDTO assuranceDTO) {
		if (assuranceDTO == null) {
			return null;
		}

		Assurance assurance = Assurance.builder().id(assuranceDTO.getId()).lokSabhaNo(assuranceDTO.getLokSabhaNo())
				.lokSabhaSessionNo(assuranceDTO.getLokSabhaSessionNo())
				.rajyaSabhaSessionNo(assuranceDTO.getRajyaSabhaSessionNo())
				.assuranceDate(assuranceDTO.getAssuranceDate()).categoryId(assuranceDTO.getCategoryId())
				.questionNo(assuranceDTO.getQuestionNo()).assuranceTypeId(assuranceDTO.getAssuranceTypeId())
				.subject(assuranceDTO.getSubject()).memberId(assuranceDTO.getMemberId())
				.ministryId(assuranceDTO.getMinistryId()).departmentId(assuranceDTO.getDepartmentId())
				.pageNo(assuranceDTO.getPageNo()).fileNo(assuranceDTO.getFileNo()).statusId(assuranceDTO.getStatusId())
				.pendingDueTo(assuranceDTO.getPendingDueTo()).mpaRefDate(assuranceDTO.getMpaRefDate())
				.reportNo(assuranceDTO.getReportNo()).remarks(assuranceDTO.getRemarks())
				.dueDateOfImplementation(assuranceDTO.getDueDateOfImplementation())
				.dateOfImplementation(assuranceDTO.getDateOfImplementation())
				.detailsOfImplementation(assuranceDTO.getDetailsOfImplementation())
				.textOfAssurance(assuranceDTO.getTextOfAssurance())
				.supplementaryPageNo(assuranceDTO.getSupplementaryPageNo())
				.supplementaryMemberId(assuranceDTO.getSupplementaryMemberId())
				.supplement(assuranceDTO.getSupplement())
				.draftStatus(assuranceDTO.getDraftStatus()).assuranceNo(assuranceDTO.getAssuranceNo())
				.pageNoCorrected(assuranceDTO.getPageNoCorrected())
				.assuranceFiles(assuranceDTO.getAssuranceFiles())
				.pageNoUncorrected(assuranceDTO.getPageNoUncorrected()).sourceId(assuranceDTO.getSourceId())
				.groupName(assuranceDTO.getGroupName()).mpaRefNo(assuranceDTO.getMpaRefNo())
				.otherPendingDueTo(assuranceDTO.getOtherPendingDueTo()).clubbedMembers(assuranceDTO.getClubbedMembers())
				.build();

		List<Long> clubbedMembers = Optional.ofNullable(assuranceDTO.getClubbedMembers())
				.orElse(Collections.emptyList());
		assurance.setClubbedMembers(clubbedMembers);
		// Handle PartDetails Mapping with Null Safety
		List<PartDetails> partDetailsEntities = Optional.ofNullable(assuranceDTO.getPartDetails())
				.orElse(Collections.emptyList()).stream().map(partDetailsDTO -> {
					PartDetails partDetails = PartDetailsMapper.toEntity(partDetailsDTO);
					partDetails.setAssurance(assurance); // Set parent reference
					return partDetails;
				}).collect(Collectors.toList());

		// Handle Supplementary Mapping with Null Safety
		List<Supplementary> supplementaryEntities = Optional.ofNullable(assuranceDTO.getSupplementary())
				.orElse(Collections.emptyList()).stream().map(supplementaryDTO -> {
					Supplementary supplementary = SupplementaryMapper.toEntity(supplementaryDTO);
					supplementary.setAssurance(assurance); // Set parent reference
					return supplementary;
				}).collect(Collectors.toList());

		// Set mapped entities in Assurance
		assurance.setPartDetails(partDetailsEntities);
		assurance.setSupplementary(supplementaryEntities);

		return assurance;

	}
	
	 public void updateAssuranceFromDTO(AssuranceDTO assuranceDTO, Assurance assurance) {
	        // Update the fields that can be changed
	        if (assuranceDTO.getLokSabhaNo() != null) {
	            assurance.setLokSabhaNo(assuranceDTO.getLokSabhaNo());
	        }
	        if (assuranceDTO.getLokSabhaSessionNo() != null) {
	            assurance.setLokSabhaSessionNo(assuranceDTO.getLokSabhaSessionNo());
	        }
	        if (assuranceDTO.getRajyaSabhaSessionNo() != null) {
	            assurance.setRajyaSabhaSessionNo(assuranceDTO.getRajyaSabhaSessionNo());
	        }
	        if (assuranceDTO.getAssuranceDate() != null) {
	            assurance.setAssuranceDate(assuranceDTO.getAssuranceDate());
	        }
	        if (assuranceDTO.getCategoryId() != null) {
	            assurance.setCategoryId(assuranceDTO.getCategoryId());
	        }
	        if (assuranceDTO.getQuestionNo() != null) {
	            assurance.setQuestionNo(assuranceDTO.getQuestionNo());
	        }
	        if (assuranceDTO.getAssuranceTypeId() != null) {
	            assurance.setAssuranceTypeId(assuranceDTO.getAssuranceTypeId());
	        }
	        if (assuranceDTO.getSubject() != null) {
	            assurance.setSubject(assuranceDTO.getSubject());
	        }
	        if (assuranceDTO.getMemberId() != null) {
	            assurance.setMemberId(assuranceDTO.getMemberId());
	        }
	        if (assuranceDTO.getMinistryId() != null) {
	            assurance.setMinistryId(assuranceDTO.getMinistryId());
	        }
	        if (assuranceDTO.getDepartmentId() != null) {
	            assurance.setDepartmentId(assuranceDTO.getDepartmentId());
	        }
	        if (assuranceDTO.getPageNo() != null) {
	            assurance.setPageNo(assuranceDTO.getPageNo());
	        }
	        if (assuranceDTO.getFileNo() != null) {
	            assurance.setFileNo(assuranceDTO.getFileNo());
	        }
	        if (assuranceDTO.getStatusId() != null) {
	            assurance.setStatusId(assuranceDTO.getStatusId());
	        }
	        if (assuranceDTO.getPendingDueTo() != null) {
	            assurance.setPendingDueTo(assuranceDTO.getPendingDueTo());
	        }
	        if (assuranceDTO.getMpaRefDate() != null) {
	            assurance.setMpaRefDate(assuranceDTO.getMpaRefDate());
	        }
	        if (assuranceDTO.getMpaRefNo() != null) {
	            assurance.setMpaRefNo(assuranceDTO.getMpaRefNo());
	        }
	        if (assuranceDTO.getReportNo() != null) {
	            assurance.setReportNo(assuranceDTO.getReportNo());
	        }
	        if (assuranceDTO.getRemarks() != null) {
	            assurance.setRemarks(assuranceDTO.getRemarks());
	        }
	        if (assuranceDTO.getDueDateOfImplementation() != null) {
	            assurance.setDueDateOfImplementation(assuranceDTO.getDueDateOfImplementation());
	        }
	        
	        if (assuranceDTO.getSupplement() != null) {
	            assurance.setSupplement(assuranceDTO.getSupplement());
	        }
	        if (assuranceDTO.getDateOfImplementation() != null) {
	            assurance.setDateOfImplementation(assuranceDTO.getDateOfImplementation());
	        }
	        if (assuranceDTO.getDetailsOfImplementation() != null) {
	            assurance.setDetailsOfImplementation(assuranceDTO.getDetailsOfImplementation());
	        }
	        if (assuranceDTO.getTextOfAssurance() != null) {
	            assurance.setTextOfAssurance(assuranceDTO.getTextOfAssurance());
	        }
	        if (assuranceDTO.getSupplementaryPageNo() != null) {
	            assurance.setSupplementaryPageNo(assuranceDTO.getSupplementaryPageNo());
	        }
	        if (assuranceDTO.getSupplementaryMemberId() != null) {
	            assurance.setSupplementaryMemberId(assuranceDTO.getSupplementaryMemberId());
	        }
	        if (assuranceDTO.getDraftStatus() != null) {
	            assurance.setDraftStatus(assuranceDTO.getDraftStatus());
	        }
	        if (assuranceDTO.getSourceId() != null) {
	            assurance.setSourceId(assuranceDTO.getSourceId());
	        }
	        if (assuranceDTO.getOtherPendingDueTo() != null) {
	            assurance.setOtherPendingDueTo(assuranceDTO.getOtherPendingDueTo());
	        }
	        if (assuranceDTO.getGroupName() != null) {
	            assurance.setGroupName(assuranceDTO.getGroupName());
	        }
	        if (assuranceDTO.getAssuranceNo() != null) {
	            assurance.setAssuranceNo(assuranceDTO.getAssuranceNo());
	        }
	        if (assuranceDTO.getPageNoCorrected() != null) {
	            assurance.setPageNoCorrected(assuranceDTO.getPageNoCorrected());
	        }
	        if (assuranceDTO.getPageNoUncorrected() != null) {
	            assurance.setPageNoUncorrected(assuranceDTO.getPageNoUncorrected());
	        }
	    }

}