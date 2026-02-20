package br.com.sicred.votacao.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.sicred.votacao.enums.TipoVoto;
import br.com.sicred.votacao.modelo.Voto;

public interface VotoRepository extends JpaRepository<Voto, UUID> {

    long countByPautaIdAndVoto(UUID pautaId, TipoVoto voto);
    
}
