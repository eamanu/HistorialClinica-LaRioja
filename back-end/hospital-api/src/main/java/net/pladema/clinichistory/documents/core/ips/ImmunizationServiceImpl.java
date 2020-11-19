package net.pladema.clinichistory.documents.core.ips;

import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.documents.repository.ips.ImmunizationRepository;
import net.pladema.clinichistory.documents.repository.ips.entity.Inmunization;
import net.pladema.clinichistory.documents.repository.ips.masterdata.ImmunizationStatusRepository;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.InmunizationStatus;
import net.pladema.clinichistory.documents.service.ips.ImmunizationService;
import net.pladema.clinichistory.documents.service.ips.SnomedService;
import net.pladema.clinichistory.documents.service.ips.domain.ImmunizationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ImmunizationServiceImpl implements ImmunizationService {

    private static final Logger LOG = LoggerFactory.getLogger(ImmunizationServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final ImmunizationRepository immunizationRepository;

    private final ImmunizationStatusRepository immunizationStatusRepository;

    private final SnomedService snomedService;

    private final DocumentService documentService;

    private final NoteService noteService;

    public ImmunizationServiceImpl(ImmunizationRepository immunizationRepository,
                                   ImmunizationStatusRepository immunizationStatusRepository,
                                   SnomedService snomedService,
                                   DocumentService documentService,
                                   NoteService noteService){
        this.immunizationRepository = immunizationRepository;
        this.immunizationStatusRepository = immunizationStatusRepository;
        this.snomedService = snomedService;
        this.documentService = documentService;
        this.noteService = noteService;
    }

    @Override
    public List<ImmunizationBo> loadImmunization(Integer patientId, Long documentId, List<ImmunizationBo> immunizations) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, immunizations {}", patientId, documentId, immunizations);
        immunizations.forEach(i -> {
            String sctId = snomedService.createSnomedTerm(i.getSnomed());

            AtomicReference<Long> noteId = new AtomicReference<>(null);
            Optional.ofNullable(i.getNote()).ifPresent(n ->
                noteId.set(loadNote(n))
            );

            Inmunization immunization = saveImmunization(patientId, i, sctId, noteId.get());

            i.setId(immunization.getId());
            i.setStatusId(immunization.getStatusId());
            i.setStatus(getStatus(i.getStatusId()));
            i.setAdministrationDate(immunization.getAdministrationDate());

            documentService.createImmunization(documentId, immunization.getId());
        });
        List<ImmunizationBo> result = immunizations;
        LOG.debug(OUTPUT, result);
        return result;
    }

    private Inmunization saveImmunization(Integer patientId, ImmunizationBo immunizationBo, String sctId, Long noteId) {
        LOG.debug("Input parameters -> patientId {}, immunizationBo {}, sctId {}, noteId {}", patientId, immunizationBo, sctId, noteId);
        Inmunization immunization = new Inmunization(patientId, sctId, immunizationBo.getStatusId()
                , immunizationBo.getAdministrationDate(), immunizationBo.getInstitutionId(), noteId);
        immunization = immunizationRepository.save(immunization);
        LOG.debug("Immunization saved -> {}", immunization.getId());
        LOG.debug(OUTPUT, immunization);
        return immunization;
    }

    private String getStatus(String id) {
        return immunizationStatusRepository.findById(id).map(InmunizationStatus::getDescription).orElse(null);
    }


    private Long loadNote(String note) {
        LOG.debug("Input parameters -> note {}", note);
        Long result = noteService.createNote(note);
        LOG.debug(OUTPUT, result);
        return result;
    }

}