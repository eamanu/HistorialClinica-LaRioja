package net.pladema.federar.services.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.restclient.services.domain.LoginResponse;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FederarLoginResponse implements LoginResponse {
	private String accessToken;
	private String tokenType;
	private String expiresIn;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public FederarLoginResponse(@JsonProperty("accessToken") String accessToken, @JsonProperty("tokenType") String tokenType,
			@JsonProperty("expiresIn") String expiresIn) {
		this.accessToken = accessToken;
		this.tokenType = tokenType;
		this.expiresIn = expiresIn;
	}
	
	public String getToken() {
		return accessToken;
	}

	
}
