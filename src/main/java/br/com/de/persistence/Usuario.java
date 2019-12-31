package br.com.de.persistence;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import br.com.de.utils.EncryptionUtil;

@Entity
@Table(name = "usuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	private String email;
	private String senha;
	@Column(name = "ativo")
	private Boolean enabled;
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_usuario")
	private List<Permissao> permissoes;

	public Usuario() {		
		this.enabled = false;
	}

	@PreUpdate
	public void onPreUpdate() {
		this.senha = EncryptionUtil.encrypt(senha);
	}

	@PostLoad
	public void posFindUser() {
		this.senha = EncryptionUtil.decrypt(senha);
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

}