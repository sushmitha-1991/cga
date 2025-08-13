package com.dsp.cga.mapper;

import com.dsp.cga.assurance.clubbedassurance.dto.ClubbedAssuranceDTO;
import com.dsp.cga.assurance.clubbedassurance.entity.ClubbedAssurance;
import com.dsp.cga.assurance.dto.CommonRequestResponseDTO;
import com.dsp.cga.eunms.CgaStatus;
import com.dsp.cga.eunms.MinistryStatus;
import com.dsp.cga.util.Utils;

public class ClubbedAssuranceMapper {

	
	 public static ClubbedAssuranceDTO toDTO(ClubbedAssurance entity) {
	        if (entity == null) return null;

	        return ClubbedAssuranceDTO.builder()
	                .id(entity.getId())
	                .assuranceId(entity.getAssuranceId())
	                .clubbedAssuranceIds(entity.getClubbedAssuranceIds())
	                .date(entity.getDate())
	                .fromMinistry(entity.getFromMinistry())
	                .statusForCga(entity.getStatusForCga())
	                .statusForMinistry(entity.getStatusForMinistry())
	                .build();
	    }

	    public static ClubbedAssurance toEntity(ClubbedAssuranceDTO dto) {
	        if (dto == null) return null;

	        return ClubbedAssurance.builder()
	                .id(dto.getId())
	                .assuranceId(dto.getAssuranceId())
	                .clubbedAssuranceIds(dto.getClubbedAssuranceIds())
	                .date(dto.getDate())
					.fromMinistry(true)
					.statusForMinistry(dto.getDraftStatus() ? MinistryStatus.DRAFT.getValue() : MinistryStatus.SUBMITTED.getValue())
					.statusForCga(dto.getDraftStatus() ? null : Utils.getStatusForCga(MinistryStatus.SUBMITTED.getValue()))
	                .build();
	    }
	    
	    public static void updateEntityFromDTO(ClubbedAssuranceDTO dto, ClubbedAssurance entity) {
	        if (dto.getAssuranceId() != null) {
	            entity.setAssuranceId(dto.getAssuranceId());
	        }
	        
	        entity.setDate(dto.getDate());
	        
	        if (dto.getClubbedAssuranceIds() != null) {
	            entity.setClubbedAssuranceIds(dto.getClubbedAssuranceIds());
	        }
	       
	        // Add other fields here if needed.
	        entity.setDraftStatus(dto.getDraftStatus());
	        if (MinistryStatus.RETURNED.getValue().equals(dto.getStatusForMinistry()) || 
	        		CgaStatus.RETURNED.getValue().equals(dto.getStatusForMinistry())) {
	        	entity.setStatusForMinistry(MinistryStatus.RETURNED.getValue());
	        	entity.setStatusForCga(MinistryStatus.RETURNED.getValue());
	        } else {
	        	entity.setStatusForMinistry(dto.getDraftStatus() ? MinistryStatus.DRAFT.getValue() : MinistryStatus.SUBMITTED.getValue());
	        	entity.setStatusForCga(dto.getDraftStatus() ? null : Utils.getStatusForCga(MinistryStatus.SUBMITTED.getValue()));
	        }	
	        
	        if (!CgaStatus.ACCEPTED.getValue().equals(entity.getStatusForCga())) {
	            entity.setStatusForMinistry(dto.getDraftStatus() ? MinistryStatus.DRAFT.getValue() : MinistryStatus.SUBMITTED.getValue());
				entity.setStatusForCga(dto.getDraftStatus() ? null : Utils.getStatusForCga(MinistryStatus.SUBMITTED.getValue()));
	        }	        
	  }
	    
	  public static void updateEntityForReturnOrAccept(CommonRequestResponseDTO dto, ClubbedAssurance entity) {
		  
		  if (!MinistryStatus.RETURNED.getValue().equals(dto.getStatusForMinistry()) 
			  && !CgaStatus.ACCEPTED.getValue().equals(dto.getStatusForMinistry())) 
			  return;
		  
		  if (MinistryStatus.RETURNED.getValue().equals(dto.getStatusForMinistry())) {
			  
			  entity.setStatusForMinistry(MinistryStatus.RETURNED.getValue());
			  entity.setStatusForCga(MinistryStatus.RETURNED.getValue());
			  entity.setReturnRemarks(entity.getReturnRemarks());
			  return;
		  }

		  if (MinistryStatus.ACCEPTED.getValue().equals(dto.getStatusForMinistry())) {
			  
			  entity.setStatusForMinistry(MinistryStatus.ACCEPTED.getValue());
			  entity.setStatusForCga(MinistryStatus.ACCEPTED.getValue());
			  return;
		  }

	  }
	  
}
