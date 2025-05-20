/**
 * 
 * @author Mrinal Lakhanpal
 * @version 1.0
 */
package com.dsp.cga.assurance.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.entity.PersonalDetails;



@Repository
public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, Long> {

	@Query(value = """
			    SELECT CONCAT(bu.first_name, ' ', COALESCE(bu.middle_name, ''), ' ', bu.last_name) AS memberName,
			           bu.id AS memberId,
			           c.code AS party,
			           s.state_name AS state,
			           s.abbr AS abbr,
			           pd.mp_code AS mpCode
			    FROM authentication.base_user bu
			    JOIN members.personal_details pd ON pd.user_id = bu.id
			    JOIN master.codes c ON c.id = pd.party_id
			    JOIN master.states s ON s.id = pd.state_id
			""", nativeQuery = true)
	List<Object[]> findMembersSummary();

	@Query(value = """
		    SELECT TRIM(CONCAT(
		        COALESCE(pd.first_name, ''), ' ', 
		        COALESCE(pd.middle_name, ''), ' ', 
		        COALESCE(pd.last_name, '')
		    )) 
		    FROM members.personal_details pd
		    WHERE pd.id = :memberId
		    """, nativeQuery = true)
		String findMemberNameByMemberId(@Param("memberId") Long memberId);


	
}
