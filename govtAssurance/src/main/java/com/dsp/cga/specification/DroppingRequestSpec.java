package com.dsp.cga.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.dsp.cga.assurance.droppingrequest.entity.DroppingRequest;
import com.dsp.cga.util.Utils;


public class DroppingRequestSpec {
	
	public static Specification<DroppingRequest> isActive(boolean isActive) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), isActive);
	}

	public static Specification<DroppingRequest> isFromMinistry (boolean fromMinistry) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("fromMinistry"), fromMinistry);
	}
		
	public static Specification<DroppingRequest> hasMinistryId(Long ministryId) {
		return (root, query, criteriaBuilder) -> ministryId != null
				? criteriaBuilder.equal(root.get("assurance").get("ministryId"), ministryId)
				: criteriaBuilder.conjunction();
	}
	
	public static Specification<DroppingRequest> hasMemberId(Long memberId) {
		return (root, query, criteriaBuilder) -> memberId != null
				? criteriaBuilder.equal(root.get("assurance").get("memberId"), memberId)
				: criteriaBuilder.conjunction();
	}
	
	public static Specification<DroppingRequest> hasDateBetween(LocalDate startDate, LocalDate endDate) {
		return (root, query, criteriaBuilder) -> {
			if (startDate != null && endDate != null) {
				return criteriaBuilder.between(root.get("inputDate"), startDate, endDate);
			} else if (startDate != null) {
				return criteriaBuilder.greaterThanOrEqualTo(root.get("inputDate"), startDate);
			} else if (endDate != null) {
				return criteriaBuilder.lessThanOrEqualTo(root.get("inputDate"), endDate);
			} else {
				return criteriaBuilder.conjunction();
			}
		};
	}


	public static Specification<DroppingRequest> hasStatus(String statusForMinistry) {
		return (root, query, criteriaBuilder) -> statusForMinistry != null ? criteriaBuilder.equal(root.get("statusForMinistry"), statusForMinistry)
				:  criteriaBuilder.conjunction();
	}

	
	public static Specification<DroppingRequest> hasStatus() {
		
		Specification<DroppingRequest> hasSubmittedStatus = (root, query, cb) -> 
								cb.equal(root.get("statusForMinistry"), "Submitted");

		Specification<DroppingRequest> hasReturnedStatus = (root, query, cb) ->
								cb.equal(root.get("statusForMinistry"), "Returned");

		Specification<DroppingRequest> combinedSpec = hasSubmittedStatus.or(hasReturnedStatus);
		return combinedSpec;

	}


	public static Specification<DroppingRequest> hasHouse(String houseAbbr) {
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