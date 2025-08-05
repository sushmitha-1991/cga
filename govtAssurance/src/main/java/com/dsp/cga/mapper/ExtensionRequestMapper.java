package com.dsp.cga.mapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.dsp.cga.assurance.extensionrequest.dto.ExtensionRequestDTO;
import com.dsp.cga.assurance.extensionrequest.entity.ExtensionRequest;
import com.dsp.cga.assurance.implementationrequest.dto.ImplementationRequestDTO;
import com.dsp.cga.eunms.CgaStatus;
import com.dsp.cga.eunms.MinistryStatus;
import com.dsp.cga.util.Utils;
@Component
public class ExtensionRequestMapper {
	
	 public static ExtensionRequestDTO toDTO(ExtensionRequest entity) {
	        if (entity == null) {
	            return null;
	        }

	        return ExtensionRequestDTO.builder()
	                .id(entity.getId())
	                .assuranceId(entity.getAssuranceId())
	                .extensionNo(entity.getExtensionNo())
	                .subject(entity.getSubject())
	                .extRequestedTillDate(entity.getExtRequestedTillDate())
	                .extGrantedTillDate(entity.getExtGrantedTillDate())
	                .ministryRefNo(entity.getMinistryRefNo())
	                .ministryRefDate(entity.getMinistryRefDate())
	                .remarks(entity.getRemarks())
	                .extensionFiles(entity.getExtensionFiles())
	                .draftStatus(entity.getDraftStatus())
	                .statusId(entity.getStatusId())
	                .statusForMinistry(entity.getStatusForMinistry())
	                .statusForCga(entity.getStatusForCga())
	                 .build();
	    }

	 public static void updateEntityFromDTO(ExtensionRequestDTO dto, ExtensionRequest entity) {
	        if (dto != null && entity != null) {
	            entity.setExtensionNo(dto.getExtensionNo());
	            entity.setSubject(dto.getSubject());
	            entity.setExtRequestedTillDate(dto.getExtRequestedTillDate());
	            entity.setExtGrantedTillDate(dto.getExtGrantedTillDate());
	            entity.setMinistryRefNo(dto.getMinistryRefNo());
	            entity.setMinistryRefDate(dto.getMinistryRefDate());
	            entity.setRemarks(dto.getRemarks());
	            entity.setExtensionFiles(dto.getExtensionFiles());
	            entity.setDraftStatus(dto.getDraftStatus());

	            // If Accepted, Status not to be updated. Else update. 
	            if (!CgaStatus.ACCEPTED.getValue().equals(entity.getStatusForCga())) {
		            entity.setStatusForMinistry(dto.getDraftStatus() ? MinistryStatus.DRAFT.getValue() : MinistryStatus.SUBMITTED.getValue());
					entity.setStatusForCga(dto.getDraftStatus() ? null : Utils.getStatusForCga(MinistryStatus.SUBMITTED.getValue()));
	            }
	            
	            entity.setStatusId(dto.getStatusId());
	        }
	    }
	    public static ExtensionRequest toEntity(ExtensionRequestDTO dto) {
	        if (dto == null) {
	            return null;
	        }

	        return ExtensionRequest.builder()
	                .id(dto.getId())
	                .assuranceId(dto.getAssuranceId())
	              //  .questionNo(dto.getQuestionNo())
	                .extensionNo(dto.getExtensionNo())
	                .subject(dto.getSubject())
	                .extRequestedTillDate(dto.getExtRequestedTillDate())
	                .extGrantedTillDate(dto.getExtGrantedTillDate())
	                .ministryRefNo(dto.getMinistryRefNo())
	                .ministryRefDate(dto.getMinistryRefDate())
	                .remarks(dto.getRemarks())
	                .extensionFiles(dto.getExtensionFiles())
	                .draftStatus(dto.getDraftStatus())
	                .extensionFiles(dto.getExtensionFiles())
	                .fromMinistry(true)
		             .statusForMinistry(dto.getDraftStatus() ? MinistryStatus.DRAFT.getValue() : MinistryStatus.SUBMITTED.getValue())
					.statusForCga(dto.getDraftStatus() ? null : Utils.getStatusForCga(MinistryStatus.SUBMITTED.getValue()))
	                .statusId(dto.getStatusId())
	                .build();
	    }
	}


