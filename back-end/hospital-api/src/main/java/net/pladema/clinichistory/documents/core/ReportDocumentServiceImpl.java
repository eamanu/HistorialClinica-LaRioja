package net.pladema.clinichistory.documents.core;

import net.pladema.clinichistory.documents.repository.*;
import net.pladema.clinichistory.documents.service.ReportDocumentService;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.ResponsibleDoctorVo;
import net.pladema.clinichistory.documents.repository.generalstate.domain.*;
import net.pladema.clinichistory.hospitalization.service.summary.domain.ResponsibleDoctorBo;
import net.pladema.clinichistory.documents.service.ips.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportDocumentServiceImpl implements ReportDocumentService {

    public static final String OUTPUT = "Output -> {}";

    private static final Logger LOG = LoggerFactory.getLogger(ReportDocumentServiceImpl.class);

    private static final String LOGGING_DOCUMENT_ID = "Input parameters -> documentId {}";

    private final DocumentRepository documentRepository;

    private final DocumentHealthConditionRepository documentHealthConditionRepository;

    private final DocumentImmunizationRepository documentImmunizationRepository;

    private final DocumentVitalSignRepository documentVitalSignRepository;

    private final DocumentLabRepository documentLabRepository;

    private final DocumentAllergyIntoleranceRepository documentAllergyIntoleranceRepository;

    private final DocumentMedicamentionStatementRepository documentMedicamentionStatementRepository;

    public ReportDocumentServiceImpl(DocumentRepository documentRepository,
                                     DocumentHealthConditionRepository documentHealthConditionRepository,
                                     DocumentImmunizationRepository documentImmunizationRepository,
                                     DocumentVitalSignRepository documentVitalSignRepository,
                                     DocumentLabRepository documentLabRepository,
                                     DocumentAllergyIntoleranceRepository documentAllergyIntoleranceRepository,
                                     DocumentMedicamentionStatementRepository documentMedicamentionStatementRepository) {
        this.documentRepository = documentRepository;
        this.documentHealthConditionRepository = documentHealthConditionRepository;
        this.documentImmunizationRepository = documentImmunizationRepository;
        this.documentVitalSignRepository = documentVitalSignRepository;
        this.documentLabRepository = documentLabRepository;
        this.documentAllergyIntoleranceRepository = documentAllergyIntoleranceRepository;
        this.documentMedicamentionStatementRepository = documentMedicamentionStatementRepository;
    }

    @Override
    public GeneralHealthConditionBo getReportHealthConditionFromDocument(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        List<HealthConditionVo> resultQuery = documentHealthConditionRepository.getHealthConditionFromDocumentToReport(documentId);
        GeneralHealthConditionBo result = new GeneralHealthConditionBo(resultQuery);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<ImmunizationBo> getReportImmunizationStateFromDocument(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        List<ImmunizationVo> resultQuery = documentImmunizationRepository.getImmunizationStateFromDocumentToReport(documentId);
        List<ImmunizationBo> result = resultQuery.stream().map(ImmunizationBo::new).collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<AllergyConditionBo> getReportAllergyIntoleranceStateFromDocument(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        List<AllergyConditionVo> resultQuery = documentAllergyIntoleranceRepository.getAllergyIntoleranceStateFromDocumentToReport(documentId);
        List<AllergyConditionBo> result = resultQuery.stream().map(AllergyConditionBo::new).collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<MedicationBo> getReportMedicationStateFromDocument(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        List<MedicationVo> resultQuery = documentMedicamentionStatementRepository.getMedicationStateFromDocumentToReport(documentId);
        List<MedicationBo> result = resultQuery.stream().map(MedicationBo::new).collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public AnthropometricDataBo getReportAnthropometricDataStateFromDocument(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        List<ClinicalObservationVo> clinicalObservationVos = documentVitalSignRepository.getVitalSignStateFromDocument(documentId);
        clinicalObservationVos.addAll(documentLabRepository.getLabStateFromDocument(documentId));
        MapClinicalObservationVo resultQuery = new MapClinicalObservationVo(clinicalObservationVos);
        AnthropometricDataBo result = resultQuery.getLastAnthropometricData().orElse(null);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public VitalSignBo getReportVitalSignStateFromDocument(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        MapClinicalObservationVo resultQuery =
                new MapClinicalObservationVo(documentVitalSignRepository.getVitalSignStateFromDocument(documentId));
        VitalSignBo result = resultQuery.getLastVitalSigns().orElse(null);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public ResponsibleDoctorBo getAuthor(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        ResponsibleDoctorVo author;
        //User Creator
        try {
            author = documentRepository.getUserCreator(documentId);
        }
        catch(IncorrectResultSizeDataAccessException ex) {
            author = null;
        }
        if (author == null)
            //Responsible
            author = documentRepository.getResponsible(documentId);
        ResponsibleDoctorBo result = null;

        if (author != null)
            result = new ResponsibleDoctorBo(author.getId(),
                    author.getFirstName(),
                    author.getLastName(),
                    author.getLicence());
        LOG.debug(OUTPUT, result);
        return result;

    }


}
