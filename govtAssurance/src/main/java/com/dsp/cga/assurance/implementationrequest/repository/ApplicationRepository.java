/**
 * 
 * @author Mrinal Lakhanpal
 * @version 1.0
 */
package com.dsp.cga.assurance.implementationrequest.repository;
import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.implementationrequest.entity.Application;



/**
 * 
 */
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

	 Optional<Application> findByAbbr(String abbr);

	


}

