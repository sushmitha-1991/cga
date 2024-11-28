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
	                //.assuranceId(entity.getAssuranceId())
	                .questionNo(entity.getQuestionNo())
	                .extensionNo(entity.getExtensionNo())
	                .subject(entity.getSubject())
	                .extRequestedTillDate(entity.getExtRequestedTillDate())
	                .extGrantedTillDate(entity.getExtGrantedTillDate())
	                .ministryRefNo(entity.getMinistryRefNo())
	                .ministryRefDate(entity.getMinistryRefDate())
	                .remarks(entity.getRemarks())
	                .extensionFiles(entity.getExtensionFiles())
	                .draftStatus(entity.getDraftStatus())
	                .build();
	    }

	    public static ExtensionRequest toEntity(ExtensionRequestDTO dto) {
	        if (dto == null) {
	            return null;
	        }

	        return ExtensionRequest.builder()
	                .id(dto.getId())
	               // .assuranceId(dto.getAssuranceId())
	                .questionNo(dto.getQuestionNo())
	                .extensionNo(dto.getExtensionNo())
	                .subject(dto.getSubject())
	                .extRequestedTillDate(dto.getExtRequestedTillDate())
	                .extGrantedTillDate(dto.getExtGrantedTillDate())
	                .ministryRefNo(dto.getMinistryRefNo())
	                .ministryRefDate(dto.getMinistryRefDate())
	                .remarks(dto.getRemarks())
	                .extensionFiles(dto.getExtensionFiles())
	                .draftStatus(dto.getDraftStatus())
	                .build();
	    }
	}


