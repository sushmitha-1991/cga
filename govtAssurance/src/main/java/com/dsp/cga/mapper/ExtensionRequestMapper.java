package com.dsp.cga.mapper;

import org.springframework.stereotype.Component;

import com.dsp.cga.assurance.extensionrequest.dto.ExtensionRequestDTO;
import com.dsp.cga.assurance.extensionrequest.entity.ExtensionRequest;
@Component
public class ExtensionRequestMapper {
	
	 public static ExtensionRequestDTO toDTO(ExtensionRequest entity) {
	        if (entity == null) {
	            return null;
	        }

	        return ExtensionRequestDTO.builder()
	                .id(entity.getId())
	                .assuranceId(entity.getAssuranceId())
	                //.questionNo(entity.getQuestionNo())
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
	                .build();
	    }

	 public static void updateEntityFromDTO(ExtensionRequestDTO dto, ExtensionRequest entity) {
	        if (dto != null && entity != null) {
	          //  entity.setQuestionNo(dto.getQuestionNo());
	            entity.setExtensionNo(dto.getExtensionNo());
	            entity.setSubject(dto.getSubject());
	            entity.setExtRequestedTillDate(dto.getExtRequestedTillDate());
	            entity.setExtGrantedTillDate(dto.getExtGrantedTillDate());
	            entity.setMinistryRefNo(dto.getMinistryRefNo());
	            entity.setMinistryRefDate(dto.getMinistryRefDate());
	            entity.setRemarks(dto.getRemarks());
	            entity.setExtensionFiles(dto.getExtensionFiles());
	            entity.setDraftStatus(dto.getDraftStatus());
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
	                .statusId(dto.getStatusId())
	                .build();
	    }
	}


