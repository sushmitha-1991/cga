package com.dsp.cga.assurance.repository;

import java.util.List;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.entity.Code;



@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {
	Stream<Code> findByCodeTypeIdAndIsActive(Long codeTypeId, boolean isActive);

// old implenetion by samar
//    @Query("SELECT new com.dsp.master.code.Dto.CodeDTO(c.id, c.code, c.abbr, c.displaySequence) " +
//            "FROM Code c JOIN c.codeType ct " +
//            "WHERE ct.abbr = :abbr AND c.locale = :locale AND c.isActive = true")
//    List<CodeDTO> findCodesByAbbrAndLocale(@Param("abbr") String abbr, @Param("locale") String locale);

	// new Implemention
	@Query(value = "SELECT " + "c.id, " + "COALESCE(c.code_data ->> :locale, c.code_data ->> 'en') AS code, "
			+ "c.abbr, " + "c.display_sequence " + "FROM master.codes c "
			+ "JOIN master.code_types ct ON ct.id = c.code_type_id " + "WHERE ct.abbr = :abbr "
			+ "and c.is_active = true and c.is_deleted=false "
			+ "ORDER BY c.display_sequence", nativeQuery = true)
	List<Object[]> findCodesByLocaleAndAbbr(@Param("locale") String locale, @Param("abbr") String abbr);

	@Query("SELECT c.code FROM Code c WHERE c.id = :statusId")
	String findCodeById(@Param("statusId") Long statusId);


}
