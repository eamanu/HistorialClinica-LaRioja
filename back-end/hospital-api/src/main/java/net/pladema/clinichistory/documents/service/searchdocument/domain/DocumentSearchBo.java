package net.pladema.clinichistory.documents.service.searchdocument.domain;

import lombok.*;
import net.pladema.clinichistory.documents.repository.searchdocument.DocumentSearchVo;
import net.pladema.clinichistory.hospitalization.service.summary.domain.ResponsibleDoctorBo;
import net.pladema.clinichistory.documents.service.ips.domain.DocumentObservationsBo;
import net.pladema.person.repository.domain.ProcedureReduced;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DocumentSearchBo {

    private Long id;

    private DocumentObservationsBo notes;

    private String mainDiagnosis;

    private List<String> diagnosis;

    private List<ProcedureReduced> procedures;

    private ResponsibleDoctorBo creator;

    private LocalDate createdOn;

    public DocumentSearchBo(DocumentSearchVo source) {
        this.id = source.getId();
        if(source.getNotes() != null)
            this.notes = new DocumentObservationsBo(source.getNotes());
        this.mainDiagnosis = source.getMainDiagnosis();
        this.diagnosis = source.getDiagnosis();
        this.procedures = source.getProcedures();
        this.creator = new ResponsibleDoctorBo(source.getCreator().getFirstName(), source.getCreator().getLastName());
        this.createdOn = source.getCreatedOn();
    }
}
