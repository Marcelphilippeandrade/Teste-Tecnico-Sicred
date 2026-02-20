CREATE TABLE sessao (
    id UUID PRIMARY KEY,
    inicio TIMESTAMP NOT NULL,
    fim TIMESTAMP NOT NULL,
    pauta_id UUID NOT NULL UNIQUE,

    CONSTRAINT fk_sessao_pauta
        FOREIGN KEY (pauta_id)
        REFERENCES pauta(id)
);
