package com.dsp.govtAssurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.dsp.govtAssurance.entity.BaseUser;


@Repository
public interface BaseUserRepository extends JpaRepository<BaseUser, Long> {

}
