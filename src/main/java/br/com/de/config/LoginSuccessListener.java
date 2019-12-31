package br.com.de.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

	private static final Logger logger = LoggerFactory.getLogger(LoginSuccessListener.class);

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent evt) {
		logger.info("logado com sucesso: " + evt.getAuthentication().getName());

	}
}