package net.pladema.internation.service.internment.impl;

import net.pladema.internation.repository.core.EvolutionNoteDocumentRepository;
import net.pladema.internation.repository.core.InternmentEpisodeRepository;
import net.pladema.internation.repository.core.domain.InternmentSummary;
import net.pladema.internation.repository.core.entity.EvolutionNoteDocument;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InternmentEpisodeServiceImpl implements InternmentEpisodeService {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentEpisodeServiceImpl.class);

    private final InternmentEpisodeRepository internmentEpisodeRepository;

    private final EvolutionNoteDocumentRepository evolutionNoteDocumentRepository;

    public InternmentEpisodeServiceImpl(InternmentEpisodeRepository internmentEpisodeRepository,
                                        EvolutionNoteDocumentRepository evolutionNoteDocumentRepository) {
        this.internmentEpisodeRepository = internmentEpisodeRepository;
        this.evolutionNoteDocumentRepository = evolutionNoteDocumentRepository;
    }


    @Override
    public void updateAnamnesisDocumentId(Integer internmentEpisodeId, Long anamnesisDocumentId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}, anamnesisDocumentId {}", internmentEpisodeId, anamnesisDocumentId);
        internmentEpisodeRepository.updateAnamnesisDocumentId(internmentEpisodeId, anamnesisDocumentId, LocalDateTime.now());
    }

    @Override
    public void updateEpicrisisDocumentId(Integer internmentEpisodeId, Long epicrisisId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}, epicrisisId {}", internmentEpisodeId, epicrisisId);
        internmentEpisodeRepository.updateEpicrisisDocumentId(internmentEpisodeId, epicrisisId, LocalDateTime.now());
    }

    @Override
    public EvolutionNoteDocument addEvolutionNote(Integer internmentEpisodeId, Long evolutionNoteId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}, evolutionNoteId {}", internmentEpisodeId, evolutionNoteId);
        EvolutionNoteDocument result = new EvolutionNoteDocument(evolutionNoteId, internmentEpisodeId);
        result = evolutionNoteDocumentRepository.save(result);
        LOG.debug("Output -> {}", result);
        return result;
    }

    @Override
    public Optional<InternmentSummary> getIntermentSummary(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> {}", internmentEpisodeId);
        Optional<InternmentSummary> result = internmentEpisodeRepository.getSummary(internmentEpisodeId);
        LOG.debug("Output -> {}", result);
        return result;
    }

    @Override
    public Optional<Integer> getPatient(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> {}", internmentEpisodeId);
        Optional<Integer> result = internmentEpisodeRepository.getPatient(internmentEpisodeId);
        LOG.debug("Output -> {}", result);
        return result;
    }


}
