package net.pladema.user.controller.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.permissions.controller.dto.BackofficeUserRoleDto;

@Getter
@Setter
@NoArgsConstructor
public class BackofficeUserDto {
	private Integer id;
	private Integer personId;
	private String username;
	private Boolean enable;
	private LocalDateTime lastLogin;
	private List<BackofficeUserRoleDto> roles;
	
	@JsonIgnore
	public List<Short> getRolesIds() {
		return roles
				.stream()
				.map(BackofficeUserRoleDto::getRoleId)
				.collect(Collectors.toList());
	}
	
	public void setRoles(List<BackofficeUserRoleDto> roles) {
		this.roles = roles;
		if (this.roles != null)
			this.roles.forEach(x -> x.setUserId(this.getId()));
	}
	
	public List<BackofficeUserRoleDto> getRoles(){
		return this.roles != null ? this.roles : new ArrayList<>();
	}
	
}
