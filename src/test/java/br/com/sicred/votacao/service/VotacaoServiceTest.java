package br.com.sicred.votacao.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.sicred.votacao.dtos.PautaDTO;
import br.com.sicred.votacao.dtos.ResultadoDTO;
import br.com.sicred.votacao.dtos.VotoDTO;
import br.com.sicred.votacao.enums.TipoVoto;
import br.com.sicred.votacao.exception.business.*;
import br.com.sicred.votacao.integration.client.CpfValidationClient;
import br.com.sicred.votacao.modelo.Pauta;
import br.com.sicred.votacao.modelo.Sessao;
import br.com.sicred.votacao.modelo.Voto;
import br.com.sicred.votacao.repository.PautaRepository;
import br.com.sicred.votacao.repository.SessaoRepository;
import br.com.sicred.votacao.repository.VotoRepository;

@ExtendWith(MockitoExtension.class)
class VotacaoServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private VotoRepository votoRepository;

    @InjectMocks
    private VotacaoService votacaoService;
    
    @Mock
    private CpfValidationClient cpfValidationClient;

    private UUID pautaId;
    private Pauta pauta;

    @BeforeEach
    void setup() {
        pautaId = UUID.randomUUID();
        pauta = new Pauta();
        pauta.setId(pautaId);
        pauta.setTitulo("Pauta teste");
    }

    // ---------- criarPauta ----------

    @Test
    void deveCriarPautaComSucesso() {
        PautaDTO dto = new PautaDTO("Titulo", "Descricao");

        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);

        Pauta result = votacaoService.criarPauta(dto);

        assertNotNull(result);
        verify(pautaRepository).save(any(Pauta.class));
    }

    // ---------- abrirSessao ----------

    @Test
    void deveAbrirSessaoComTempoDefault() {
        when(sessaoRepository.findByPautaId(pautaId)).thenReturn(Optional.empty());
        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));
        when(sessaoRepository.save(any(Sessao.class))).thenAnswer(i -> i.getArgument(0));

        Sessao sessao = votacaoService.abrirSessao(pautaId, null);

        assertNotNull(sessao);
        assertEquals(pauta, sessao.getPauta());
        verify(sessaoRepository).save(any(Sessao.class));
    }

    @Test
    void naoDeveAbrirSessaoQuandoJaExiste() {
        when(sessaoRepository.findByPautaId(pautaId))
            .thenReturn(Optional.of(new Sessao()));

        assertThrows(
            SessaoJaAbertaException.class,
            () -> votacaoService.abrirSessao(pautaId, 5L)
        );
    }

    @Test
    void naoDeveAbrirSessaoQuandoPautaNaoExiste() {
        when(sessaoRepository.findByPautaId(pautaId)).thenReturn(Optional.empty());
        when(pautaRepository.findById(pautaId)).thenReturn(Optional.empty());

        assertThrows(
            PautaNotFoundException.class,
            () -> votacaoService.abrirSessao(pautaId, 5L)
        );
    }

    // ---------- votar ----------

    @Test
    void deveRegistrarVotoComSucesso() {
        Sessao sessao = new Sessao();
        sessao.setFim(LocalDateTime.now().plusMinutes(5));
        sessao.setPauta(pauta);

        VotoDTO dto = new VotoDTO("12345678901", TipoVoto.SIM);

        when(sessaoRepository.findByPautaId(pautaId)).thenReturn(Optional.of(sessao));
        when(votoRepository.existsByPautaIdAndCpfAssociado(pautaId, dto.cpf()))
            .thenReturn(false);

        votacaoService.votar(pautaId, dto);

        verify(votoRepository).save(any(Voto.class));
    }

    @Test
    void naoDevePermitirVotoComSessaoEncerrada() {
        Sessao sessao = new Sessao();
        sessao.setFim(LocalDateTime.now().minusMinutes(1));

        when(sessaoRepository.findByPautaId(pautaId))
            .thenReturn(Optional.of(sessao));

        VotoDTO dto = new VotoDTO("123", TipoVoto.SIM);

        assertThrows(
            SessaoEncerradaException.class,
            () -> votacaoService.votar(pautaId, dto)
        );
    }

    @Test
    void naoDevePermitirVotoDuplicado() {
        Sessao sessao = new Sessao();
        sessao.setFim(LocalDateTime.now().plusMinutes(5));
        sessao.setPauta(pauta);

        VotoDTO dto = new VotoDTO("123", TipoVoto.SIM);

        when(sessaoRepository.findByPautaId(pautaId))
            .thenReturn(Optional.of(sessao));
        when(votoRepository.existsByPautaIdAndCpfAssociado(pautaId, dto.cpf()))
            .thenReturn(true);

        assertThrows(
            VotoDuplicadoException.class,
            () -> votacaoService.votar(pautaId, dto)
        );
    }

    // ---------- resultado ----------

    @Test
    void deveRetornarResultadoComSucesso() {
        Sessao sessao = new Sessao();
        sessao.setFim(LocalDateTime.now().minusMinutes(1));

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));
        when(sessaoRepository.findByPautaId(pautaId)).thenReturn(Optional.of(sessao));
        when(votoRepository.countByPautaIdAndVoto(pautaId, TipoVoto.SIM)).thenReturn(3L);
        when(votoRepository.countByPautaIdAndVoto(pautaId, TipoVoto.NAO)).thenReturn(1L);

        ResultadoDTO resultado = votacaoService.resultado(pautaId);

        assertEquals(3, resultado.votosSim());
        assertEquals(1, resultado.votosNao());
    }

    @Test
    void naoDeveRetornarResultadoQuandoSessaoNaoExiste() {
        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));
        when(sessaoRepository.findByPautaId(pautaId)).thenReturn(Optional.empty());

        assertThrows(
            SessaoNaoEncontradaException.class,
            () -> votacaoService.resultado(pautaId)
        );
    }

    @Test
    void naoDeveRetornarResultadoQuandoSessaoEmAndamento() {
        Sessao sessao = new Sessao();
        sessao.setFim(LocalDateTime.now().plusMinutes(10));

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));
        when(sessaoRepository.findByPautaId(pautaId)).thenReturn(Optional.of(sessao));

        assertThrows(
            SessaoEmAndamentoException.class,
            () -> votacaoService.resultado(pautaId)
        );
    }
    
    @Test
    void naoDevePermitirVotoQuandoCpfInvalido() {
        Sessao sessao = new Sessao();
        sessao.setFim(LocalDateTime.now().plusMinutes(5));
        sessao.setPauta(pauta);

        VotoDTO dto = new VotoDTO("12345678901", TipoVoto.SIM);

        when(sessaoRepository.findByPautaId(pautaId))
            .thenReturn(Optional.of(sessao));

        when(votoRepository.existsByPautaIdAndCpfAssociado(pautaId, dto.cpf()))
            .thenReturn(false);

        doThrow(CpfInvalidoException.class)
            .when(cpfValidationClient)
            .validarCpfParaVoto(dto.cpf());

        assertThrows(
            CpfInvalidoException.class,
            () -> votacaoService.votar(pautaId, dto)
        );

        verify(votoRepository, never()).save(any());
    }
    
    @Test
    void naoDevePermitirVotoQuandoCpfNaoPodeVotar() {
        Sessao sessao = new Sessao();
        sessao.setFim(LocalDateTime.now().plusMinutes(5));
        sessao.setPauta(pauta);

        VotoDTO dto = new VotoDTO("12345678901", TipoVoto.NAO);

        when(sessaoRepository.findByPautaId(pautaId))
            .thenReturn(Optional.of(sessao));

        when(votoRepository.existsByPautaIdAndCpfAssociado(pautaId, dto.cpf()))
            .thenReturn(false);

        doThrow(CpfNaoPodeVotarException.class)
            .when(cpfValidationClient)
            .validarCpfParaVoto(dto.cpf());

        assertThrows(
            CpfNaoPodeVotarException.class,
            () -> votacaoService.votar(pautaId, dto)
        );

        verify(votoRepository, never()).save(any());
    }
}
