package br.com.de.persistence;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "permissao")
public class Permissao {
	
	@EmbeddedId
	private IdPermissao id;
	@Column(name = "permissao", insertable= false, updatable= false)
	@Enumerated(EnumType.STRING)
	private TipoPermissao tipoPermissao;

	
	public TipoPermissao getTipoPermissao() {
		return tipoPermissao;
	}

	public void setTipoPermissao(TipoPermissao tipoPermissao) {
		this.tipoPermissao = tipoPermissao;
	}

}
