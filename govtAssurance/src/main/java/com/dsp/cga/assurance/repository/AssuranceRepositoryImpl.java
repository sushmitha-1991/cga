package com.dsp.cga.assurance.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.supplementry.entity.ClubbedMemberInfo;
import com.dsp.cga.assurance.supplementry.entity.Supplementary;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
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

        // List to hold the predicates
        List<Predicate> predicates = new ArrayList<>();

        // Filtering by fromDate
        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("assuranceDate"), fromDate));
        }

        // Filtering by toDate
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("assuranceDate"), toDate));
        }

        // Filtering by ministryId
        if (ministryId != null) {
            predicates.add(cb.equal(root.get("ministryId"), ministryId));
        }

        if (memberId != null) {
            // Member ID directly in Assurance
            Predicate memberPredicate = cb.equal(root.get("memberId"), memberId);

            // Join Supplementary -> ClubbedMemberInfo
            Join<Assurance, Supplementary> supplementaryJoin = root.join("supplementary", JoinType.LEFT);
            Join<Supplementary, ClubbedMemberInfo> clubbedMembersJoin = supplementaryJoin.join("clubbedMembers", JoinType.LEFT);

            // Check member in clubbed members
            Predicate clubbedMemberPredicate = cb.equal(clubbedMembersJoin.get("clubbedMember"), memberId);

            predicates.add(cb.or(memberPredicate, clubbedMemberPredicate));
        }

        // Additional conditions
        predicates.add(cb.isNotNull(root.get("lokSabhaSessionNo"))); // lokSabhaSessionNo is not null
        predicates.add(cb.isNull(root.get("rajyaSabhaSessionNo")));  // rajyaSabhaSessionNo is null

        // Apply all predicates to the query
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        // Execute the query
        return entityManager.createQuery(query).getResultList();
    }


}

