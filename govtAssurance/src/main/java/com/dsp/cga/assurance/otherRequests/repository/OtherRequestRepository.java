package com.dsp.cga.assurance.otherRequests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dsp.cga.assurance.otherRequests.entity.OtherRequests;

@Repository
public interface OtherRequestRepository extends JpaRepository<OtherRequests, Long>, JpaSpecificationExecutor<OtherRequests> {

}
