package net.pladema.person.repository;

import net.pladema.patient.repository.domain.HealthInsuranceVo;
import net.pladema.person.repository.entity.HealthInsurance;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthInsuranceRepository extends JpaRepository<HealthInsurance, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT new net.pladema.patient.repository.domain.HealthInsuranceVo(mc.id, mc.name, hi.rnos, hi.acronym) " +
            "FROM MedicalCoverage as mc " +
            "JOIN HealthInsurance as hi ON (hi.id = mc.id) ")
    public List<HealthInsuranceVo> getAllWithNames(Sort sort);

    @Transactional(readOnly = true)
    @Query("SELECT new net.pladema.patient.repository.domain.HealthInsuranceVo(mc.id, mc.name, hi.rnos, hi.acronym) " +
            "FROM MedicalCoverage as mc " +
            "JOIN HealthInsurance as hi ON (hi.id = mc.id) " +
            "WHERE hi.rnos = :rnos")
    public Optional<HealthInsuranceVo> findByRnos(@Param("rnos") Integer rnos);

    @Transactional(readOnly = true)
    @Query(value = "SELECT exists (select 1 from health_insurance where rnos = :rnos)", nativeQuery = true)
    boolean existsByRnos(@Param("rnos") Integer rnos);
}
