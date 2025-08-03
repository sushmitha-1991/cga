package com.dsp.cga.specification;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.dsp.cga.assurance.implementationrequest.entity.ImplementationRequest;
import com.dsp.cga.eunms.CgaStatus;
import com.dsp.cga.eunms.Entities;
import com.dsp.cga.eunms.MinistryStatus;
import com.dsp.cga.eunms.MopaStatus;
import com.dsp.cga.util.Utils;

import jakarta.persistence.criteria.Expression;


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

			Expression<java.sql.Date> createdAtDate = criteriaBuilder.function(
				    "date", java.sql.Date.class, root.get("createdAt"));
			
			if (startDate != null && endDate != null) {				
				 return criteriaBuilder.between(
					        createdAtDate, java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate)
					    );				
			} else if (startDate != null) {
				return criteriaBuilder.greaterThanOrEqualTo(createdAtDate, java.sql.Date.valueOf(startDate));
			} else if (endDate != null) {
			    return criteriaBuilder.lessThanOrEqualTo(createdAtDate, java.sql.Date.valueOf(endDate));
			} else {
				return criteriaBuilder.conjunction();
			}
		};
	}
	


	public static Specification<ImplementationRequest> hasStatus(String status, String statusFor) {
		
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
		
		if (Entities.MOPA.getValue().equalsIgnoreCase(statusFor)) {
			
			if (MopaStatus.RETURNED_TO_MINISTRY.getValue().equalsIgnoreCase(status)) {
				System.out.println("RETURNED TO MINISTRY");
				Specification<ImplementationRequest> hasReturnedStatus = (root, query, cb) -> 
									cb.equal(root.get("statusForMinistry"), MinistryStatus.RETURNED.getValue());
				
				System.out.println(hasReturnedStatus);
				return hasReturnedStatus;					
			}
			
			if (MopaStatus.RETURNED.getValue().equals(status)) {
				// RETURNED - if Returned from CGA, to be selected
				// RETURNED - If Returned by MoPA to Ministry, not to be selected
				Specification<ImplementationRequest> hasReturnedStatus = (root, query, cb) ->
							cb.equal(root.get("statusForMopa"), MopaStatus.RETURNED.getValue());
							
				Specification<ImplementationRequest> hasMinistryStatus = (root, query, cb) ->
							cb.equal(root.get("statusForCga"), CgaStatus.RETURNED.getValue());
				
				return hasReturnedStatus.and(hasMinistryStatus);
			} 
			
			return (root, query, criteriaBuilder) -> status != null 
						? criteriaBuilder.equal(root.get("statusForMopa"), status)
						: criteriaBuilder.conjunction();		
		}
		
		return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
	}

	
	public static Specification<ImplementationRequest> hasStatus(String statusFor) {
		
		statusFor = StringUtils.isEmpty(statusFor) ? Entities.MINISTRY.getValue() : statusFor;
		
		if (Entities.MOPA.getValue().equalsIgnoreCase(statusFor)) {
			
			Specification<ImplementationRequest> hasReceivedStatus = (root, query, cb) -> 
			cb.equal(root.get("statusForMopa"), MopaStatus.RECEIVED.getValue());

			Specification<ImplementationRequest> hasProgressStatus = (root, query, cb) -> 
			cb.equal(root.get("statusForMopa"), MopaStatus.IN_PROGRESS.getValue());

			// RETURNED - if Returned from CGA, to be selected
			// RETURNED - If Returned by MoPA to Ministry, not to be selected. For this we check if CGA status is RETURNED
			Specification<ImplementationRequest> hasReturnedStatus = (root, query, cb) ->
						cb.equal(root.get("statusForMopa"), MopaStatus.RETURNED.getValue());
						
			Specification<ImplementationRequest> hasMinistryStatus = (root, query, cb) ->
						cb.equal(root.get("statusForCga"), CgaStatus.RETURNED.getValue());
			
			Specification<ImplementationRequest> returnedSpec = hasReturnedStatus.and(hasMinistryStatus);
						
			Specification<ImplementationRequest> combinedSpec = hasReceivedStatus
																	.or(hasProgressStatus)
																	.or(returnedSpec);
			return combinedSpec;
			
		} 
		
		if (Entities.CGA.getValue().equalsIgnoreCase(statusFor)) {

			Specification<ImplementationRequest> hasReceivedStatus = (root, query, cb) -> 
			cb.equal(root.get("statusForCga"), CgaStatus.RECEIVED.getValue());

			Specification<ImplementationRequest> hasReturnedStatus = (root, query, cb) ->
						cb.equal(root.get("statusForCga"), CgaStatus.RETURNED.getValue());
			
			Specification<ImplementationRequest> combinedSpec = hasReceivedStatus.or(hasReturnedStatus);
			return combinedSpec;
		
		} 
		
		if (Entities.MINISTRY.getValue().equalsIgnoreCase(statusFor)) {

			Specification<ImplementationRequest> hasSubmittedStatus = (root, query, cb) -> 
			cb.equal(root.get("statusForMinistry"), MinistryStatus.SUBMITTED.getValue());

			Specification<ImplementationRequest> hasReturnedStatus = (root, query, cb) ->
						cb.equal(root.get("statusForMinistry"), MinistryStatus.RETURNED.getValue());
			
			Specification<ImplementationRequest> combinedSpec = hasSubmittedStatus.or(hasReturnedStatus);
			return combinedSpec;
		}	
		
		return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
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