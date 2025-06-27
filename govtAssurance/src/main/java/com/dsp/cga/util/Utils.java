/**
 * 
 * @author Mrinal Lakhanpal

 * @version 1.0
 */
package com.dsp.cga.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.dsp.cga.assurance.dto.UserDetailsDTO;
import com.dsp.cga.assurance.repository.MyUserRepository;
import com.dsp.cga.entity.BaseUser;



/**
 * 
 */
@Service
public class Utils {

	public final static String LOK_SABHA = "LS";
	
	public final static String RAJYA_SABHA = "RS";

	@Autowired
	private MyUserRepository myUserRepository;

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
	
}
