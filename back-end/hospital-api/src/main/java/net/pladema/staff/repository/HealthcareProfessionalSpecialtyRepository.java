package net.pladema.staff.repository;

import net.pladema.staff.repository.domain.ProfessionalClinicalSpecialtyVo;
import net.pladema.staff.repository.entity.HealthcareProfessionalSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HealthcareProfessionalSpecialtyRepository extends JpaRepository<HealthcareProfessionalSpecialty, Integer> {


    @Transactional(readOnly = true)
    @Query( "SELECT (CASE WHEN COUNT(hps.id)> 0 THEN TRUE ELSE FALSE END) " +
            "FROM HealthcareProfessionalSpecialty AS hps " +
            "WHERE hps.healthcareProfessionalId = :healthcareProfessionalId " +
            "AND hps.clinicalSpecialtyId = :clinicalSpecialtyId " +
            "AND hps.professionalSpecialtyId = :professionalSpecialtyId ")
    boolean existsValues(@Param("healthcareProfessionalId") Integer healthcareProfessionalId,
                         @Param("clinicalSpecialtyId")  Integer clinicalSpecialtyId,
                         @Param("professionalSpecialtyId")  Integer professionalSpecialtyId);

    @Transactional(readOnly = true)
    @Query( "SELECT (CASE WHEN COUNT(hps.id) = 1 THEN TRUE ELSE FALSE END) " +
            "FROM HealthcareProfessionalSpecialty AS hps " +
            "WHERE hps.healthcareProfessionalId = :healthcareProfessionalId ")
    boolean hasOnlyOneSpecialty(@Param("healthcareProfessionalId") Integer healthcareProfessionalId);

    @Transactional(readOnly = true)
    @Query( "SELECT (CASE WHEN COUNT(d.healthcareProfessionalId) >= 1 THEN TRUE ELSE FALSE END)" +
            "FROM Diary d " +
            "JOIN DoctorsOffice doff ON d.doctorsOfficeId = doff.id " +
            "JOIN ClinicalSpecialtySector css ON doff.clinicalSpecialtySectorId = css.id "+
            "WHERE d.healthcareProfessionalId = :healthcareProfessionalId AND css.clinicalSpecialtyId = :clinicalSpecialtyId")
    boolean hasHealthcareProfessionalSpecialtyAffected(@Param("healthcareProfessionalId") Integer healthcareProfessionalId,
                                                    @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT NEW net.pladema.staff.repository.domain.ProfessionalClinicalSpecialtyVo" +
            "(hps.healthcareProfessionalId, cs) " +
            "FROM HealthcareProfessionalSpecialty hps "
            + "INNER JOIN ClinicalSpecialty cs ON hps.clinicalSpecialtyId = cs.id "
            + "WHERE hps.healthcareProfessionalId IN :professionalsIds")
    List<ProfessionalClinicalSpecialtyVo> getAllByProfessionals(@Param("professionalsIds") List<Integer> professionalsIds);

}
