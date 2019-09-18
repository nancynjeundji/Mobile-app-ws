package com.nancy.app.ws.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nancy.app.ws.SpringApplicationContext;
import com.nancy.app.ws.service.UserService;
import com.nancy.app.ws.shared.dto.UserDto;
import com.nancy.app.ws.ui.model.request.UserLoginRequestModel;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/** cette classe vérifie les informations d'authen 
 * et en cas de succès rajoute le token dans le header 
 * pour que l'utilisateur puisse effectuer des requetes
 * 
 * C'est un paramètre important de la méthode configure de WebSecurity
 * */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	/** fonction SpringSécu qui gère l'authent,  elle utilise les données en base (via loadUserByUsername) et fait la vérif, 
	 * si l'authent est ok, on trigger la méthode successfulAuthentication*/
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {	
			
			UserLoginRequestModel creds = new ObjectMapper()
											.readValue(request.getInputStream(),
														UserLoginRequestModel.class);
			
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							creds.getEmail(),
							creds.getPassword(),
							new ArrayList<>()));
	
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Une fois que le user est authentifié OK, 
	 * Cette méthode sera appelée pour rajouter le token 
	 * dans le header de telle sorte que cet user puisse acceder aux ressources
	 * de l'application */
	
	@Override
	protected void successfulAuthentication( HttpServletRequest req, 
											HttpServletResponse res,
											FilterChain chain,
											Authentication auth) throws IOException, ServletException{
		
		String userName = ((User) auth.getPrincipal()).getUsername();// le userName c'est le login, dans cette appli il s'agit de l'email
		
		//construction du token
		String token = Jwts.builder()
						.setSubject(userName)
						.setExpiration( new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
						.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
						.compact();
		
		//récupération de l'ID de l'user
		UserService userService = (UserService)SpringApplicationContext.getBean("userServiceImpl");
		
		UserDto userDto = userService.getUser(userName);
		
		// configuration du header de la réponse
		res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
		res.addHeader("UserID", userDto.getUserId());
	}

}
