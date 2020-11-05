package net.pladema.hl7.dataexchange.clinical;

import net.pladema.hl7.dataexchange.IMultipleResourceFhir;
import net.pladema.hl7.dataexchange.mock.MockCondition;
import net.pladema.hl7.dataexchange.model.adaptor.FhirDateMapper;
import net.pladema.hl7.dataexchange.model.adaptor.FhirID;
import net.pladema.hl7.supporting.terminology.coding.CodingProfile;
import net.pladema.hl7.supporting.terminology.coding.CodingCode;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import net.pladema.hl7.dataexchange.model.domain.ConditionVo;

import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ConditionResource extends IMultipleResourceFhir {

    @Autowired
    public ConditionResource(){
        super();
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.Condition;
    }

    @Override
    public List<Condition> fetch(String id, Reference[] references) {
        List<ConditionVo> conditions = MockCondition.mock();

        if(conditions.isEmpty())
            return noInformationAvailable(references[0]);

        List<Condition> resources = new ArrayList<>();
        conditions.forEach( (condition) ->{
            Condition resource = new Condition();
            resource.setId(condition.getId());
            resource.addCategory(newCodeableConcept(CodingSystem.LOINC, CodingCode.Condition.CATEGORY));
            resource.setSubject(references[0]);
            resource.getOnsetDateTimeType().setValue(FhirDateMapper.toDate(condition.getCreatedOn()));
            resource.setVerificationStatus(newCodeableConcept(CodingSystem.Condition.VERIFICATION, condition.getVerificationStatus()));
            resource.setCode(newCodeableConcept(CodingSystem.SNOMED, condition.get()));
            resource.setRecordedDate(FhirDateMapper.toDate(condition.getStartDate()));

            resource.setSeverity(newCodeableConcept(CodingSystem.LOINC, condition.getSeverity()));
            resource.setClinicalStatus(newCodeableConcept(CodingSystem.Condition.STATUS, condition.getClinicalStatus()));

            resource.setMeta(newMeta(CodingProfile.Condition.URL));
            resources.add(resource);
        });
        return resources;
    }

    private List<Condition> noInformationAvailable(Reference patientRef){
        Condition none = new Condition();
        none.setId(FhirID.autoGenerated());
        none.setCode(newCodeableConcept(CodingSystem.NODATA, CodingCode.Condition.KNOWN_ABSENT));
        none.addCategory(newCodeableConcept(CodingSystem.LOINC, CodingCode.Condition.CATEGORY));
        none.setClinicalStatus(newCodeableConcept(CodingSystem.Condition.STATUS, ConditionVo.defaultClinicalStatus()));
        none.setVerificationStatus(newCodeableConcept(CodingSystem.Condition.VERIFICATION, ConditionVo.defaultVerificationStatus()));
        none.setSubject(patientRef);
        none.getOnsetDateTimeType().addExtension(newExtension(
                CodingProfile.DATA_ABSENT_REASON, CodingCode.ABSENT_REASON)
        );
        return Collections.singletonList(none);
    }
}