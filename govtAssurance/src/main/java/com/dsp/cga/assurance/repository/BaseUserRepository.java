package com.dsp.cga.assurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dsp.cga.entity.BaseUser;


@Repository
public interface BaseUserRepository extends JpaRepository<BaseUser, Long> {
    @Query(value = "select id from master.houses where abbr=:abbr",
    nativeQuery = true)
Integer fetchHouseIdByAbbr(String abbr);
}
