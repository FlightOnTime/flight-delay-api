CREATE TABLE prediction_history (
    id BIGSERIAL PRIMARY KEY,

    companhia_aerea VARCHAR(50) NOT NULL,
    origem_aeroporto VARCHAR(10) NOT NULL,
    destino_aeroporto VARCHAR(10) NOT NULL,

    data_partida TIMESTAMP NOT NULL,
    dia_da_semana VARCHAR(20),

    atraso_previsto BOOLEAN NOT NULL,
    probabilidade_atraso DOUBLE PRECISION NOT NULL,

    modelo_versao VARCHAR(50),
    tempo_resposta_ms DOUBLE PRECISION,

    status BOOLEAN NOT NULL,
    request_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);