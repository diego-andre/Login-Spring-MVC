package br.com.de.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.com.de.persistence.Usuario;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Environment env;

	public void enviaEmailTokenRecuperacao(final String contextPath, final Usuario user, final String token) {
		final SimpleMailMessage email = constructResetTokenEmail(contextPath, user, token);
		mailSender.send(email);
	}

	protected SimpleMailMessage constructResetTokenEmail(final String contextPath, final Usuario user,
			final String token) {
		final String url = contextPath + "/changePassword?id=" + user.getCodigo() + "&token=" + token;
		final String recipientAddress = user.getEmail();
		final String subject = "Recuperação de senha";
		final String mensagem = "Sr.(a) usuário(a).\n\n" + "Segue URL para recuperação de senha.\n"
				+ "Atenciosamente, central de atendimento.\n\n"
				+ "Abra o seguinte URL para verificar sua conta: \r\n" + url + "\n\n" + "Obrigado!";
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(mensagem);
		email.setFrom(env.getProperty("support.email"));
		return email;
	}

}
