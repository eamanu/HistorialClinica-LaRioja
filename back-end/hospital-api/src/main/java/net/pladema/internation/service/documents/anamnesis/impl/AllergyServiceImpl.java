package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.repository.ips.AllergyIntoleranceRepository;
import net.pladema.internation.repository.ips.entity.AllergyIntolerance;
import net.pladema.internation.service.SnomedService;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.AllergyService;
import net.pladema.internation.service.domain.ips.AllergyConditionBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllergyServiceImpl implements AllergyService {

    private static final Logger LOG = LoggerFactory.getLogger(AllergyServiceImpl.class);

    private final AllergyIntoleranceRepository allergyIntoleranceRepository;

    private final DocumentService documentService;

    private final SnomedService snomedService;

    public AllergyServiceImpl(AllergyIntoleranceRepository allergyIntoleranceRepository,
                              DocumentService documentService,
                              SnomedService snomedService){
        this.allergyIntoleranceRepository = allergyIntoleranceRepository;
        this.documentService = documentService;
        this.snomedService = snomedService;
    }

    @Override
    public void loadAllergies(Integer patientId, Long documentId, List<AllergyConditionBo> allergies) {
        LOG.debug("Going to load allergies -> {}", allergies);
        LOG.debug("Input parameters -> patientId {}, documentId {}, allergies {}", documentId, patientId, allergies);
        allergies.forEach(allergy -> {

            String sctid = snomedService.createSnomedTerm(allergy.getSnomed());
            if(sctid == null)
                throw new IllegalArgumentException("snomed.invalid");

            AllergyIntolerance allergyIntolerance = new AllergyIntolerance(patientId,
                    sctid,
                    allergy.getStatusId(),
                    allergy.getVerificationId(),
                    allergy.getCategoryId(),
                    allergy.getDate(),
                    allergy.isDeleted());
            allergyIntolerance = allergyIntoleranceRepository.save(allergyIntolerance);
            documentService.createDocumentAllergyIntolerance(documentId, allergyIntolerance.getId());

        });
    }
}
