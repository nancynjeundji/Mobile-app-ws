package com.nancy.app.ws;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/** l'idée de la creation de cette classe serait d'utiliser la classe UserServiceImpl
 * en tant que bean afin d'avoir accès à certaines informations de l'utilisateur CONNECTé 
 * (par exple l'id_user)
 * partout dans l'appli*/
public class SpringApplicationContext implements ApplicationContextAware {

	private static ApplicationContext CONTEXT;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {

		CONTEXT = context;
	}

	public static Object getBean(String beanName) {
		return CONTEXT.getBean(beanName);
	}

}
