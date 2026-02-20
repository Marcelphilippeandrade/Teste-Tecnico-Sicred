package br.com.sicred.votacao.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.sicred.votacao.dtos.PautaDTO;
import br.com.sicred.votacao.dtos.ResultadoDTO;
import br.com.sicred.votacao.dtos.VotoDTO;
import br.com.sicred.votacao.enums.TipoVoto;
import br.com.sicred.votacao.exception.PautaNotFoundException;
import br.com.sicred.votacao.exception.SessaoEmAndamentoException;
import br.com.sicred.votacao.exception.SessaoEncerradaException;
import br.com.sicred.votacao.exception.SessaoJaAbertaException;
import br.com.sicred.votacao.exception.SessaoNaoEncontradaException;
import br.com.sicred.votacao.exception.VotoDuplicadoException;
import br.com.sicred.votacao.modelo.Pauta;
import br.com.sicred.votacao.modelo.Sessao;
import br.com.sicred.votacao.modelo.Voto;
import br.com.sicred.votacao.repository.PautaRepository;
import br.com.sicred.votacao.repository.SessaoRepository;
import br.com.sicred.votacao.repository.VotoRepository;

@Service
public class VotacaoService {

	private final PautaRepository pautaRepository;
	private final SessaoRepository sessaoRepository;
	private final VotoRepository votoRepository;

	public VotacaoService(PautaRepository pautaRepository, SessaoRepository sessaoRepository,
			VotoRepository votoRepository) {

		this.pautaRepository = pautaRepository;
		this.sessaoRepository = sessaoRepository;
		this.votoRepository = votoRepository;
	}

	public Pauta criarPauta(PautaDTO dto) {
		Pauta pauta = new Pauta();
		pauta.setTitulo(dto.titulo());
		pauta.setDescricao(dto.descricao());
		return pautaRepository.save(pauta);
	}

	public Sessao abrirSessao(UUID pautaId, Long minutos) {
	    if (sessaoRepository.findByPautaId(pautaId).isPresent()) {
	        throw new SessaoJaAbertaException();
	    }

	    Pauta pauta = pautaRepository.findById(pautaId)
	        .orElseThrow(PautaNotFoundException::new);

	    LocalDateTime inicio = LocalDateTime.now();
	    LocalDateTime fim = inicio.plusMinutes(minutos != null ? minutos : 1);

	    Sessao sessao = new Sessao();
	    sessao.setInicio(inicio);
	    sessao.setFim(fim);
	    sessao.setPauta(pauta);

	    return sessaoRepository.save(sessao);
	}

	public void votar(UUID pautaId, VotoDTO dto) {

	    Sessao sessao = sessaoRepository.findByPautaId(pautaId)
	        .orElseThrow(PautaNotFoundException::new);

	    if (LocalDateTime.now().isAfter(sessao.getFim())) {
	        throw new SessaoEncerradaException();
	    }

	    if (votoRepository.existsByPautaIdAndCpfAssociado(
	            pautaId, dto.cpf())) {
	        throw new VotoDuplicadoException();
	    }

	    Voto voto = new Voto();
	    voto.setCpfAssociado(dto.cpf());
	    voto.setVoto(dto.voto());
	    voto.setPauta(sessao.getPauta());

	    votoRepository.save(voto);
	}

	public ResultadoDTO resultado(UUID pautaId) {

	    Pauta pauta = pautaRepository.findById(pautaId)
	        .orElseThrow(PautaNotFoundException::new);

	    Sessao sessao = sessaoRepository.findByPautaId(pauta.getId())
	        .orElseThrow(SessaoNaoEncontradaException::new);

	    if (LocalDateTime.now().isBefore(sessao.getFim())) {
	        throw new SessaoEmAndamentoException();
	    }

	    long sim = votoRepository.countByPautaIdAndVoto(pautaId, TipoVoto.SIM);
	    long nao = votoRepository.countByPautaIdAndVoto(pautaId, TipoVoto.NAO);

	    return new ResultadoDTO(sim, nao);
	}

}
