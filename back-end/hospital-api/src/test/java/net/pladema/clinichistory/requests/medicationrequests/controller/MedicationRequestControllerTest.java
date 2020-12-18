package net.pladema.clinichistory.requests.medicationrequests.controller;

import net.pladema.UnitController;
import net.pladema.clinichistory.requests.medicationrequests.controller.mapper.CreateMedicationRequestMapper;
import net.pladema.clinichistory.requests.medicationrequests.controller.mapper.ListMedicationInfoMapper;
import net.pladema.clinichistory.requests.medicationrequests.service.CreateMedicationRequestService;
import net.pladema.clinichistory.requests.medicationrequests.service.ListMedicationInfoService;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MedicationRequestController.class)
public class MedicationRequestControllerTest extends UnitController {

    private String BASE_PATH = "/institutions/{institutionId}/patient/{patientId}/medication-requests";

    @MockBean
    private CreateMedicationRequestService createMedicationRequestService;

    @MockBean
    private HealthcareProfessionalExternalService healthcareProfessionalExternalService;

    @MockBean
    private CreateMedicationRequestMapper createMedicationRequestMapper;

    @MockBean
    private ListMedicationInfoMapper medicationRequestMapper;

    @MockBean
    private ListMedicationInfoService listMedicationInfoService;

    @Before
    public void setup() {

    }

    @Test
    @WithMockUser(authorities = {"ROOT"})
    public void test_createWithoutSnomed_fail() throws Exception {
        String URL = BASE_PATH
                    .replace("{institutionId}","1")
                    .replace("{patientId}", "1");
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockWithoutSnomed()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*")
                        .value(buildMessage("value.mandatory")));
    }


    private String mockWithoutSnomed() {
        return "{\n" +
                "  \"hasRecipe\": true,\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"dosage\": {\n" +
                "        \"chronic\": true,\n" +
                "        \"diary\": true,\n" +
                "        \"duration\": 0,\n" +
                "        \"frequency\": 0\n" +
                "      },\n" +
                "      \"healthConditionId\": 1,\n" +
                "      \"observations\": \"string\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"medicalCoverageId\": 0\n" +
                "}";
    }

    @Test
    @WithMockUser(authorities = {"ROOT"})
    public void test_createWithoutItems_fail() throws Exception {
        String URL = BASE_PATH
                .replace("{institutionId}","1")
                .replace("{patientId}", "1");
        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mockWithoutItems()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.items")
                        .value("must not be empty"));
    }

    private String mockWithoutItems() {
        return "{\n" +
                "  \"hasRecipe\": true,\n" +
                "  \"items\": [],\n" +
                "  \"medicalCoverageId\": 0\n" +
                "}";
    }

    @Test
    @WithMockUser(authorities = {"ROOT"})
    public void test_createWithoutHealthConditionId_fail() throws Exception {
        String URL = BASE_PATH
                .replace("{institutionId}","1")
                .replace("{patientId}", "1");
        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mockWithoutHealthConditionId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*")
                        .value(buildMessage("value.mandatory")));
    }

    private String mockWithoutHealthConditionId() {
        return "{\n" +
                "  \"hasRecipe\": true,\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"dosage\": {\n" +
                "        \"chronic\": true,\n" +
                "        \"diary\": true,\n" +
                "        \"duration\": 0,\n" +
                "        \"frequency\": 0\n" +
                "      },\n" +
                "      \"observations\": \"string\",\n" +
                "      \"snomed\": {\n" +
                "        \"id\": \"string\",\n" +
                "        \"parentFsn\": \"string\",\n" +
                "        \"parentId\": \"string\",\n" +
                "        \"pt\": \"string\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"medicalCoverageId\": 0\n" +
                "}";
    }


}