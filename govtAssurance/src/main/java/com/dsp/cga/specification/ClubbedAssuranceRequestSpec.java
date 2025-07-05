package com.dsp.cga.specification;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.dsp.cga.assurance.clubbedassurance.entity.ClubbedAssurance;
import com.dsp.cga.assurance.otherRequests.entity.OtherRequests;
import com.dsp.cga.eunms.CgaStatus;
import com.dsp.cga.eunms.Entities;
import com.dsp.cga.eunms.MinistryStatus;
import com.dsp.cga.util.Utils;


public class ClubbedAssuranceRequestSpec {
	
	public static Specification<ClubbedAssurance> isActive(boolean isActive) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), isActive);
	}

	public static Specification<ClubbedAssurance> isFromMinistry (boolean fromMinistry) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("fromMinistry"), fromMinistry);
	}
		
	public static Specification<ClubbedAssurance> hasMinistryId(Long ministryId) {
		return (root, query, criteriaBuilder) -> ministryId != null
				? criteriaBuilder.equal(root.get("assurance").get("ministryId"), ministryId)
				: criteriaBuilder.conjunction();
	}
	
	public static Specification<ClubbedAssurance> hasMemberId(Long memberId) {
		return (root, query, criteriaBuilder) -> memberId != null
				? criteriaBuilder.equal(root.get("assurance").get("memberId"), memberId)
				: criteriaBuilder.conjunction();
	}
	
	public static Specification<ClubbedAssurance> hasDateBetween(LocalDate startDate, LocalDate endDate) {
		return (root, query, criteriaBuilder) -> {
			if (startDate != null && endDate != null) {
				return criteriaBuilder.between(root.get("date"), startDate, endDate);
			} else if (startDate != null) {
				return criteriaBuilder.greaterThanOrEqualTo(root.get("date"), startDate);
			} else if (endDate != null) {
				return criteriaBuilder.lessThanOrEqualTo(root.get("date"), endDate);
			} else {
				return null;
			}
		};
	}


	public static Specification<ClubbedAssurance> hasStatus(String status, String statusFor) {

		statusFor = StringUtils.isEmpty(statusFor) ? Entities.MINISTRY.getValue() : statusFor;
		
		if (Entities.CGA.getValue().equalsIgnoreCase(statusFor)) {
			return (root, query, criteriaBuilder) -> status != null 
						? criteriaBuilder.equal(root.get("statusForCga"), status)
						: criteriaBuilder.conjunction();		
		}
		
		if (Entities.MINISTRY.getValue().equalsIgnoreCase(statusFor)) {
			return (root, query, criteriaBuilder) -> status != null 
					? criteriaBuilder.equal(root.get("statusForMinistry"), status)
					: criteriaBuilder.conjunction();
		}
		
		return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
	}

	
	public static Specification<ClubbedAssurance> hasStatus(String statusFor) {
		
		statusFor = StringUtils.isEmpty(statusFor) ? Entities.MINISTRY.getValue() : statusFor;
		
		if (Entities.CGA.getValue().equalsIgnoreCase(statusFor)) {

			Specification<ClubbedAssurance> hasReceivedStatus = (root, query, cb) -> 
			cb.equal(root.get("statusForCga"), CgaStatus.RECEIVED.getValue());

			Specification<ClubbedAssurance> hasReturnedStatus = (root, query, cb) ->
						cb.equal(root.get("statusForCga"), CgaStatus.RETURNED.getValue());
			
			Specification<ClubbedAssurance> combinedSpec = hasReceivedStatus.or(hasReturnedStatus);
			return combinedSpec;
			
		}
		
		if (Entities.MINISTRY.getValue().equalsIgnoreCase(statusFor)) {

			Specification<ClubbedAssurance> hasSubmittedStatus = (root, query, cb) -> 
			cb.equal(root.get("statusForMinistry"), MinistryStatus.SUBMITTED.getValue());

			Specification<ClubbedAssurance> hasReturnedStatus = (root, query, cb) ->
						cb.equal(root.get("statusForMinistry"), MinistryStatus.RETURNED.getValue());
			
			Specification<ClubbedAssurance> combinedSpec = hasSubmittedStatus.or(hasReturnedStatus);
			return combinedSpec;
		} 
		
		return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

	}


	public static Specification<ClubbedAssurance> hasHouse(String houseAbbr) {
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