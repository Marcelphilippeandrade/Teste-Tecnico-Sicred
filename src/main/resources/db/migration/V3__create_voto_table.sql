CREATE TABLE voto (
    id UUID PRIMARY KEY,
    cpf_associado VARCHAR(14) NOT NULL,
    voto VARCHAR(3) NOT NULL,
    pauta_id UUID NOT NULL,

    CONSTRAINT fk_voto_pauta
        FOREIGN KEY (pauta_id)
        REFERENCES pauta(id),

    CONSTRAINT uk_voto_unico
        UNIQUE (pauta_id, cpf_associado)
);
