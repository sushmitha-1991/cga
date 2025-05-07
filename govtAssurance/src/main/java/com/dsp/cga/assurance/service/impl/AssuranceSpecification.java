package com.dsp.cga.assurance.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.dsp.cga.assurance.entity.Assurance;

import jakarta.persistence.criteria.Predicate;

public class AssuranceSpecification {

    public static Specification<Assurance> filterByParams(Long ministryId, Long sessionNo, Integer houseId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (ministryId != null) {
                predicates.add(cb.equal(root.get("ministryId"), ministryId));
            }

            if (houseId != null && sessionNo != null) {
                if (houseId == 1) {
                    predicates.add(cb.isNotNull(root.get("rajyaSabhaSessionNo")));
                    predicates.add(cb.equal(root.get("rajyaSabhaSessionNo"), sessionNo));
                } else if (houseId == 2) {
                    predicates.add(cb.isNotNull(root.get("lokSabhaSessionNo")));
                    predicates.add(cb.equal(root.get("lokSabhaSessionNo"), sessionNo));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
