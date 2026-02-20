package br.com.sicred.votacao.modelo;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pauta {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private String titulo;
	private String descricao;

	@OneToOne(mappedBy = "pauta", cascade = CascadeType.ALL)
	private Sessao sessao;

}
