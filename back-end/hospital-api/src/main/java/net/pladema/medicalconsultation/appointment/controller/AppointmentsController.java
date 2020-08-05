package net.pladema.medicalconsultation.appointment.controller;

import io.swagger.annotations.Api;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentListDto;
import net.pladema.medicalconsultation.appointment.controller.dto.CreateAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.mapper.AppointmentMapper;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.patient.controller.dto.AppointmentPatientDto;
import net.pladema.patient.controller.service.PatientExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/institutions/{institutionId}/medicalConsultations/appointments")
@Api(value = "Appointments ", tags = { "Appointments" })
@Validated
public class AppointmentsController {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentsController.class);

    public static final String OUTPUT = "Output -> {}";

    private final AppointmentService appointmentService;

    private final AppointmentMapper appointmentMapper;

    private final PatientExternalService patientExternalService;

    public AppointmentsController(AppointmentService appointmentService, AppointmentMapper appointmentMapper,
                                  PatientExternalService patientExternalService) {
        super();
        this.appointmentService = appointmentService;
        this.appointmentMapper = appointmentMapper;
        this.patientExternalService = patientExternalService;
    }

    @Transactional
    @PostMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO')")
    public ResponseEntity<Integer> create(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestBody CreateAppointmentDto createAppointmentDto) {
        LOG.debug("Input parameters -> institutionId {}, appointmentDto {}", institutionId, createAppointmentDto);
        Random rand = new Random();
        Integer result = rand.nextInt(1000) + 0;;
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Collection<AppointmentListDto>> getList(@PathVariable(name = "institutionId")  Integer institutionId,
                                                                  @RequestParam(name = "diaryIds") @NotEmpty List<Integer> diaryIds){
        LOG.debug("Input parameters -> institutionId {}, diaryIds {}", institutionId, diaryIds);
        Collection<AppointmentBo> resultService = appointmentService.getAppointmentsByDiaries(diaryIds);
        Collection<AppointmentListDto> result = resultService.stream()
                .parallel()
                .map(this::mapData)
                .collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok(result);
    }

    private AppointmentListDto mapData(AppointmentBo appointmentBo) {
        LOG.debug("Input parameters -> appointmentBo {}", appointmentBo);
        AppointmentPatientDto appointmentPatientDto = patientExternalService.getAppointmentPatientDto(appointmentBo.getPatientId());
        AppointmentListDto result = appointmentMapper.toAppointmentListDto(appointmentBo, appointmentPatientDto);
        LOG.debug(OUTPUT, result);
        return result;
    }
}