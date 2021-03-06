package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import net.pladema.UnitRepository;
import net.pladema.clinichistory.documents.repository.ips.entity.HealthCondition;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ConditionClinicalStatus;
import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.HealthConditionService;
import net.pladema.clinichistory.documents.service.ips.domain.*;
import net.pladema.clinichistory.documents.service.ips.domain.enums.EUnitsOfTimeBo;
import net.pladema.clinichistory.requests.medicationrequests.repository.MedicationRequestRepository;
import net.pladema.clinichistory.requests.medicationrequests.service.CreateMedicationRequestService;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationRequestBo;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class CreateMedicationRequestServiceImplTest extends UnitRepository {

	private CreateMedicationRequestService createMedicationRequestService;

	@Autowired
	private MedicationRequestRepository medicationRequestRepository;

	@MockBean
	private DocumentFactory documentFactory;

	@MockBean
	private HealthConditionService healthConditionService;

	@Before
	public void setUp() {
		createMedicationRequestService = new CreateMedicationRequestServiceImpl(medicationRequestRepository, documentFactory, healthConditionService);
	}

	@Test
	public void execute_withNullInstitution(){
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(null, null)
		);
		String expectedMessage = "El identificador de la instituci??n es obligatorio";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void execute_withNullMedicationRequest(){
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
			createMedicationRequestService.execute(1, null)
		);
		String expectedMessage = "La receta es obligatoria";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void execute_withNullPatient(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(1, medicationRequest)
		);
		String expectedMessage = "La informaci??n del paciente es obligatoria";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void execute_withNullDoctorId(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setPatientInfo(new PatientInfoBo(1, (short)1, (short)29));
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(1, medicationRequest)
		);
		String expectedMessage = "El identificador del m??dico es obligatorio";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void execute_withEmptyMedications(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setPatientInfo(new PatientInfoBo(1, (short)1, (short)29));
		medicationRequest.setDoctorId(1);
		medicationRequest.setMedicalCoverageId(5);
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(1, medicationRequest)
		);
		String expectedMessage = "La receta tiene que tener asociada al menos una medicaci??n";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void execute_withInvalidMedication_startDate(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setPatientInfo(new PatientInfoBo(1, (short)1, (short)29));
		medicationRequest.setDoctorId(1);
		medicationRequest.setMedicalCoverageId(5);

		MedicationBo medication = new MedicationBo();
		HealthConditionBo hc = new HealthConditionBo();
		hc.setId(1);
		medication.setHealthCondition(hc);
		medication.setSnomed(new SnomedBo("12312", "ANGINA"));

		DosageBo dosage = new DosageBo();
		dosage.setStartDate(null);
		medication.setDosage(dosage);

		medicationRequest.setMedications(List.of(medication));
		when(healthConditionService.getHealthCondition(any())).thenReturn(mockActiveHealthCondition());

		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(1, medicationRequest)
		);
		String expectedMessage = "La fecha de comienzo para tomar la medicaci??n es obligatoria";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void execute_withInvalidMedication_Snomed(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setPatientInfo(new PatientInfoBo(1, (short)1, (short)29));
		medicationRequest.setDoctorId(1);
		medicationRequest.setMedicalCoverageId(5);

		MedicationBo medication = new MedicationBo();
		medication.setSnomed(null);
		medicationRequest.setMedications(List.of(medication));
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(1, medicationRequest)
		);
		String expectedMessage = "La terminolog??a snomed es obligatoria";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));

		medication.setSnomed(new SnomedBo(null, "ANGINA"));
		medicationRequest.setMedications(List.of(medication));
		exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(1, medicationRequest)
		);
		expectedMessage = "El c??digo identificador de snomed es obligatorio";
		actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));

		medication.setSnomed(new SnomedBo("12314124", null));
		medicationRequest.setMedications(List.of(medication));
		exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(1, medicationRequest)
		);
		expectedMessage = "El termino preferido de snomed es obligatorio";
		actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void execute_withInvalidMedication_HealthCondition_Snomed(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setPatientInfo(new PatientInfoBo(1, (short)1, (short)29));
		medicationRequest.setDoctorId(1);
		medicationRequest.setMedicalCoverageId(5);


		MedicationBo medication = new MedicationBo();
		medication.setSnomed(new SnomedBo("12314124", "IBUPROFENO 500 mg"));
		medication.setHealthCondition(null);
		medicationRequest.setMedications(List.of(medication));
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(1, medicationRequest)
		);
		String expectedMessage = "La medicaci??n tiene que estar asociada a un problema";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));

		medication = new MedicationBo();
		medication.setSnomed(new SnomedBo("12314124", "IBUPROFENO 500 mg"));
		HealthConditionBo hc = new HealthConditionBo();
		hc.setId(null);
		medication.setHealthCondition(hc);
		medicationRequest.setMedications(List.of(medication));
		exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(1, medicationRequest)
		);
		expectedMessage = "La medicaci??n tiene que estar asociada a un problema";
		actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void execute_withNewerSolvedHeathCondition(){
		Integer institutionId = 5;

		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setDoctorId(1);
		medicationRequest.setPatientInfo(new PatientInfoBo(4, (short)1, (short)29));
		medicationRequest.setMedicalCoverageId(5);
		medicationRequest.setMedications(List.of(
				createMedicationBo(
						"IBUPROFENO 500",
						1,
						ConditionClinicalStatus.ACTIVE,
						createDosageBo(15d, 8, EUnitsOfTimeBo.HOUR))));

		when(healthConditionService.getLastHealthCondition(any(), any())).thenReturn(mockHealthConditionMapWhithNewerHealthCondition());

		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(institutionId, medicationRequest)
		);
		String expectedMessage = "El problema asociado tiene que estar activo";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void execute_withDuplicatedStudy() {

		Integer institutionId = 5;
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setDoctorId(1);
		medicationRequest.setPatientInfo(new PatientInfoBo(4, (short) 1, (short) 29));
		medicationRequest.setMedicalCoverageId(5);

		String repetedSnomedSctid = "IBUPROFENO 500";

		medicationRequest.setMedications(List.of(
				createMedicationBo(
						"Bayaspirina",
						1,
						ConditionClinicalStatus.ACTIVE,
						createDosageBo(30d, 1, EUnitsOfTimeBo.DAY)),
				createMedicationBo(
						repetedSnomedSctid,
						1,
						ConditionClinicalStatus.ACTIVE,
						createDosageBo(15d, 8, EUnitsOfTimeBo.HOUR)),
				createMedicationBo(
						repetedSnomedSctid,
						1,
						ConditionClinicalStatus.ACTIVE,
						createDosageBo(10d, 3, EUnitsOfTimeBo.HOUR))
		));


		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(institutionId, medicationRequest)
		);
		String expectedMessage = "La receta no puede contener m??s de un medicamento con el mismo problema y el mismo concepto snomed";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void execute_success() {
		Integer institutionId = 5;
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setDoctorId(1);
		medicationRequest.setPatientInfo(new PatientInfoBo(4, (short)1, (short)29));
		medicationRequest.setMedicalCoverageId(5);
		medicationRequest.setMedications(List.of(
		        createMedicationBo(
		                "IBUPROFENO 500",
                        1,
                        ConditionClinicalStatus.ACTIVE,
                        createDosageBo(15d, 8, EUnitsOfTimeBo.HOUR)),
                createMedicationBo(
                        "CLONA 200",
                        1,
                        ConditionClinicalStatus.ACTIVE,
                        createDosageBo(7d, 12, EUnitsOfTimeBo.HOUR))));
		when(healthConditionService.getHealthCondition(any())).thenReturn(mockActiveHealthCondition());
		when(healthConditionService.getLastHealthCondition(any(), any())).thenReturn(mockHealthConditionMap());
		Integer medicationRequestId = createMedicationRequestService.execute(institutionId, medicationRequest);

		Assertions.assertEquals(1, medicationRequestRepository.count());
		Assertions.assertNotNull(medicationRequestId);
	}

	private Map<Integer, HealthConditionBo> mockHealthConditionMap() {
		HealthConditionBo hc1 = new HealthConditionBo();
		hc1.setId(1);
		hc1.setStatusId(ConditionClinicalStatus.ACTIVE);
		List<HealthConditionBo> hcs = List.of(hc1);
		HashMap<Integer, HealthConditionBo> result = new HashMap<>();
		result.put(1,hc1);
		return result;
	}

	private Map<Integer, HealthConditionBo> mockHealthConditionMapWhithNewerHealthCondition() {
		HealthConditionBo hc1 = new HealthConditionBo();
		hc1.setId(47);
		hc1.setStatusId(ConditionClinicalStatus.SOLVED);

		HashMap<Integer, HealthConditionBo> result = new HashMap<>();
		result.put(1,hc1);
		return result;
	}

	private HealthConditionNewConsultationBo mockActiveHealthCondition(){
		HealthCondition hc = new HealthCondition();
		hc.setStatusId(ConditionClinicalStatus.ACTIVE);
		return new HealthConditionNewConsultationBo(hc);
	}

	private HealthConditionNewConsultationBo mockSolvedHealthCondition(){
		HealthCondition hc = new HealthCondition();
		hc.setStatusId(ConditionClinicalStatus.SOLVED);
		return new HealthConditionNewConsultationBo(hc);
	}

	private DosageBo createDosageBo(Double duration, Integer frequency, EUnitsOfTimeBo unitsOfTimeBo) {
		DosageBo result = new DosageBo();
		result.setStartDate(LocalDate.now());
		result.setDuration(duration);
		result.setFrequency(frequency);
		result.setPeriodUnit(unitsOfTimeBo);
		return result;
	}

	private MedicationBo createMedicationBo(String sctid, Integer healthConditionId, String healthConditionStatus, DosageBo dosage) {
		MedicationBo result = new MedicationBo();
		result.setSnomed(new SnomedBo(sctid, sctid));
		HealthConditionBo hc = new HealthConditionBo();
		hc.setId(healthConditionId);
		hc.setStatusId(healthConditionStatus);
		result.setHealthCondition(hc);
		result.setDosage(dosage);
		result.setNote("Probando");
		return result;
	}

}
