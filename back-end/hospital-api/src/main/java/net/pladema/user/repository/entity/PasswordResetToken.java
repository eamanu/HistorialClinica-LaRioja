package net.pladema.user.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="password_reset_token")
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetToken implements Serializable {

	private static final long serialVersionUID = -2775496112608602124L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "token", nullable = false, length = 200)
	private String token;

	@Column(name = "user_id", nullable = false)
	private Integer userId;

	@Column(name = "enable", nullable = false)
	private Boolean enable ;

	@Column(name = "expiry_date", nullable = false)
	private LocalDateTime expiryDate;

}
