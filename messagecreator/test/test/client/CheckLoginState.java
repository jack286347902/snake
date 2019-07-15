package test.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class CheckLoginState {
	
	@NonNull
	private String token;


}
