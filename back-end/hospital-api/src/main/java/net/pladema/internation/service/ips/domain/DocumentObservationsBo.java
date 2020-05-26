package net.pladema.internation.service.ips.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class DocumentObservationsBo implements Serializable {

    private String otherNote;

    private String physicalExamNote;

    private String studiesSummaryNote;

    private String evolutionNote;

    private String clinicalImpressionNote;

    private String currentIllnessNote;

    private String indicationsNote;
}