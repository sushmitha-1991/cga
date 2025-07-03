package com.dsp.cga.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.dsp.cga.assurance.implementationrequest.entity.ImplementationRequest;
import com.dsp.cga.util.Utils;


public class ImplementationRequestSpec {
	
	public static Specification<ImplementationRequest> isActive(boolean isActive) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), isActive);
	}

	public static Specification<ImplementationRequest> isFromMinistry (boolean fromMinistry) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("fromMinistry"), fromMinistry);
	}
		
	public static Specification<ImplementationRequest> hasMinistryId(Long ministryId) {
		return (root, query, criteriaBuilder) -> ministryId != null
				? criteriaBuilder.equal(root.get("assurance").get("ministryId"), ministryId)
				: criteriaBuilder.conjunction();
	}
	
	public static Specification<ImplementationRequest> hasMemberId(Long memberId) {
		return (root, query, criteriaBuilder) -> memberId != null
				? criteriaBuilder.equal(root.get("assurance").get("memberId"), memberId)
				: criteriaBuilder.conjunction();
	}
	
	public static Specification<ImplementationRequest> hasDateBetween(LocalDate startDate, LocalDate endDate) {
		return (root, query, criteriaBuilder) -> {
			if (startDate != null && endDate != null) {
				return criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
			} else if (startDate != null) {
				return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate);
			} else if (endDate != null) {
				return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate);
			} else {
				return criteriaBuilder.conjunction();
			}
		};
	}


	public static Specification<ImplementationRequest> hasStatus(String statusForMinistry) {
		return (root, query, criteriaBuilder) -> statusForMinistry != null ? criteriaBuilder.equal(root.get("statusForMinistry"), statusForMinistry)
				:  criteriaBuilder.conjunction();
	}

	
	public static Specification<ImplementationRequest> hasStatus() {
		
		Specification<ImplementationRequest> hasSubmittedStatus = (root, query, cb) -> 
								cb.equal(root.get("statusForMinistry"), "Submitted");

		Specification<ImplementationRequest> hasReturnedStatus = (root, query, cb) ->
								cb.equal(root.get("statusForMinistry"), "Returned");

		Specification<ImplementationRequest> combinedSpec = hasSubmittedStatus.or(hasReturnedStatus);
		return combinedSpec;

	}


	public static Specification<ImplementationRequest> hasHouse(String houseAbbr) {
	    return (root, query, criteriaBuilder) -> {
	        if (Utils.LOK_SABHA.equals(houseAbbr)) {
	            return criteriaBuilder.isNotNull(root.get("assurance").get("lokSabhaSessionNo"));
	        } else if (Utils.RAJYA_SABHA.equals(houseAbbr)) {
	            return criteriaBuilder.isNotNull(root.get("assurance").get("rajyaSabhaSessionNo"));
	        } else {
	            return criteriaBuilder.conjunction(); 
	        }
	    };
	}

}