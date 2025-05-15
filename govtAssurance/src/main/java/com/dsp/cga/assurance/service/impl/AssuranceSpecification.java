package com.dsp.cga.assurance.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.dsp.cga.assurance.entity.Assurance;

import jakarta.persistence.criteria.Predicate;

public class AssuranceSpecification {


	
	public static Specification<Assurance> filterByParams(Long ministryId, Long sessionNo, Long houseId) {
	    return (root, query, cb) -> {
	        List<Predicate> predicates = new ArrayList<>();

	        if (ministryId != null) {
	            predicates.add(cb.equal(root.get("ministryId"), ministryId));
	        }

	        if (houseId != null) {
	            if (houseId.equals(1L)) {
	                // Rajya Sabha
	                predicates.add(cb.isNotNull(root.get("rajyaSabhaSessionNo")));
	                if (sessionNo != null) {
	                    predicates.add(cb.equal(root.get("rajyaSabhaSessionNo"), sessionNo));
	                }
	            } else if (houseId.equals(2L)) {
	                // Lok Sabha
	                predicates.add(cb.isNotNull(root.get("lokSabhaSessionNo")));
	                predicates.add(cb.isNotNull(root.get("lokSabhaNo"))); // Optional if required
	                if (sessionNo != null) {
	                    predicates.add(cb.equal(root.get("lokSabhaSessionNo"), sessionNo));
	                }
	            }
	        }

	        return cb.and(predicates.toArray(new Predicate[0]));
	    };
	}

}
