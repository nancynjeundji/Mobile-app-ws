package com.nancy.app.ws.security;

import com.nancy.app.ws.SpringApplicationContext;

// tout ce qu'on met dans le postman comme variable pour effectuer une requete
//parce qu'en vrai le final user il utilise pas postman LOL
public class SecurityConstants {

	public static final long EXPIRATION_TIME = 864000000; // 10 jours
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users";
	//public static final String TOKEN_SECRET = "jf9i4jgu83nfl0";
	
	//le secret token est dans l'application.properties
	public static String getTokenSecret() {
		AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
		 return appProperties.getTokenSecret();
	}
}
/*TODO A un moment il va falloir mettre des valeurs comme SIGN_UP_URL et TOKEN_SECRET 
 * dans un property files pour ne pas les gerer dans le code*/
