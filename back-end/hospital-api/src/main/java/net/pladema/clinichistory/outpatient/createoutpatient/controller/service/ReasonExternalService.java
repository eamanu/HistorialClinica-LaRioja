package net.pladema.clinichistory.outpatient.createoutpatient.controller.service;

import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;

import java.util.List;

public interface ReasonExternalService {

    List<SnomedDto> addReasons(List<SnomedDto> reasons);
}
