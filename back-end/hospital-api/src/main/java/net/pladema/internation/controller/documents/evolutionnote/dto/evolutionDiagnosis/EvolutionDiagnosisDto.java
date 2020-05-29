package net.pladema.internation.controller.documents.evolutionnote.dto.evolutionDiagnosis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.controller.internment.dto.DocumentObservationsDto;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class EvolutionDiagnosisDto implements Serializable {

    @Nullable
    private DocumentObservationsDto notes;

    @Nullable
    private List<Integer> diagnosesId = new ArrayList<>();

}