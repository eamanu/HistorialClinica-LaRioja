package net.pladema.renaper.services;

import net.pladema.renaper.configuration.RenaperWSConfig;
import net.pladema.renaper.services.domain.RenaperLoginPayload;
import net.pladema.renaper.services.domain.RenaperLoginResponse;
import ar.lamansys.sgx.restclient.configuration.interceptors.LoggingRequestInterceptor;
import ar.lamansys.sgx.restclient.configuration.resttemplate.RestTemplateSSL;
import ar.lamansys.sgx.restclient.services.AuthService;
import ar.lamansys.sgx.restclient.services.domain.WSResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RenaperAuthService extends AuthService<RenaperLoginResponse> {

	private static final Logger LOG = LoggerFactory.getLogger(RenaperAuthService.class);

	public static final String OUTPUT = "Output -> {}";

	private RenaperWSConfig renaperWSConfig;

	public RenaperAuthService(
			@Value("${ws.renaper.url.login:/usuarios/aplicacion/login}") String relUrl,
			RenaperWSConfig wsConfig) throws Exception {
		super(relUrl, new RestTemplateSSL(new LoggingRequestInterceptor()), wsConfig);
		renaperWSConfig = wsConfig;
	}

	@Override
	protected ResponseEntity<RenaperLoginResponse> callLogin()  throws WSResponseException {
		ResponseEntity<RenaperLoginResponse> result = exchangePost(relUrl, new RenaperLoginPayload(renaperWSConfig.getNombre(),
				renaperWSConfig.getClave(), renaperWSConfig.getDominio()), RenaperLoginResponse.class);
		LOG.debug(OUTPUT, result);
		return result;
	}

}
