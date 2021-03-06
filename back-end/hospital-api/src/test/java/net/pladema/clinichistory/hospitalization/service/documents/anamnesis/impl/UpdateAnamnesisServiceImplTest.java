package net.pladema.clinichistory.hospitalization.service.documents.anamnesis.impl;

import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.impl.UpdateAnamnesisServiceImpl;
import net.pladema.clinichistory.documents.service.ips.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
public class UpdateAnamnesisServiceImplTest {

	private static final String TOKEN = "TOKEN";

	private UpdateAnamnesisServiceImpl updateAnamnesisServiceImpl;

	@MockBean
	private DocumentService documentService;

	@MockBean
	private InternmentEpisodeService internmentEpisodeService;

	@MockBean
	private NoteService noteService;

	@MockBean
	private HealthConditionService healthConditionService;

	@MockBean
	private AllergyService allergyService;

	@MockBean
	private CreateMedicationService createMedicationService;

	@MockBean
	private ClinicalObservationService clinicalObservationService;

	@MockBean
	private ImmunizationService immunizationService;

	@Before
	public void setUp() {
		updateAnamnesisServiceImpl = new UpdateAnamnesisServiceImpl(documentService, internmentEpisodeService,
				noteService, healthConditionService, allergyService, createMedicationService, clinicalObservationService,
                immunizationService);
	}

	@Test
	public void test() {
		assertTrue(true);
	}

}
