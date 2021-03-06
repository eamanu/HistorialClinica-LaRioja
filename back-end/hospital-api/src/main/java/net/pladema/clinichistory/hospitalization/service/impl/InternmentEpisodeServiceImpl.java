package net.pladema.clinichistory.hospitalization.service.impl;

import net.pladema.clinichistory.documents.repository.EvolutionNoteDocumentRepository;
import net.pladema.clinichistory.documents.repository.entity.EvolutionNoteDocument;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.hospitalization.repository.PatientDischargeRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.repository.domain.PatientDischarge;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.EvaluationNoteSummaryVo;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.InternmentSummaryVo;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentSummaryBo;
import net.pladema.clinichistory.hospitalization.service.domain.PatientDischargeBo;
import ar.lamansys.sgx.shared.auditable.entity.Updateable;
import net.pladema.sgx.exceptions.NotFoundException;
import net.pladema.sgx.security.utils.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class InternmentEpisodeServiceImpl implements InternmentEpisodeService {


	private static final Logger LOG = LoggerFactory.getLogger(InternmentEpisodeServiceImpl.class);

	private static final String INPUT_PARAMETERS_INTERNMENT_EPISODE = "Input parameters -> internmentEpisodeId {}";
	private static final String INPUT_PARAMETERS = "Input parameters -> {}";
	private static final String INTERNMENT_NOT_FOUND = "internmentepisode.not.found";
	private static final String LOGGING_OUTPUT = "Output -> {}";
	private static final short ACTIVE = 1;
	private static final String WRONG_ID_EPISODE = "wrong-id-episode";

	private final InternmentEpisodeRepository internmentEpisodeRepository;

	private final EvolutionNoteDocumentRepository evolutionNoteDocumentRepository;

	private final PatientDischargeRepository patientDischargeRepository;

	private final DocumentService documentService;

	public InternmentEpisodeServiceImpl(InternmentEpisodeRepository internmentEpisodeRepository,
			EvolutionNoteDocumentRepository evolutionNoteDocumentRepository,
			PatientDischargeRepository patientDischargeRepository,DocumentService documentService) {
		this.internmentEpisodeRepository = internmentEpisodeRepository;
		this.evolutionNoteDocumentRepository = evolutionNoteDocumentRepository;
		this.patientDischargeRepository = patientDischargeRepository;
		this.documentService = documentService;
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
		LOG.debug("Input parameters -> internmentEpisodeId {}, evolutionNoteId {}", internmentEpisodeId,
				evolutionNoteId);
		EvolutionNoteDocument result = new EvolutionNoteDocument(evolutionNoteId, internmentEpisodeId);
		result = evolutionNoteDocumentRepository.save(result);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public InternmentEpisode addInternmentEpisode(InternmentEpisode internmentEpisode, Integer institutionId) {
		LOG.debug("Input parameters -> internmentEpisode {}, institutionId {}", internmentEpisode, institutionId);
		internmentEpisode.setInstitutionId(institutionId);
		internmentEpisode.setStatusId(ACTIVE);
		internmentEpisode.setEntryDate(LocalDate.now());
		InternmentEpisode result = internmentEpisodeRepository.save(internmentEpisode);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public boolean haveAnamnesis(Integer internmentEpisodeId) {
		LOG.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE, internmentEpisodeId);
		boolean result = internmentEpisodeRepository.haveAnamnesis(internmentEpisodeId);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public boolean haveEpicrisis(Integer internmentEpisodeId) {
		LOG.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE, internmentEpisodeId);
		boolean result = internmentEpisodeRepository.haveEpicrisis(internmentEpisodeId);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public LocalDate getEntryDate(Integer internmentEpisodeId) {
		LOG.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE, internmentEpisodeId);
		LocalDate result = internmentEpisodeRepository.getEntryDate(internmentEpisodeId);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public boolean canCreateEpicrisis(Integer internmentEpisodeId) {
		LOG.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE, internmentEpisodeId);
		boolean result = internmentEpisodeRepository.canCreateEpicrisis(internmentEpisodeId);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public Optional<InternmentSummaryBo> getIntermentSummary(Integer internmentEpisodeId) {
		LOG.debug(INPUT_PARAMETERS, internmentEpisodeId);
		Optional<InternmentSummaryVo> resultQuery = internmentEpisodeRepository.getSummary(internmentEpisodeId);
		AtomicReference<Optional<InternmentSummaryBo>> result = new AtomicReference<>(Optional.empty());
		resultQuery.ifPresent(r -> {
			r.getDocuments().setLastEvaluationNote(getLastEvaluationNoteSummary(internmentEpisodeId).orElse(new EvaluationNoteSummaryVo()));
			result.set(Optional.of(new InternmentSummaryBo(r)));

		});
		LOG.debug(LOGGING_OUTPUT, result);
		return result.get();
	}

    private Optional<EvaluationNoteSummaryVo> getLastEvaluationNoteSummary(Integer internmentEpisodeId){
		LOG.debug(INPUT_PARAMETERS, internmentEpisodeId);
        Page<EvaluationNoteSummaryVo> resultQuery = evolutionNoteDocumentRepository.getLastEvaluationNoteSummary(internmentEpisodeId, PageRequest.of(0, 1));
		Optional<EvaluationNoteSummaryVo> result = resultQuery.getContent().stream().findFirst();
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public Optional<Integer> getPatient(Integer internmentEpisodeId) {
		LOG.debug(INPUT_PARAMETERS, internmentEpisodeId);
		Optional<Integer> result = internmentEpisodeRepository.getPatient(internmentEpisodeId);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public PatientDischargeBo savePatientDischarge(PatientDischargeBo patientDischargeBo) {
		LOG.debug(INPUT_PARAMETERS, patientDischargeBo);
		return patientDischargeRepository.findById(patientDischargeBo.getInternmentEpisodeId()).map(pd -> {
			PatientDischargeBo result = new PatientDischargeBo(updatePatientDischarge(pd, patientDischargeBo));
			LOG.debug(LOGGING_OUTPUT, result);
			return result;
		}).orElseGet(() -> {
			PatientDischarge entityResult = patientDischargeRepository.save(new PatientDischarge(patientDischargeBo));
			PatientDischargeBo result = new PatientDischargeBo(entityResult);
			LOG.debug(LOGGING_OUTPUT, result);
			return result;
		});
	}

	private PatientDischarge updatePatientDischarge(PatientDischarge patientDischarge, PatientDischargeBo patientDischargeBo) {
		LOG.debug(INPUT_PARAMETERS, patientDischargeBo, patientDischarge);
		patientDischarge.setInternmentEpisodeId(patientDischargeBo.getInternmentEpisodeId());
		patientDischarge.setDischargeTypeId(patientDischargeBo.getDischargeTypeId());
		patientDischarge.setAdministrativeDischargeDate(patientDischargeBo.getAdministrativeDischargeDate());
		patientDischarge = patientDischargeRepository.save(patientDischarge);
		LOG.debug(LOGGING_OUTPUT, patientDischarge);
		return patientDischarge;
	}

	@Override
	public void updateInternmentEpisodeStatus(Integer internmentEpisodeId, Short statusId) {
		LOG.debug("Input parameters -> {}, {}", internmentEpisodeId, statusId);
		InternmentEpisode internmentEpisode = internmentEpisodeRepository.findById(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException(WRONG_ID_EPISODE, INTERNMENT_NOT_FOUND));
		internmentEpisode.setStatusId(statusId);
		internmentEpisodeRepository.save(internmentEpisode);
	}
	
	@Override
	public List<InternmentEpisode> findByBedId(Integer bedId) {
		LOG.debug("Input parameters -> bedId {} ", bedId);
		List<InternmentEpisode> result = internmentEpisodeRepository.findByBedId(bedId);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}


	@Override
	public InternmentEpisode getInternmentEpisode(Integer internmentEpisodeId, Integer institutionId) {
		LOG.debug("Input parameters -> internmentEpisodeId {}, institutionId {}", internmentEpisodeId, institutionId);
		InternmentEpisode internmentEpisode = internmentEpisodeRepository.getInternmentEpisode(internmentEpisodeId,institutionId)
				.orElseThrow(() -> new NotFoundException(WRONG_ID_EPISODE, INTERNMENT_NOT_FOUND));
		LOG.debug(LOGGING_OUTPUT, internmentEpisode);
		return internmentEpisode;
	}
	
	@Override
	public boolean existsActiveForBedId(Integer bedId) {
		LOG.debug("Input parameters -> bedId {} ", bedId);
		List<InternmentEpisode> episodes = this.findByBedId(bedId);
		boolean result = episodes != null && !episodes.isEmpty() && anyActive(episodes);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public LocalDate getLastUpdateDateOfInternmentEpisode(Integer internmentEpisodeId) {
		LOG.debug("Input parameters -> internmentEpisodeId {} ", internmentEpisodeId);
		LocalDate entryDate = this.getEntryDate(internmentEpisodeId);
		if (entryDate == null)
			throw new NotFoundException(WRONG_ID_EPISODE, INTERNMENT_NOT_FOUND);
		List<Updateable> intermentDocuments = documentService.getUpdatableDocuments(internmentEpisodeId);
		List<LocalDate> dates = intermentDocuments.stream()
				.map( doc -> doc.getUpdatedOn().toLocalDate())
				.collect(Collectors.toList());
		dates.add(entryDate);
		LocalDate result = dates.stream().max(LocalDate::compareTo).orElse(entryDate);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public LocalDateTime updateInternmentEpisodeProbableDischargeDate(Integer internmentEpisodeId, LocalDateTime probableDischargeDate) {
		LOG.debug("Input parameters -> internmentEpisodeId {}, probableDischargeDate {}", internmentEpisodeId, probableDischargeDate);
		Integer currentUser = UserInfo.getCurrentAuditor();
		internmentEpisodeRepository.updateInternmentEpisodeProbableDischargeDate(internmentEpisodeId, probableDischargeDate, currentUser, LocalDateTime.now());
		LOG.debug(LOGGING_OUTPUT, probableDischargeDate);
		return probableDischargeDate;
    }

	private boolean anyActive(List<InternmentEpisode> episodes) {
		LOG.debug("Input parameters -> episodes {}", episodes);
		boolean result = episodes.stream().anyMatch(InternmentEpisode::isActive);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public Integer updateInternmentEpisodeBed(Integer internmentEpisodeId, Integer newBedId) {
		LOG.debug("Input parameters -> internmentEpisodeId {}, newBedId {}", internmentEpisodeId, newBedId);
		Integer currentUser = UserInfo.getCurrentAuditor();
		Integer oldBed = internmentEpisodeRepository.getOne(internmentEpisodeId).getBedId();
		internmentEpisodeRepository.updateInternmentEpisodeBed(internmentEpisodeId,newBedId, currentUser, LocalDateTime.now());
		LOG.debug(LOGGING_OUTPUT, newBedId);
		return oldBed;
	}

}
