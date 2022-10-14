package chocoteamteam.togather.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class FCMRegisterTokenForm {

	@NotEmpty
	private String registrationToken;

}
