package com.dsp.cga.assurance.extensionrequest.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.extensionrequest.entity.ExtensionRequest;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class ExtensionRequestSpecification {


public static Specification<ExtensionRequest> withFilters(Long sessionNo, Long statusId,
        LocalDate extRequestedTillDate, LocalDate extGrantedTillDate, LocalDate ministryRefDate) {
    return (root, query, cb) -> {
        Join<ExtensionRequest, Assurance> assuranceJoin = root.join("assurance", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (sessionNo != null) {
            predicates.add(cb.or(
                cb.equal(assuranceJoin.get("lokSabhaSessionNo"), sessionNo),
                cb.equal(assuranceJoin.get("rajyaSabhaSessionNo"), sessionNo)
            ));
        }

        if (statusId != null) {
            predicates.add(cb.equal(root.get("statusId"), statusId));
        }

        if (extRequestedTillDate != null) {
            predicates.add(cb.equal(root.get("extRequestedTillDate"), extRequestedTillDate));
        }

        if (extGrantedTillDate != null) {
            predicates.add(cb.equal(root.get("extGrantedTillDate"), extGrantedTillDate));
        }

        if (ministryRefDate != null) {
            predicates.add(cb.equal(root.get("ministryRefDate"), ministryRefDate));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    };
}
}