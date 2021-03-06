package net.pladema.renaper.services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.pladema.renaper.configuration.RenaperCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import net.pladema.renaper.configuration.RenaperRestTemplateAuth;
import net.pladema.renaper.configuration.RenaperWSConfig;
import net.pladema.renaper.services.RenaperService;
import net.pladema.renaper.services.domain.PersonDataResponse;
import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;
import ar.lamansys.sgx.shared.restclient.services.RestClient;

@Service
@Conditional(RenaperCondition.class)
public class RenaperServiceImpl extends RestClient implements RenaperService {

	private RenaperWSConfig renaperWSConfig;
	
	public RenaperServiceImpl(RenaperRestTemplateAuth restTemplateAuth, RenaperWSConfig wsConfig) {
		super(restTemplateAuth, wsConfig);
		this.renaperWSConfig = wsConfig;
	}

	@Override
	public List<PersonMedicalCoverageBo> getPersonMedicalCoverage(String nroDocumento, Short idSexo) {
		String urlWithParams = renaperWSConfig.getUrlCobertura() + "?nroDocumento=" + nroDocumento + "&idSexo=" + idSexo; 
		ResponseEntity<PersonMedicalCoverageBo[]> response = exchangeGet(urlWithParams, PersonMedicalCoverageBo[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public Optional<PersonDataResponse> getPersonData(String nroDocumento, Short idSexo) {
		String urlWithParams = renaperWSConfig.getUrlPersona() + "?nroDocumento=" + nroDocumento + "&idSexo=" + idSexo; 
		ResponseEntity<PersonDataResponse> response = exchangeGet(urlWithParams, PersonDataResponse.class);
		return Optional.ofNullable(response.getBody());
	}

	
}
