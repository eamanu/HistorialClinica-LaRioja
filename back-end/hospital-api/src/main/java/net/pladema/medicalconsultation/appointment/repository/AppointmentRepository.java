package net.pladema.medicalconsultation.appointment.repository;

import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Transactional(readOnly = true)
    @Query( "SELECT NEW net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo(aa.pk.diaryId, a)" +
            "FROM Appointment AS a " +
            "JOIN AppointmentAssn AS aa ON (a.id = aa.pk.appointmentId) " +
            "WHERE aa.pk.diaryId IN (:diaryIds) ")
    List<AppointmentDiaryVo> getAppointmentsByDiaries(@Param("diaryIds") List<Integer> diaryIds);


}
