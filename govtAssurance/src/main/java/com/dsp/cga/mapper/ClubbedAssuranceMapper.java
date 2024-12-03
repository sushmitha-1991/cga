package com.dsp.cga.mapper;

import com.dsp.cga.assurance.clubbedassurance.dto.ClubbedAssuranceDTO;
import com.dsp.cga.assurance.clubbedassurance.entity.ClubbedAssurance;

public class ClubbedAssuranceMapper {

	
	 public static ClubbedAssuranceDTO toDTO(ClubbedAssurance entity) {
	        if (entity == null) return null;

	        return ClubbedAssuranceDTO.builder()
	                .id(entity.getId())
	                .assuranceId(entity.getAssuranceId())
	                .clubbedAssuranceId(entity.getClubbedAssuranceId())
	               
	                .build();
	    }

	    public static ClubbedAssurance toEntity(ClubbedAssuranceDTO dto) {
	        if (dto == null) return null;

	        return ClubbedAssurance.builder()
	                .id(dto.getId())
	                .assuranceId(dto.getAssuranceId())
	                .clubbedAssuranceId(dto.getClubbedAssuranceId())
	                
	                .build();
	    }
	    
	    public static void updateEntityFromDTO(ClubbedAssuranceDTO dto, ClubbedAssurance entity) {
	        if (dto.getAssuranceId() != null) {
	            entity.setAssuranceId(dto.getAssuranceId());
	        }
	        if (dto.getClubbedAssuranceId() != null) {
	            entity.setClubbedAssuranceId(dto.getClubbedAssuranceId());
	        }
	       
	        // Add other fields here if needed.
	    }
}
