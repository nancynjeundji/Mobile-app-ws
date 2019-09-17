package com.nancy.app.ws.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.nancy.app.ws.service.UserService;

@EnableWebSecurity
public class WebSecurity  extends WebSecurityConfigurerAdapter{
	
	private final UserService userDetailsService;// ici il faut utiliser notre interface csutom qui doit étendre UserDetailService de SpringSécu
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public WebSecurity(UserService userDetailsService, 
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
	/* en gros, les méthodes de type void vont bypasser la sécurité mais pas les autres
	les autres auront besoin du token pour acceder aux ressources*/
		
		http.csrf()
			.disable()
			.authorizeRequests()
//			.antMatchers(HttpMethod.POST, "/users")
			.antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.addFilter(new AuthenticationFilter(authenticationManager()));//Voir la classe AuthenticationFilter
			
	}
	
	
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}
}
