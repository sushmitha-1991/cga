package com.dsp.cga.mapper;

import org.springframework.stereotype.Component;

import com.dsp.cga.assurance.dto.SupplementaryDTO;
import com.dsp.cga.assurance.supplementry.entity.Supplementary;
@Component
public class SupplementaryMapper {

	 public static SupplementaryDTO toDTO(Supplementary supplementary) {
	        return SupplementaryDTO.builder()
	                .id(supplementary.getId())
	                .supp(supplementary.getSupp())
	                .memberId(supplementary.getMemberId())
	                .clubbedMembers(supplementary.getClubbedMembers())
	                .part(supplementary.getPart())
	                .subject(supplementary.getSubject())
	                .statusId(supplementary.getStatusId())
	                .extentOfAssurance(supplementary.getExtentOfAssurance())
	                .build();
	    }

	    public static Supplementary toEntity(SupplementaryDTO supplementaryDTO) {
	        if (supplementaryDTO == null) {
	            return null;
	        }

	        return Supplementary.builder()
	                .id(supplementaryDTO.getId())
	                .supp(supplementaryDTO.getSupp())
	                .memberId(supplementaryDTO.getMemberId())
	                .clubbedMembers(supplementaryDTO.getClubbedMembers())
	                .part(supplementaryDTO.getPart())
	                .subject(supplementaryDTO.getSubject())
	                .statusId(supplementaryDTO.getStatusId())
	                .extentOfAssurance(supplementaryDTO.getExtentOfAssurance())
	                .build();
	    }
	}