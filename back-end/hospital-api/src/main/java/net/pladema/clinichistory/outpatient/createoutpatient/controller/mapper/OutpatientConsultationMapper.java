package net.pladema.clinichistory.outpatient.createoutpatient.controller.mapper;

import net.pladema.clinichistory.ips.service.domain.ImmunizationBo;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.CreateOutpatientDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientImmunizationDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientReasonDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientUpdateImmunizationDto;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ReasonBo;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface OutpatientConsultationMapper {

    @Named("fromCreateOutpatientDto")
    OutpatientDocumentBo fromCreateOutpatientDto(CreateOutpatientDto createOutpatientDto);

    @Named("fromListReasonDto")
    List<ReasonBo> fromListReasonDto(List<OutpatientReasonDto> reasons);

    @Named("fromOutpatientImmunizationDto")
    ImmunizationBo fromOutpatientImmunizationDto(OutpatientImmunizationDto vaccineDto);

    @Named("fromOutpatientUpdateImmunizationDto")
    ImmunizationBo fromOutpatientImmunizationDto(OutpatientUpdateImmunizationDto outpatientUpdateImmunization);
}
