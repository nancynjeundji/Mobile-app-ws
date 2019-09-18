package com.nancy.app.ws.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;

/**Ce filter permet de recuper le token de l'user connecté et de verifier
 * s'il est authorisé à effectuer des opérations 
 */
public class AuthorizationFilter extends BasicAuthenticationFilter {

	public AuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req,
									HttpServletResponse res,
									FilterChain chain) throws IOException, ServletException {
		
		String header = req.getHeader(SecurityConstants.HEADER_STRING);
	
		// si le token ne commence pas par "Bearer ", aller au filtre suivant 
		if(header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)){
			chain.doFilter(req, res);
			return;
		}
		
		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
 	}
	
	
	//extraction du prefix du Token pour garder le secret
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
		
		String token = req.getHeader(SecurityConstants.HEADER_STRING);
		
		if(token != null) {
			
			token = token.replace(SecurityConstants.TOKEN_PREFIX, "");
			
			String user = Jwts.parser()
					.setSigningKey(SecurityConstants.getTokenSecret())
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
			
			if(user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}
			return null;
		}
		
		return null;
	}
}
