package com.dsp.cga.assurance.implementationrequest.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.implementationrequest.entity.ImplementationRequest;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class ImplementationRequestSpecification {

	public static Specification<ImplementationRequest> filterByCriteria(
	        Long sessionNo, LocalDate layingDate, Long implementationTypeId, Long statusId, Long ministryId, Long houseId) {

	    return (Root<ImplementationRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
	        List<Predicate> predicates = new ArrayList<>();

//	        if (sessionNo != null) {
//	            predicates.add(cb.equal(root.get("sessionNo"), sessionNo));
//	        }
	        
//	        if (sessionNo != null && houseId != null) {
//	            Join<ImplementationRequest, Assurance> assuranceJoin = root.join("assurance");
//
//	            if (houseId == 1) {
//	                predicates.add(cb.equal(assuranceJoin.get("rajyaSabhaSessionNo"), sessionNo));
//	            } else if (houseId == 2) {
//	                predicates.add(cb.equal(assuranceJoin.get("lokSabhaSessionNo"), sessionNo));
//	            }
//	        }
	        
	        if (sessionNo != null && houseId != null) {
	            Subquery<Long> subquery = query.subquery(Long.class);
	            Root<Assurance> assuranceRoot = subquery.from(Assurance.class);
	            subquery.select(assuranceRoot.get("id"));

	            if (houseId == 1) {
	                subquery.where(cb.equal(assuranceRoot.get("rajyaSabhaSessionNo"), sessionNo));
	            } else if (houseId == 2) {
	                subquery.where(cb.equal(assuranceRoot.get("lokSabhaSessionNo"), sessionNo));
	            }

	            predicates.add(cb.in(root.get("assuranceId")).value(subquery));
	        }

	        
	        if (layingDate != null) {
	            predicates.add(cb.equal(root.get("layingDate"), layingDate));
	        }
	        if (implementationTypeId != null) {
	            predicates.add(cb.equal(root.get("implementationTypeId"), implementationTypeId));
	        }
	        if (statusId != null) {
	            // Join Assurance table (assuming assuranceId is present in ImplementationRequest)
	            Root<ImplementationRequest> implRoot = root;
	            Subquery<Long> subquery = query.subquery(Long.class);
	            Root<Assurance> assuranceRoot = subquery.from(Assurance.class);
	            subquery.select(assuranceRoot.get("id"))
	                    .where(cb.equal(assuranceRoot.get("statusId"), statusId));

	            predicates.add(cb.in(root.get("assuranceId")).value(subquery));
	        }

	        if (ministryId != null) {
	            Subquery<Long> subquery = query.subquery(Long.class);
	            Root<Assurance> assuranceRoot = subquery.from(Assurance.class);
	            subquery.select(assuranceRoot.get("id"))
	                    .where(cb.equal(assuranceRoot.get("ministryId"), ministryId));
	            predicates.add(root.get("assuranceId").in(subquery));
	        }
	        return cb.and(predicates.toArray(new Predicate[0]));
	    };
	}

}
