package br.com.sicred.votacao.modelo;

import java.util.UUID;

import br.com.sicred.votacao.enums.TipoVoto;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "pauta_id", "cpfAssociado" }) })
public class Voto {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private String cpfAssociado;

	@Enumerated(EnumType.STRING)
	private TipoVoto voto;

	@ManyToOne
	@JoinColumn(name = "pauta_id")
	private Pauta pauta;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getCpfAssociado() {
		return cpfAssociado;
	}

	public void setCpfAssociado(String cpfAssociado) {
		this.cpfAssociado = cpfAssociado;
	}

	public TipoVoto getVoto() {
		return voto;
	}

	public void setVoto(TipoVoto voto) {
		this.voto = voto;
	}

	public Pauta getPauta() {
		return pauta;
	}

	public void setPauta(Pauta pauta) {
		this.pauta = pauta;
	}

}
