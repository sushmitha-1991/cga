/**
 * 
 * @author Mrinal Lakhanpal

 * @version 1.0
 */
package com.dsp.cga.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dsp.cga.assurance.dto.ApiResponse;
import com.dsp.cga.assurance.dto.SessionDTO;
import com.dsp.cga.assurance.dto.UserDetailsDTO;
import com.dsp.cga.assurance.implementationrequest.entity.ImplementationRequest;
import com.dsp.cga.assurance.repository.MyUserRepository;
import com.dsp.cga.entity.BaseUser;
import com.dsp.cga.eunms.CgaStatus;
import com.dsp.cga.eunms.MinistryStatus;
import com.dsp.cga.eunms.MopaStatus;

import jakarta.servlet.http.HttpServletRequest;



/**
 * 
 */
@Service
public class Utils {

	public final static String LOK_SABHA = "LS";
	
	public final static String RAJYA_SABHA = "RS";

	@Autowired
	private MyUserRepository myUserRepository;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${master.service.url}")
	private String masterServiceUrl;
	
	@Autowired
	private HttpServletRequest httpRequest;

	private static final Logger logger = LoggerFactory.getLogger(Utils.class);
	
	public Long getUserIdFromAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new IllegalStateException("No authenticated user found");
		}

		String username = authentication.getName();
		BaseUser baseUser = myUserRepository.findByUsername(username);
		if (baseUser == null) {
			throw new UsernameNotFoundException("Authenticated user not found in the database");
		}

		return baseUser.getId();
	}
	
	
   public static Long getHouseId() {
        Long houseId = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsDTO) {
            houseId = ((UserDetailsDTO) principal).getHouseId();
        }
        return houseId;
    }
   
	public String getHouseAbbr() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!(principal instanceof UserDetailsDTO udto)) {
			throw new RuntimeException("Authentication Problems");
		}
		return udto.getHouseAbbr();
	}
	
	public static String getStatusForMopa (String statusForMinistry, String sendTo) {
		
		String returnStatus = null;
		switch (statusForMinistry) {
		
			case "Returned" :
				returnStatus = MopaStatus.RETURNED.getValue();
				break;
			case "Submitted" :
				if (StringUtils.isEmpty(sendTo)) returnStatus = MopaStatus.RECEIVED.getValue();
				else returnStatus = MopaStatus.IN_PROGRESS.getValue();
				break;
			case "Accepted":
				returnStatus = MopaStatus.ACCEPTED.getValue();
				break;
			default:
				break;
		};
		
		return returnStatus;		
	}	
	
	public static String getStatusForCga (String statusForMinistry) {
		
		if (StringUtils.isEmpty(statusForMinistry)) return null;
		
		String returnStatus = null;
		switch (statusForMinistry) {
		
			case "Returned" :
				returnStatus = CgaStatus.RETURNED.getValue();
				break;
			case "Submitted" :
				returnStatus = CgaStatus.RECEIVED.getValue();
				break;
			case "Accepted":
				returnStatus = CgaStatus.ACCEPTED.getValue();
				break;
			case "Draft":
				returnStatus = null;
				break;	
			default:
				returnStatus = statusForMinistry; 
				break;
		};
		
		return returnStatus;		
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTokenFromRequest() {

		String authorizationHeader = httpRequest.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			return authorizationHeader.substring(7);
		}
		return null;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public SessionDTO getCurrentSession() {
	    String url = masterServiceUrl + "/session/current";
	    logger.info("URL(master): {}", url);

	    String token = getTokenFromRequest();
	    if (token == null || token.isEmpty()) {
	        logger.error("Authorization token is missing. Cannot get Session details");
	        return null;
	    }

	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Authorization", "Bearer " + token);

	    HttpEntity<String> entity = new HttpEntity<>(headers);

	    try {
	        ParameterizedTypeReference<ApiResponse<SessionDTO>> responseType =
		            new ParameterizedTypeReference<>() {};

	        ResponseEntity<ApiResponse<SessionDTO>> response = restTemplate.exchange(
	            url,
	            HttpMethod.GET,
	            entity,
	            responseType
	        );

	        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
	            SessionDTO session = response.getBody().getData();
	            logger.info("Session Fetched: {}", session);
	            return session;
	        } else {
	            logger.error("Failed to fetch session. Response: {}", response);
	        }
	    } catch (Exception e) {
	        logger.error("Exception while fetching current session", e);
	    }
	    return null; 
	}
	
	public static Boolean isTrue (Boolean value) {
		
		if (value == null) return Boolean.FALSE;
		
		return value;
	}
		
	
}
