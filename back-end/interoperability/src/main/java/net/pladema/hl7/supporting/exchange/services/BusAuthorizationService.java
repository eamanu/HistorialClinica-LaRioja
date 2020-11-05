package net.pladema.hl7.supporting.exchange.services;

import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.hl7.supporting.exchange.services.federar.FederarLoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class BusAuthorizationService extends RestTemplate {

    @Value("${ws.federar.url.login:/bus-auth/tokeninfo}")
    private String relativeUrl;

    private final FederarConfig configuration;

    public BusAuthorizationService(FederarConfig configuration){
        super();
        this.configuration=configuration;
    }

    @Getter
    @Setter
    @ToString
    public static class AuthorizationPayload {

        private String accessToken;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public AuthorizationPayload(@JsonProperty("accessToken") String accessToken) {
            this.accessToken = accessToken;
        }

        public String getToken() {
            return accessToken;
        }
    }

    public ResponseEntity<FederarLoginResponse> validate(String accessToken) throws RestClientException {
        ResponseEntity<FederarLoginResponse> result;
        try {
            result = exchangePost(relativeUrl,
                    new AuthorizationPayload(accessToken), FederarLoginResponse.class);
        } catch (RestClientException e) {
            throw new AuthenticationException("Invalid access token: " + e.getMessage() ) ;
        }
        return result;
    }

    private <ResponseBody, RequestBody> ResponseEntity<ResponseBody> exchangePost(String relUrl,
                                                                                  RequestBody requestBody,
                                                                                  Class<ResponseBody> responseType) {
        String fullUrl = configuration.getAbsoluteURL(relUrl);
        HttpEntity<RequestBody> entity = new HttpEntity<>(requestBody, getHeaders());
        return exchange(fullUrl, HttpMethod.POST, entity, responseType);
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        return headers;
    }
}