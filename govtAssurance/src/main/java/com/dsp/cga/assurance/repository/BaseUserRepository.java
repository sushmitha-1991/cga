package com.dsp.cga.assurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.cga.entity.BaseUser;


@Repository
public interface BaseUserRepository extends JpaRepository<BaseUser, Long> {

}
