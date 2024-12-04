package com.dsp.cga.mapper;

import org.springframework.stereotype.Component;

import com.dsp.cga.assurance.dto.AssuranceListDTO;
import com.dsp.cga.assurance.entity.Assurance;

@Component
public class AssuranceListMapper {
	
	public static AssuranceListDTO toDto(Assurance assurance) {
        if (assurance == null) {
            return null;
        }
        return AssuranceListDTO.builder()
                .id(assurance.getId())
                .assuranceNo(assurance.getAssuranceNo())
                .subject(assurance.getSubject())
                .build();
    }

}
