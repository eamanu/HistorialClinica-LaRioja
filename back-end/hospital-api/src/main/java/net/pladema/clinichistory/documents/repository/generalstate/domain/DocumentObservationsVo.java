package net.pladema.clinichistory.documents.repository.generalstate.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class DocumentObservationsVo implements Serializable {

    private String otherNote;

    private String physicalExamNote;

    private String studiesSummaryNote;

    private String evolutionNote;

    private String clinicalImpressionNote;

    private String currentIllnessNote;

    private String indicationsNote;
}
