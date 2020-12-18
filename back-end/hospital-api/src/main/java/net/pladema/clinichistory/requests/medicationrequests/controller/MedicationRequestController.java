package net.pladema.clinichistory.requests.medicationrequests.controller;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.MedicationStatementStatus;
import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.MedicationInfoDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.mapper.CreateMedicationRequestMapper;
import net.pladema.clinichistory.requests.medicationrequests.controller.mapper.ListMedicationInfoMapper;
import net.pladema.clinichistory.requests.medicationrequests.service.CreateMedicationRequestService;
import net.pladema.clinichistory.requests.medicationrequests.service.ListMedicationInfoService;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationFilterBo;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationRequestBo;
import net.pladema.sgx.error.controller.dto.ApiErrorDto;
import net.pladema.sgx.security.utils.UserInfo;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/medication-requests")
@Api(value = "Medication Request", tags = {"Medication Request"})
@Validated
public class MedicationRequestController {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationRequestController.class);

    private final CreateMedicationRequestService createMedicationRequestService;

    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

    private final CreateMedicationRequestMapper createMedicationRequestMapper;

    private final ListMedicationInfoService listMedicationInfoService;

    private final ListMedicationInfoMapper listMedicationInfoMapper;

    public MedicationRequestController(CreateMedicationRequestService createMedicationRequestService,
                                       HealthcareProfessionalExternalService healthcareProfessionalExternalService,
                                       CreateMedicationRequestMapper createMedicationRequestMapper,
                                       ListMedicationInfoService listMedicationInfoService,
                                       ListMedicationInfoMapper listMedicationInfoMapper) {
        this.createMedicationRequestService = createMedicationRequestService;
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.createMedicationRequestMapper = createMedicationRequestMapper;
        this.listMedicationInfoService = listMedicationInfoService;
        this.listMedicationInfoMapper = listMedicationInfoMapper;
    }


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Transactional
    public @ResponseBody
    Integer create(@PathVariable(name = "institutionId") Integer institutionId,
                   @PathVariable(name = "patientId") Integer patientId,
                   @RequestBody @Valid PrescriptionDto medicationRequest) {
        LOG.debug("create -> institutionId {}, patientId {}, medicationRequest {}", institutionId, patientId, medicationRequest);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        MedicationRequestBo medicationRequestBo = createMedicationRequestMapper.parseTo(doctorId, patientId, medicationRequest);
        Integer result = createMedicationRequestService.execute(institutionId, medicationRequestBo);
        LOG.debug("create result -> {}", result);
        return result;
    }



    @PutMapping(value = "/suspend")
    @ResponseStatus(code = HttpStatus.OK)
    @Transactional
    public void suspendMedication(@PathVariable(name = "institutionId") Integer institutionId,
                        @PathVariable(name = "patientId") Integer patientId,
                        @RequestParam(value = "dayQuantity") Short dayQuantity,
                        @RequestParam(value = "medicationsIds") List<Integer> medicationsIds) {
        LOG.debug("change-state -> institutionId {}, patientId {}, dayQuantity {}, medicationsIds {}", institutionId, patientId, dayQuantity, medicationsIds);
        LOG.debug("suspend success");
    }


    @PutMapping(value = "/finalize")
    @ResponseStatus(code = HttpStatus.OK)
    @Transactional
    public void finalizeMedication(@PathVariable(name = "institutionId") Integer institutionId,
                        @PathVariable(name = "patientId") Integer patientId,
                        @RequestParam(value = "medicationsIds") List<Integer> medicationsIds) {
        LOG.debug("change-state -> institutionId {}, patientId {}, medicationsIds {}", institutionId, patientId, medicationsIds);
        LOG.debug("finalize success");
    }

    @PutMapping(value = "/reactivate")
    @ResponseStatus(code = HttpStatus.OK)
    @Transactional
    public void reactivateMedication(@PathVariable(name = "institutionId") Integer institutionId,
                        @PathVariable(name = "patientId") Integer patientId,
                        @RequestParam(value = "medicationsIds") List<Integer> medicationsIds) {
        LOG.debug("change-state -> institutionId {}, patientId {}, medicationsIds {}", institutionId, patientId, medicationsIds);
        LOG.debug("reactivate success");
    }


    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public @ResponseBody
    List<MedicationInfoDto> medicationRequestList(@PathVariable(name = "institutionId") Integer institutionId,
                                                  @PathVariable(name = "patientId") Integer patientId,
                                                  @RequestParam(value = "statusId", defaultValue = MedicationStatementStatus.ACTIVE) String statusId,
                                                  @RequestParam(value = "medicationStatement", required = false) String medicationStatement,
                                                  @RequestParam(value = "healthCondition", required = false) String healthCondition) {
        LOG.debug("medicationRequestList -> institutionId {}, patientId {}, statusId {}, medicationStatement {}, healthCondition {}", institutionId, patientId, statusId, medicationStatement, healthCondition);
        List<MedicationBo> resultService = listMedicationInfoService.execute(new MedicationFilterBo(patientId, statusId, medicationStatement, healthCondition));
        List<MedicationInfoDto> result = resultService.stream()
                .map(mid -> {
                    ProfessionalDto professionalDto = healthcareProfessionalExternalService.findProfessionalByUserId(mid.getUserId());
                    return listMedicationInfoMapper.parseTo(mid, professionalDto);
                })
                .collect(Collectors.toList());
        LOG.debug("medicationRequestList result -> {}", result);
        return result;
    }

    @GetMapping(value = "/{medicationRequestId}/download")
    public ResponseEntity<InputStreamResource> download(@PathVariable(name = "institutionId") Integer institutionId,
                                                        @PathVariable(name = "patientId") Integer patientId,
                                                        @PathVariable(name = "medicationRequestId") Integer medicationRequestId) {
        LOG.debug("medicationRequestList -> institutionId {}, patientId {}, medicationRequestId {}", institutionId, patientId, medicationRequestId);
        String name = "MedicationRequest";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(os.toByteArray());
        InputStreamResource resource = new InputStreamResource(byteArrayInputStream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + name)
                .contentType(MediaType.APPLICATION_PDF).contentLength(os.size()).body(resource);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ IllegalArgumentException.class })
    public ApiErrorDto handleValidationExceptions(IllegalArgumentException ex) {
        LOG.error("Constraint violation -> {}", ex.getMessage());
        return new ApiErrorDto("Constraint violation", ex.getMessage());
    }
}