package net.pladema.medicalconsultation.diary.service.impl;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.pladema.medicalconsultation.diary.repository.DiaryRepository;
import net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo;
import net.pladema.medicalconsultation.diary.repository.entity.Diary;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.CompleteDiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;

@Service
public class DiaryServiceImpl implements DiaryService {

	private static final Logger LOG = LoggerFactory.getLogger(DiaryServiceImpl.class);

	private final DiaryOpeningHoursService diaryOpeningHoursService;

	private final DiaryRepository diaryRepository;

	public DiaryServiceImpl(DiaryOpeningHoursService diaryOpeningHoursService, DiaryRepository diaryRepository) {
		super();
		this.diaryOpeningHoursService = diaryOpeningHoursService;
		this.diaryRepository = diaryRepository;
	}

	@Override
	public Integer addDiary(DiaryBo diaryToSave) {
		LOG.debug("Input parameters -> diaryToSave {}", diaryToSave);

		Diary diary = createDiaryInstance(diaryToSave);
		diary = diaryRepository.save(diary);

		Integer diaryId = diary.getId();

		diaryOpeningHoursService.load(diaryId, diaryToSave.getDiaryOpeningHours());

		diaryToSave.setId(diaryId);
		LOG.debug("Diary saved -> {}", diaryToSave);
		return diaryId;
	}

	private Diary createDiaryInstance(DiaryBo diaryBo) {
		Diary diary = new Diary();
		diary.setHealthcareProfessionalId(diaryBo.getHealthcareProfessionalId());
		diary.setDoctorsOfficeId(diaryBo.getDoctorsOfficeId());
		diary.setStartDate(diaryBo.getStartDate());
		diary.setEndDate(diaryBo.getEndDate());
		diary.setAppointmentDuration(diaryBo.getAppointmentDuration());
		diary.setAutomaticRenewal(diaryBo.getAutomaticRenewal());
		diary.setProfessionalAsignShift(diaryBo.getProfessionalAsignShift());
		diary.setIncludeHoliday(diaryBo.getIncludeHoliday());
		diary.setActive(true);
		return diary;
	}

	/**
	 *
	 * @param healthcareProfessionalId ID profesional de salud
	 * @param doctorsOfficeId          ID consultorio
	 * @param newDiaryStart            nueva fecha de comienzo para agenda
	 * @param newDiaryEnd              nueva fecha de fin para agenda
	 * @return lista con todos los ID de agendas definidas en rangos de fecha
	 *         superpuestas para un mismo profesional de salud y consultorio.
	 */
	@Override
	public List<Integer> getAllOverlappingDiary(Integer healthcareProfessionalId, Integer doctorsOfficeId,
			LocalDate newDiaryStart, LocalDate newDiaryEnd) {
		LOG.debug(
				"Input parameters -> healthcareProfessionalId {}, doctorsOfficeId {}, newDiaryStart {}, newDiaryEnd {}",
				healthcareProfessionalId, doctorsOfficeId, newDiaryStart, newDiaryEnd);
		List<Integer> diaryIds = diaryRepository.findAllOverlappingDiary(healthcareProfessionalId, doctorsOfficeId,
				newDiaryStart, newDiaryEnd);
		LOG.debug("Diary saved -> {}", diaryIds);
		return diaryIds;

	}

	@Override
	public Collection<DiaryBo> getActiveDiariesFromProfessional(Integer healthcareProfessionalId) {
		LOG.debug("Input parameters -> healthcareProfessionalId {}", healthcareProfessionalId);
		List<DiaryListVo> diaries = diaryRepository.getActiveDiariesFromProfessional(healthcareProfessionalId);
		List<DiaryBo> result = diaries.stream().map(this::createDiaryBoInstance).collect(Collectors.toList());
		LOG.debug("Output -> {}", result);
		return result;
	}

	private DiaryBo createDiaryBoInstance(DiaryListVo diaryListVo) {
		LOG.debug("Input parameters -> diaryListVo {}", diaryListVo);
		DiaryBo result = new DiaryBo();
		result.setId(diaryListVo.getId());
		result.setDoctorsOfficeId(diaryListVo.getDoctorsOfficeId());
		result.setStartDate(diaryListVo.getStartDate());
		result.setEndDate(diaryListVo.getEndDate());
		result.setAppointmentDuration(diaryListVo.getAppointmentDuration());
		result.setProfessionalAsignShift(diaryListVo.getProfessionalAssignShift());
		result.setIncludeHoliday(diaryListVo.getIncludeHoliday());
		LOG.debug("Output -> {}", result);
		return result;
	}

	private CompleteDiaryBo createCompleteDiaryBoInstance(CompleteDiaryListVo completeDiaryListVo) {
		LOG.debug("Input parameters -> diaryListVo {}", completeDiaryListVo);
		CompleteDiaryBo result = new CompleteDiaryBo(createDiaryBoInstance(completeDiaryListVo));
		result.setSectorId(completeDiaryListVo.getSectorId());
		result.setClinicalSpecialtyId(completeDiaryListVo.getClinicalSpecialtyId());
		LOG.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Optional<CompleteDiaryBo> getDiary(Integer diaryId) {
		LOG.debug("Input parameters -> diaryId {}", diaryId);
		Optional<CompleteDiaryBo> result = diaryRepository.getDiary(diaryId).map(this::createCompleteDiaryBoInstance)
				.map(completeOpeningHours());
		LOG.debug("Output -> {}", result);
		return result;
	}

	private Function<CompleteDiaryBo, CompleteDiaryBo> completeOpeningHours() {
		return (completeDiary) -> {
			Collection<DiaryOpeningHoursBo> diaryOpeningHours = diaryOpeningHoursService
					.getDiariesOpeningHours(Stream.of(completeDiary.getId()).collect(Collectors.toList()));
			completeDiary.setDiaryOpeningHours(diaryOpeningHours.stream().collect(Collectors.toList()));
			return completeDiary;
		};
	}

}