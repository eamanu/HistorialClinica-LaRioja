package net.pladema.emergencycare.controller;

import io.swagger.annotations.Api;
import net.pladema.emergencycare.service.EmergencyCareEpisodeStateService;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.sgx.masterdata.dto.MasterDataDto;
import net.pladema.sgx.masterdata.service.domain.EnumWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/institution/{institutionId}/emergency-care/episodes/{episodeId}/state")
@Api(value = "Emergency care Episodes State", tags = {"Emergency care Episodes State"})
public class EmergencyCareEpisodeStateController {

	private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeStateController.class);

	private final EmergencyCareEpisodeStateService emergencyCareEpisodeStateService;

	public EmergencyCareEpisodeStateController(EmergencyCareEpisodeStateService emergencyCareEpisodeStateService){
		this.emergencyCareEpisodeStateService = emergencyCareEpisodeStateService;
	}

	@GetMapping
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<MasterDataDto> getState(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "episodeId") Integer episodeId) {
		LOG.debug("Input parameters -> institutionId {}, episodeId {}", institutionId, episodeId);
		EEmergencyCareState result = emergencyCareEpisodeStateService.getState(episodeId, institutionId);
		LOG.debug("Output -> {}", result);
		return ResponseEntity.ok().body(EnumWriter.write(result));
	}

	@Transactional
	@PostMapping
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<Boolean> changeState(
			@PathVariable(name = "episodeId") Integer episodeId,
			@PathVariable(name = "institutionId") Integer institutionId,
			@RequestParam(name = "emergencyCareStateId") Short emergencyCareStateId,
			@RequestParam(name = "doctorsOfficeId", required = false) Integer doctorsOfficeId) {
		LOG.debug("Change emergency care state -> episodeId {}, institutionId {}, emergencyCareStateId {}, doctorsOfficeId {}",
				episodeId, institutionId, emergencyCareStateId, doctorsOfficeId);
		Boolean result = emergencyCareEpisodeStateService.changeState(episodeId, institutionId, emergencyCareStateId, doctorsOfficeId);
		LOG.debug("Output -> {}", result);
		return ResponseEntity.ok().body(result);
	}
}
