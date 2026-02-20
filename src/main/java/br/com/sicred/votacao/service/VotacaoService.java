package br.com.sicred.votacao.service;

import org.springframework.stereotype.Service;

import br.com.sicred.votacao.dtos.PautaDTO;
import br.com.sicred.votacao.modelo.Pauta;
import br.com.sicred.votacao.repository.PautaRepository;
import br.com.sicred.votacao.repository.SessaoRepository;
import br.com.sicred.votacao.repository.VotoRepository;

@Service
public class VotacaoService {

    private final PautaRepository pautaRepository;
    private final SessaoRepository sessaoRepository;
    private final VotoRepository votoRepository;

    public VotacaoService(
        PautaRepository pautaRepository,
        SessaoRepository sessaoRepository,
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

        Voto voto = new Voto();
        voto.setCpfAssociado(dto.cpf());
        voto.setVoto(dto.voto());
        voto.setPauta(sessao.getPauta());

        votoRepository.save(voto);
    }

    public ResultadoDTO resultado(UUID pautaId) {
        long sim = votoRepository.countByPautaIdAndVoto(pautaId, TipoVoto.SIM);
        long nao = votoRepository.countByPautaIdAndVoto(pautaId, TipoVoto.NAO);
        return new ResultadoDTO(sim, nao);
    }
}
