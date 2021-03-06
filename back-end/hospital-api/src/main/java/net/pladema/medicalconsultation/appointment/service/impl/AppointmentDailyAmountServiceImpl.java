package net.pladema.medicalconsultation.appointment.service.impl;

import net.pladema.medicalconsultation.appointment.service.AppointmentDailyAmountService;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentDailyAmountBo;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.CompleteDiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.repository.entity.MedicalAttentionType;
import ar.lamansys.sgx.shared.dates.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class AppointmentDailyAmountServiceImpl implements AppointmentDailyAmountService {

    private final AppointmentService appointmentService;

    private final DiaryService diaryService;


    public AppointmentDailyAmountServiceImpl(AppointmentService appointmentService, DiaryService diaryService
    ) {
        this.appointmentService = appointmentService;
        this.diaryService = diaryService;
    }

    @Override
    public Collection<AppointmentDailyAmountBo> getDailyAmounts(Integer diaryId) {
        Collection<AppointmentDailyAmountBo> appointmentsDailyAmount = new ArrayList<>();

        Collection<AppointmentBo> appointments = appointmentService.getAppointmentsByDiaries(Arrays.asList(diaryId));
        Optional<CompleteDiaryBo> diary = diaryService.getDiary(diaryId);

        if (diary.isPresent()) {

            for (LocalDate date = diary.get().getStartDate();
                 date.isBefore(diary.get().getEndDate()) || date.isEqual(diary.get().getEndDate()); date = date.plusDays(1)) {

                Collection<DiaryOpeningHoursBo> openingHoursDate = this.getOpeningHoursFor(date, diary.get().getDiaryOpeningHours());

                if (!openingHoursDate.isEmpty()) {
                    AppointmentDailyAmountBo dailyAmount = this.calculateAmountsFor(date, openingHoursDate, appointments, diary.get().getAppointmentDuration());
                    appointmentsDailyAmount.add(dailyAmount);
                }
            }
        }

        return appointmentsDailyAmount;
    }

    private Collection<DiaryOpeningHoursBo> getOpeningHoursFor(LocalDate date, Collection<DiaryOpeningHoursBo> openingHours) {
        return openingHours.stream()
                .filter(oh -> oh.getOpeningHours().getDayWeekId().equals(DateUtils.getWeekDay(date)))
                .collect(Collectors.toList());
    }

    private AppointmentDailyAmountBo calculateAmountsFor(LocalDate date, Collection<DiaryOpeningHoursBo> openingHours,
                                                         Collection<AppointmentBo> appointments,
                                                         Short appointmentDuration) {
        AppointmentDailyAmountBo dailyAmountBo = new AppointmentDailyAmountBo(null, null, null, date);
        Collection<AppointmentBo> appointmentsForDate = appointments.stream().filter(a -> a.getDate().equals(date)).collect(Collectors.toList());

        openingHours.forEach(oh -> {
            if (oh.getMedicalAttentionTypeId().equals(MedicalAttentionType.SPONTANEOUS)) {
                calculateSpontaneousAmount(dailyAmountBo, appointmentsForDate, oh);
            } else {
                calculateProgrammedAmount(appointmentDuration, dailyAmountBo, appointmentsForDate, oh);
            }
        });

        return dailyAmountBo;
    }

    private void calculateProgrammedAmount(Short appointmentDuration, AppointmentDailyAmountBo dailyAmountBo, Collection<AppointmentBo> appointmentsForDate, DiaryOpeningHoursBo oh) {
        Long diff = MINUTES.between(oh.getOpeningHours().getFrom(), oh.getOpeningHours().getTo());
        Long segmentsInOpeningHour = diff / appointmentDuration;

        if (dailyAmountBo.getProgrammed() == null) {
            dailyAmountBo.setProgrammed(0);
            dailyAmountBo.setProgrammedAvailable(0);
        }
        dailyAmountBo.setProgrammedAvailable(dailyAmountBo.getProgrammedAvailable() + segmentsInOpeningHour.intValue());
        if (!appointmentsForDate.isEmpty()) {
            Integer amount = this.getAmountBy(oh, appointmentsForDate);
            dailyAmountBo.setProgrammed(dailyAmountBo.getProgrammed() + amount);
            dailyAmountBo.setProgrammedAvailable(dailyAmountBo.getProgrammedAvailable() - amount);
        }
    }

    private void calculateSpontaneousAmount(AppointmentDailyAmountBo dailyAmountBo, Collection<AppointmentBo> appointmentsForDate, DiaryOpeningHoursBo oh) {
        if (dailyAmountBo.getSpontaneous() == null)
            dailyAmountBo.setSpontaneous(0);
        if (!appointmentsForDate.isEmpty()) {
            dailyAmountBo.setSpontaneous(this.getAmountBy(oh, appointmentsForDate));
        }
    }

    private Integer getAmountBy(DiaryOpeningHoursBo oh, Collection<AppointmentBo> appointmentsForDate) {
        LocalTime from = oh.getOpeningHours().getFrom();
        LocalTime to = oh.getOpeningHours().getTo();

        Collection<AppointmentBo> appointmentsInOH = appointmentsForDate.stream()
                .filter(a -> (!a.isOverturn() && this.isAppointmentInOH(a, from, to)))
                .collect(Collectors.toList());

        return appointmentsInOH.size();
    }

    private boolean isAppointmentInOH(AppointmentBo a, LocalTime from, LocalTime to) {
        return (a.getHour().isAfter(from) && a.getHour().isBefore(to)) || a.getHour().equals(from);
    }
}
