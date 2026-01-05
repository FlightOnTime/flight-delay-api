-- ===================================================================
-- FLYWAY MIGRATION V1
-- Description: Create prediction_history table for storing flight delay predictions
-- Author: FlightOnTime Team
-- Date: 2025-12-30
-- ===================================================================

-- Drop table if exists (only for development - remove in production)
DROP TABLE IF EXISTS prediction_history CASCADE;

-- Create prediction_history table
CREATE TABLE prediction_history (
    -- Primary Key
    id BIGSERIAL PRIMARY KEY,
    
    -- Flight Information
    companhia VARCHAR(50) NOT NULL,
    origem_aeroporto VARCHAR(10) NOT NULL,
    destino_aeroporto VARCHAR(10) NOT NULL,
    data_partida TIMESTAMP NOT NULL,
    distancia_km DOUBLE PRECISION NOT NULL,
    
    -- Prediction Results
    previsao VARCHAR(20) NOT NULL,
    probabilidade_atraso DOUBLE PRECISION NOT NULL,
    confianca VARCHAR(20),
    
    -- Audit Fields
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    
    -- Constraints
    CONSTRAINT chk_probabilidade_atraso 
        CHECK (probabilidade_atraso >= 0 AND probabilidade_atraso <= 1),
    CONSTRAINT chk_previsao 
        CHECK (previsao IN ('No Horário', 'Atrasado', 'Cancelado')),
    CONSTRAINT chk_confianca 
        CHECK (confianca IN ('Alta', 'Média', 'Baixa') OR confianca IS NULL)
);

-- Create indexes for performance
CREATE INDEX idx_prediction_created_at 
    ON prediction_history(created_at DESC);

CREATE INDEX idx_prediction_origem 
    ON prediction_history(origem_aeroporto);

CREATE INDEX idx_prediction_destino 
    ON prediction_history(destino_aeroporto);

CREATE INDEX idx_prediction_companhia 
    ON prediction_history(companhia);

CREATE INDEX idx_prediction_data_partida 
    ON prediction_history(data_partida);

-- Add table comment
COMMENT ON TABLE prediction_history IS 
    'Histórico de predições de atraso de voos realizadas pela API';

-- Add column comments
COMMENT ON COLUMN prediction_history.id IS 
    'Identificador único da predição';
COMMENT ON COLUMN prediction_history.companhia IS 
    'Nome da companhia aérea (ex: LATAM, GOL, AZUL)';
COMMENT ON COLUMN prediction_history.origem_aeroporto IS 
    'Código IATA do aeroporto de origem (ex: GRU, SDU)';
COMMENT ON COLUMN prediction_history.destino_aeroporto IS 
    'Código IATA do aeroporto de destino (ex: GIG, BSB)';
COMMENT ON COLUMN prediction_history.data_partida IS 
    'Data e hora prevista de partida do voo';
COMMENT ON COLUMN prediction_history.distancia_km IS 
    'Distância em quilômetros entre origem e destino';
COMMENT ON COLUMN prediction_history.previsao IS 
    'Resultado da predição: No Horário, Atrasado ou Cancelado';
COMMENT ON COLUMN prediction_history.probabilidade_atraso IS 
    'Probabilidade de atraso (valor entre 0 e 1)';
COMMENT ON COLUMN prediction_history.confianca IS 
    'Nível de confiança da predição: Alta, Média ou Baixa';
COMMENT ON COLUMN prediction_history.created_at IS 
    'Data e hora de criação do registro';