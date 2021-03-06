package net.pladema.user.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PasswordResetDto {
	@NotNull(message = "{token.mandatory}")
	@NotBlank(message = "{token.mandatory}")
	private String token;
	@NotNull(message = "{password.mandatory}")
	@NotBlank(message = "{password.mandatory}")
	private String password;
}
