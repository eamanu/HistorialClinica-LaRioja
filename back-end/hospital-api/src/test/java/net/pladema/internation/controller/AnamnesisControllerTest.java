package net.pladema.internation.controller;

import net.pladema.BaseControllerTest;
import net.pladema.internation.controller.mapper.AnamnesisMapper;
import net.pladema.internation.service.InternmentEpisodeService;
import net.pladema.internation.service.documents.anamnesis.AnamnesisService;
import net.pladema.internation.service.documents.anamnesis.CreateAnamnesisService;
import net.pladema.internation.service.documents.anamnesis.UpdateAnamnesisService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(AnamnesisController.class)
public class AnamnesisControllerTest extends BaseControllerTest {

	@MockBean
	private InternmentEpisodeService internmentEpisodeService;

	@MockBean
	private CreateAnamnesisService createAnamnesisService;

	@MockBean
	private UpdateAnamnesisService updateAnamnesisService;

	@MockBean
	private AnamnesisService anamnesisService;

	@MockBean
	private AnamnesisMapper anamnesisMapper;

	@Before
	public void setup() {
	}

	@Test
	public void test() {
	}

}
