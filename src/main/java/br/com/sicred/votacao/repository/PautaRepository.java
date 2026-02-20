package br.com.sicred.votacao.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.sicred.votacao.modelo.Pauta;

public interface PautaRepository extends JpaRepository<Pauta, UUID> {
}
