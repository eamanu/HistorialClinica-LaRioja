package net.pladema.patient.repository;

import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.sgx.repository.QueryPart;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

import static net.pladema.patient.repository.PatientSearchQuery.AND_JOINING_OPERATOR;
import static net.pladema.patient.repository.PatientSearchQuery.LIKE_COMPARATOR;

@Repository
public class PatientRepositoryImpl implements PatientRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public PatientRepositoryImpl(EntityManager entityManager){
        super();
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PatientSearch> getAllByOptionalFilter(PatientSearchFilter searchFilter, Integer resultSize) {
        PatientSearchQuery patientSearchQuery = new PatientSearchQuery(searchFilter);
        QueryPart queryPart = new QueryPart(
                "SELECT ")
                .concatPart(patientSearchQuery.select())
                .concat(" FROM ")
                .concatPart(patientSearchQuery.from())
                .concat("WHERE ")
                .concatPart(patientSearchQuery.whereWithAllAttributes(AND_JOINING_OPERATOR, LIKE_COMPARATOR));
        Query query = entityManager.createQuery(queryPart.toString());
        query.setMaxResults(resultSize);
        queryPart.configParams(query);
        return patientSearchQuery.construct(query.getResultList());
    }

    public Long getCountByOptionalFilter(PatientSearchFilter searchFilter){
        PatientSearchQuery patientSearchQuery = new PatientSearchQuery(searchFilter);
        QueryPart queryPart = new QueryPart(
                "SELECT COUNT(*)")
                .concat(" FROM ")
                .concatPart(patientSearchQuery.from())
                .concat("WHERE ")
                .concatPart(patientSearchQuery.whereWithAllAttributes(AND_JOINING_OPERATOR, LIKE_COMPARATOR));
        Query query = entityManager.createQuery(queryPart.toString());
        queryPart.configParams(query);
        return (Long) query.getSingleResult();
    }
}
