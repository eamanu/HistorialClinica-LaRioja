package net.pladema.medicalconsultation.diary.controller;

import io.swagger.annotations.Api;
import net.pladema.medicalconsultation.diary.controller.dto.OccupationDto;
import net.pladema.medicalconsultation.diary.controller.mapper.DiaryMapper;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import net.pladema.medicalconsultation.diary.service.domain.OccupationBo;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/institutions/{institutionId}/medicalConsultations/diaryOpeningHours")
@Api(value = "Diary opening hours", tags = { "Diary Opening Hours" })
public class DiaryOpeningHoursController {

    private static final Logger LOG = LoggerFactory.getLogger(DiaryOpeningHoursController.class);
    public static final String OUTPUT = "Output -> {}";

    private final DiaryOpeningHoursService diaryOpeningHoursService;

    private final DiaryMapper diaryMapper;

    private final LocalDateMapper localDateMapper;

    public DiaryOpeningHoursController(DiaryOpeningHoursService diaryOpeningHoursService,
                                       DiaryMapper diaryMapper,
                                       LocalDateMapper localDateMapper){
        super();
        this.diaryOpeningHoursService = diaryOpeningHoursService;
        this.diaryMapper = diaryMapper;
        this.localDateMapper = localDateMapper;
    }

    /**
     *
     * @param doctorsOfficeId consultorio en el cual iniciar una nueva agenda
     * @param startDateStr fecha de inicio de nueva agenda
     * @param endDateStr fecha de fin de nueva agenda
     * @return lista con los días y rangos de horario en los cuales el consultorio {@code doctorsOfficeId}
     * se encuentra ocupado
     */
    @GetMapping("/doctorsOffice/{doctorsOfficeId}")
    public ResponseEntity<List<OccupationDto>> getAllWeeklyDoctorsOfficeOcupation(
            @PathVariable(name = "doctorsOfficeId") Integer doctorsOfficeId,
            @RequestParam(name = "startDate") String startDateStr,
            @RequestParam(name = "endDate") String endDateStr) {
        LOG.debug("Input parameters -> doctorsOfficeId {}, startDateStr {}, endDateStr {}",
                doctorsOfficeId, startDateStr, endDateStr);

        LocalDate startDate = localDateMapper.fromStringToLocalDate(startDateStr);
        LocalDate endDate = localDateMapper.fromStringToLocalDate(endDateStr);

        List<OccupationBo> occupationBos = diaryOpeningHoursService
                .findAllWeeklyDoctorsOfficeOccupation(doctorsOfficeId, startDate, endDate);
        List<OccupationDto> result = diaryMapper.toListOccupationDto(occupationBos);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }
}