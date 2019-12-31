package br.com.de.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.de.persistence.PasswordResetToken;
import br.com.de.persistence.PasswordResetTokenRepository;
import br.com.de.persistence.Usuario;
import br.com.de.persistence.UsuarioRepository;

@Service
@Transactional
public class UserService {

	@Autowired
	private UsuarioRepository userRepository;

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	public Usuario findUserByEmail(final String email) {
		return userRepository.findByEmail(email);
	}

	public void createPasswordResetTokenForUser(final Usuario user, final String token) {
		final PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(myToken);
	}

	public PasswordResetToken getPasswordResetToken(final String token) {
		return passwordResetTokenRepository.findByToken(token);
	}

	public void changeUserPassword(final Usuario user, final String password) {
		user.setSenha(password);
		userRepository.save(user);
	}

}
