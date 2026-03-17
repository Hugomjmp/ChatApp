package pt.hugo.LusApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import pt.hugo.LusApp.model.data.database.DB;
import pt.hugo.LusApp.model.services.AuthService;


@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private AuthService authService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		if (authService.validateCredentials(username,password)){
			return new UsernamePasswordAuthenticationToken(username, password, null);
		}
		throw new BadCredentialsException("User doesn't exist!");
	}



	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
