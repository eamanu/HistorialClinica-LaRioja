package net.pladema.clinichistory.documents.controller;

import net.pladema.clinichistory.hospitalization.controller.generalstate.mapper.HealthConditionMapper;
import net.pladema.clinichistory.documents.controller.dto.HealthConditionNewConsultationDto;
import net.pladema.clinichistory.documents.service.ips.HealthConditionService;
import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionNewConsultationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/institutions/{institutionId}/healthcondition")
public class HealthConditionController {

    private static final Logger LOG = LoggerFactory.getLogger(HealthConditionController.class);
    private static final String OUTPUT = "Output -> {}";
    private static final String INPUT = "Input data -> healthConditionId {}";

    private final HealthConditionMapper healthConditionMapper;
    private final HealthConditionService healthConditionService;

    public HealthConditionController(HealthConditionMapper healthConditionMapper,
                                     HealthConditionService healthConditionService){
       this.healthConditionMapper = healthConditionMapper;
       this.healthConditionService = healthConditionService;
    }

    @GetMapping(value = "/{healthConditionId}")
    public ResponseEntity<HealthConditionNewConsultationDto> getHealthCondition(@PathVariable Integer healthConditionId){
        LOG.debug(INPUT, healthConditionId);

        HealthConditionNewConsultationBo hcbo = healthConditionService.getHealthCondition(healthConditionId);
        HealthConditionNewConsultationDto result = healthConditionMapper.toHealthConditionNewConsultationDto(hcbo);

        LOG.debug(OUTPUT, result);

        return ResponseEntity.ok().body(result);
    }
}
