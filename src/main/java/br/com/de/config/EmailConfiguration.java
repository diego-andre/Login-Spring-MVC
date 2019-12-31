package br.com.de.config;

import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import br.com.de.utils.EncryptionUtil;

@Configuration
@EnableConfigurationProperties(MailProperties.class)
public class EmailConfiguration {

	@Autowired
	private MailProperties mailProperties;

	@Bean
	public JavaMailSender getMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(mailProperties.getHost());
		mailSender.setPort(mailProperties.getPort());
		mailSender.setUsername(mailProperties.getUsername());
		mailSender.setPassword(EncryptionUtil.decrypt(mailProperties.getPassword()));
		mailSender.setJavaMailProperties(getMailProperties(mailProperties.getProperties()));

		return mailSender;
	}

	private Properties getMailProperties(Map<String, String> map) {
		Properties mailProperties = new Properties();
		mailProperties.putAll(map);
		return mailProperties;
	}

}
