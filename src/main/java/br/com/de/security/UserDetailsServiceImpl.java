package br.com.de.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.de.persistence.Permissao;
import br.com.de.persistence.Usuario;
import br.com.de.persistence.UsuarioRepository;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
		logger.info("Tentativa de login por: " + email);
		Usuario user = usuarioRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("Nenhum usuário encontrado com o e-mail: " + email);
		}		
		return new User(user.getEmail(), user.getSenha(), true, true, true, true, getAuthorities(user.getPermissoes()));
	}

	private Collection<? extends GrantedAuthority> getAuthorities(List<Permissao> role) {
		return role.stream().map((r) -> new SimpleGrantedAuthority(r.getTipoPermissao().toString()))
				.collect(Collectors.toList());
	}

}
