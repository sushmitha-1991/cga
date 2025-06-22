package com.dsp.cga.mapper;

import java.util.Comparator;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.dsp.cga.assurance.dto.AssuranceListDTO;
import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.partdetails.entity.PartDetails;

@Component
public class AssuranceListMapper {
	
	public static AssuranceListDTO toDto(Assurance assurance) {
        if (assurance == null) {
            return null;
        }
        return AssuranceListDTO.builder()
                .id(assurance.getId())
                .assuranceNo(assurance.getAssuranceNo())
                .subject(assurance.getSubject() != null 
                ? assurance.getSubject() 
                : (assurance.getPartDetails() != null && !assurance.getPartDetails().isEmpty()
                    ? assurance.getPartDetails().stream()
                        .sorted(Comparator.comparing(PartDetails::getId).reversed()) // latest by ID
                        .map(PartDetails::getSubject)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(null)
                    : null))
                .build();
    }

}
