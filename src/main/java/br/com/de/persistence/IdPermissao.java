package br.com.de.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class IdPermissao implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Column(name = "id_usuario")
	private Integer idUsuario;
	@Column(name = "permissao")
	@Enumerated(EnumType.STRING)
	private TipoPermissao tipoPermissao;

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public TipoPermissao getTipoPermissao() {
		return tipoPermissao;
	}

	public void setTipoPermissao(TipoPermissao tipoPermissao) {
		this.tipoPermissao = tipoPermissao;
	}

}
