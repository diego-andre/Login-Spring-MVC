package br.com.de.controller;

import java.util.Calendar;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.ImmutableMap;

import br.com.de.persistence.PasswordResetToken;
import br.com.de.persistence.Usuario;
import br.com.de.service.EmailService;
import br.com.de.service.UserService;

@Controller
class RegistrationController {

	private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserDetailsService userDetailsService;

	@RequestMapping("/forgotPassword")
	public String recupera() {
		return "forgotPassword";
	}

	@PostMapping("/resetPassword")
	@ResponseBody
	public ModelAndView resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail,
			final RedirectAttributes redirectAttributes) {
		final Usuario user = userService.findUserByEmail(userEmail);
		logger.info("Tentativa de recuperação de senha por: " + user.getEmail());
		if (user != null) {
			final String token = UUID.randomUUID().toString();
			userService.createPasswordResetTokenForUser(user, token);
			final String appUrl = request.isSecure() ? "https://"
					: "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
			emailService.enviaEmailTokenRecuperacao(appUrl, user, token);
		}
		redirectAttributes.addFlashAttribute("message", "Você deve receber um email de redefinição de senha em breve");
		return new ModelAndView("redirect:/login");
	}

	@GetMapping("/changePassword")
	public ModelAndView showChangePasswordPage(@RequestParam("id") final long id,
			@RequestParam("token") final String token, final RedirectAttributes redirectAttributes) {
		final PasswordResetToken passToken = userService.getPasswordResetToken(token);
		logger.info("Tentativa de validadção de token: " + token);
		if (passToken == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Token de redefinição de senha inválido");
			return new ModelAndView("redirect:/login");
		}
		final Usuario user = passToken.getUser();
		if (user.getCodigo() != id) {
			redirectAttributes.addFlashAttribute("errorMessage", "Token de redefinição de senha inválido");
			return new ModelAndView("redirect:/login");
		}

		final Calendar cal = Calendar.getInstance();
		if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			redirectAttributes.addFlashAttribute("errorMessage", "Seu token de reposição de senha expirou");
			return new ModelAndView("redirect:/login");
		}

		final Authentication auth = new UsernamePasswordAuthenticationToken(user, null,
				userDetailsService.loadUserByUsername(user.getEmail()).getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
		logger.info("Acessando redefinição de senha. Token validado com sucesso!");
		return new ModelAndView("resetPassword");
	}

	@PostMapping("/savePassword")
	@ResponseBody
	public ModelAndView savePassword(@RequestParam("password") final String password,
			@RequestParam("passwordConfirmation") final String passwordConfirmation,
			final RedirectAttributes redirectAttributes) {
		logger.info("Salvando a alteração de senha!");
		if (!password.equals(passwordConfirmation)) {
			return new ModelAndView("resetPassword", ImmutableMap.of("errorMessage", "As senhas não coincidem"));
		}
		final Usuario user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		userService.changeUserPassword(user, password);
		redirectAttributes.addFlashAttribute("message", "Redefinição de senha com sucesso");
		return new ModelAndView("redirect:/login");
	}

}
