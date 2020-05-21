package net.pladema.internation.controller.internment;

import io.swagger.annotations.Api;
import net.pladema.internation.controller.internment.dto.InternmentEpisodeDto;
import net.pladema.internation.controller.internment.dto.InternmentEpisodeProcessDto;
import net.pladema.internation.controller.internment.dto.InternmentPatientDto;
import net.pladema.internation.controller.internment.mapper.InternmentEpisodeMapper;
import net.pladema.internation.service.internment.InternmentPatientService;
import net.pladema.internation.service.internment.domain.BasicListedPatientBo;
import net.pladema.internation.service.internment.domain.InternmentEpisodeBo;
import net.pladema.internation.service.internment.domain.InternmentEpisodeProcessBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/patients")
@Api(value = "Internment Patient", tags = { "Internment Patient" })
public class InternmentPatientController {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentPatientController.class);
    private static final String OUTPUT = "Output -> {}";
    private static final String INPUT_PARAMETERS_INSTITUTION_ID = "Input parameters -> institutionId {}";

    private final InternmentPatientService internmentPatientService;

    private final InternmentEpisodeMapper internmentEpisodeMapper;

    public InternmentPatientController(InternmentPatientService internmentPatientService,
                                       InternmentEpisodeMapper internmentEpisodeMapper) {
        this.internmentPatientService = internmentPatientService;
        this.internmentEpisodeMapper = internmentEpisodeMapper;
    }

    @GetMapping
    public ResponseEntity<List<InternmentEpisodeDto>> getAllInternmentPatient(
            @PathVariable(name = "institutionId") Integer institutionId){
        LOG.debug(INPUT_PARAMETERS_INSTITUTION_ID, institutionId);
        List<InternmentEpisodeBo> internmentEpisodes = internmentPatientService.getAllInternmentPatient(institutionId);
        List<InternmentEpisodeDto> result = internmentEpisodeMapper.toListInternmentEpisodeDto(internmentEpisodes);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @GetMapping("/basicdata")
    public ResponseEntity<List<InternmentPatientDto>> getAllInternmentPatientsBasicData(
            @PathVariable(name = "institutionId") Integer institutionId){
        LOG.debug(INPUT_PARAMETERS_INSTITUTION_ID, institutionId);
        List<BasicListedPatientBo> basicListedPatientBos = internmentPatientService.getInternmentPatients(institutionId);
        List<InternmentPatientDto> result = internmentEpisodeMapper.toListInternmentPatientDto(basicListedPatientBos);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{patientId}/internmentEpisodeIdInProcess")
    public ResponseEntity<InternmentEpisodeProcessDto> internmentEpisodeIdInProcess(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId){
        LOG.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
        InternmentEpisodeProcessBo serviceResult = internmentPatientService.internmentEpisodeInProcess(institutionId,patientId);
        InternmentEpisodeProcessDto result = new InternmentEpisodeProcessDto(serviceResult.getId(), serviceResult.isInProgress());
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }
}