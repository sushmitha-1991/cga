package com.dsp.cga.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.dsp.cga.assurance.otherRequests.entity.OtherRequests;
import com.dsp.cga.util.Utils;


public class OtherRequestSpec {
	
	public static Specification<OtherRequests> isActive(boolean isActive) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), isActive);
	}

	public static Specification<OtherRequests> isFromMinistry (boolean fromMinistry) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("fromMinistry"), fromMinistry);
	}
		
	public static Specification<OtherRequests> hasMinistryId(Long ministryId) {
		return (root, query, criteriaBuilder) -> ministryId != null
				? criteriaBuilder.equal(root.get("assurance").get("ministryId"), ministryId)
				: criteriaBuilder.conjunction();
	}
	
	public static Specification<OtherRequests> hasMemberId(Long memberId) {
		return (root, query, criteriaBuilder) -> memberId != null
				? criteriaBuilder.equal(root.get("assurance").get("memberId"), memberId)
				: criteriaBuilder.conjunction();
	}
	
	public static Specification<OtherRequests> hasDateBetween(LocalDate startDate, LocalDate endDate) {
		return (root, query, criteriaBuilder) -> {
			if (startDate != null && endDate != null) {
				return criteriaBuilder.between(root.get("requestedDate"), startDate, endDate);
			} else if (startDate != null) {
				return criteriaBuilder.greaterThanOrEqualTo(root.get("requestedDate"), startDate);
			} else if (endDate != null) {
				return criteriaBuilder.lessThanOrEqualTo(root.get("requestedDate"), endDate);
			} else {
				return null;
			}
		};
	}


	public static Specification<OtherRequests> hasStatus(String statusForMinistry) {
		return (root, query, criteriaBuilder) -> statusForMinistry != null ? criteriaBuilder.equal(root.get("statusForMinistry"), statusForMinistry)
				: null;
	}

	
	public static Specification<OtherRequests> hasStatus() {
		
		Specification<OtherRequests> hasSubmittedStatus = (root, query, cb) -> 
								cb.equal(root.get("statusForMinistry"), "Submitted");

		Specification<OtherRequests> hasReturnedStatus = (root, query, cb) ->
								cb.equal(root.get("statusForMinistry"), "Returned");

		Specification<OtherRequests> combinedSpec = hasSubmittedStatus.or(hasReturnedStatus);
		return combinedSpec;

	}


	public static Specification<OtherRequests> hasHouse(String houseAbbr) {
	    return (root, query, criteriaBuilder) -> {
	        if (Utils.LOK_SABHA.equals(houseAbbr)) {
	            return criteriaBuilder.isNotNull(root.get("assurance").get("lokSabhaSessionNo"));
	        } else if (Utils.RAJYA_SABHA.equals(houseAbbr)) {
	            return criteriaBuilder.isNotNull(root.get("assurance").get("rajyaSabhaSessionNo"));
	        } else {
	            return criteriaBuilder.conjunction(); // always true
	        }
	    };
	}

}