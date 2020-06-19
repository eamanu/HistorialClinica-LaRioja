package net.pladema.clinichistory.hospitalization.controller.documents.epicrisis;

import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import net.pladema.clinichistory.hospitalization.controller.constraints.DocumentValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentType;
import net.pladema.clinichistory.hospitalization.service.epicrisis.EpicrisisReportService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.pdf.service.PdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/epicrisis-report")
@Api(value = "Epicrisis Report", tags = { "Epicrisis Report" })
@Validated
public class EpicrisisReportController {

    private static final Logger LOG = LoggerFactory.getLogger(EpicrisisReportController.class);

    public static final String OUTPUT = "Output -> {}";
    public static final String INVALID_EPISODE = "internmentepisode.invalid";

    private final InternmentEpisodeService internmentEpisodeService;

    private final EpicrisisReportService epicrisisReportService;

    private final PdfService pdfService;

    public EpicrisisReportController(InternmentEpisodeService internmentEpisodeService,
                                     EpicrisisReportService epicrisisReportService,
                                     PdfService pdfService) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.epicrisisReportService = epicrisisReportService;
        this.pdfService = pdfService;
    }


    @GetMapping("/{epicrisisId}")
    @InternmentValid
    @DocumentValid(isConfirmed = true, documentType = DocumentType.EPICRISIS)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO')")
    public ResponseEntity<InputStreamResource> getPDF(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "epicrisisId") Long epicrisisId) throws IOException, DocumentException {

        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, epicrisisId {}",
                institutionId, internmentEpisodeId, epicrisisId);
        EpicrisisBo epicrisis = epicrisisReportService.getDocument(epicrisisId);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_EPISODE));
        String name = "Epicrisis_" + epicrisis.getId() ;
        return pdfService.getResponseEntityPdf(name, "epicrisis", pdfService.buildContext(epicrisis, patientId));
    }
}