package com.dsp.cga.mapper;

import org.springframework.stereotype.Component;

import com.dsp.cga.assurance.droppingrequest.dto.DroppingRequestDTO;
import com.dsp.cga.assurance.droppingrequest.entity.DroppingRequest;

@Component
public class DroppingRequestMapper {

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
	                .ministryRefNo(dto.getMinistryRefNo())
	                .ministryRefDate(dto.getMinistryRefDate())
	                .acceptedId(dto.getAcceptedId())
	                .subject(dto.getSubject())
	                .remarks(dto.getRemarks())
	                .meetingDate(dto.getMeetingDate())
	                .meetingNo(dto.getMeetingNo())
	                .droppingFiles(dto.getDroppingFiles())
	                .draftStatus(dto.getDraftStatus())
	                .type(dto.getType())
	                .issueLetterMinistry(dto.getIssueLetterMinistry())
	                .communicationDate(dto.getCommunicationDate())
	             
	                .session(dto.getSession())
	                .build();
	    }

	    // Convert DroppingRequest entity to DroppingRequestDTO
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
	                .ministryRefNo(entity.getMinistryRefNo())
	                .ministryRefDate(entity.getMinistryRefDate())
	                .acceptedId(entity.getAcceptedId())
	                .subject(entity.getSubject())
	                .remarks(entity.getRemarks())
	                .meetingDate(entity.getMeetingDate())
	                .meetingNo(entity.getMeetingNo())
	                .droppingFiles(entity.getDroppingFiles())
	                .draftStatus(entity.getDraftStatus())
	                .communicationDate(entity.getCommunicationDate())
	                .issueLetterMinistry(entity.getIssueLetterMinistry())
	                .type(entity.getType())
	                .session(entity.getSession())
	                .build();
	    }

	    public static void updateEntityFromDTO(DroppingRequestDTO dto, DroppingRequest entity) {
	        if (dto != null && entity != null) {
	            entity.setDroppingType(dto.getDroppingType());
	            entity.setSupplementaryId(dto.getSupplementaryId());
	            entity.setPartId(dto.getPartId());
	            entity.setDropRequestDate(dto.getDropRequestDate());
	            entity.setActualDropDate(dto.getActualDropDate());
	            entity.setAcceptedId(dto.getAcceptedId());
	            entity.setMinistryRefDate(dto.getMinistryRefDate());
	            entity.setMinistryRefNo(dto.getMinistryRefNo());
	            entity.setSubject(dto.getSubject());
	            entity.setMeetingNo(dto.getMeetingNo());
	            entity.setMeetingDate(dto.getMeetingDate());
	            entity.setDroppingFiles(dto.getDroppingFiles());
	            entity.setIssueLetterMinistry(dto.getIssueLetterMinistry());
	           entity.setCommunicationDate(dto.getCommunicationDate());
	           entity.setDroppingFiles(dto.getDroppingFiles());
	            entity.setSession(dto.getSession());
	            entity.setRemarks(dto.getRemarks());
	        }
	    }

}
