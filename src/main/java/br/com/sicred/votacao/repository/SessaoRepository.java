package br.com.sicred.votacao.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.sicred.votacao.modelo.Sessao;

public interface SessaoRepository extends JpaRepository<Sessao, UUID> {
    Optional<Sessao> findByPautaId(UUID pautaId);
}
