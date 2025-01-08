package com.dsp.cga.assurance.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dsp.cga.assurance.entity.Assurance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
@Component
public class AssuranceRepositoryImpl implements AssuranceRepositoryCustom {

	@PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Assurance> findFilteredAssurancesWithCriteria(
        LocalDate fromDate, LocalDate toDate, Long ministryId, Long memberId) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Assurance> query = cb.createQuery(Assurance.class);
        Root<Assurance> root = query.from(Assurance.class);

        List<Predicate> predicates = new ArrayList<>();
        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("assuranceDate"), fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("assuranceDate"), toDate));
        }
        if (ministryId != null) {
            predicates.add(cb.equal(root.get("ministryId"), ministryId));
        }
        if (memberId != null) {
            predicates.add(cb.equal(root.get("memberId"), memberId));
        }

        predicates.add(cb.isNotNull(root.get("rajyaSabhaSessionNo"))); // rajyaSabhaSessionNo is not null
        predicates.add(cb.isNull(root.get("lokSabhaSessionNo")));      // lokSabhaSessionNo is null

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }
    
    @Override
    public List<Assurance> findFilteredAssurancesLsWithCriteria(
        LocalDate fromDate, LocalDate toDate, Long ministryId, Long memberId) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Assurance> query = cb.createQuery(Assurance.class);
        Root<Assurance> root = query.from(Assurance.class);

        List<Predicate> predicates = new ArrayList<>();
        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("assuranceDate"), fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("assuranceDate"), toDate));
        }
        if (ministryId != null) {
            predicates.add(cb.equal(root.get("ministryId"), ministryId));
        }
        if (memberId != null) {
            predicates.add(cb.equal(root.get("memberId"), memberId));
        }

        predicates.add(cb.isNotNull(root.get("lokSabhaSessionNo"))); // lokSabhaSessionNo is not null
        predicates.add(cb.isNull(root.get("rajyaSabhaSessionNo")));  // rajyaSabhaSessionNo is null
        query.where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }
}

