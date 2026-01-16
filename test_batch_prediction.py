import requests
import random
from datetime import datetime, timedelta

# Configurações do Endpoint
URL = "http://localhost:8080/api/v1/predict/batch"

# Parâmetros para geração aleatória
COMPANHIAS = ["DELTA", "UNITED", "AMERICAN", "SOUTHWEST"]
AEROPORTOS = ["ATL", "LAX", "ORD", "JFK", "DFW", "MIA"]
START_DATE = datetime(2026, 1, 15)
END_DATE = datetime(2026, 12, 31)

def generate_random_date(start, end):
    """Gera uma data aleatória entre start e end no formato ISO."""
    delta = end - start
    int_delta = (delta.days * 24 * 60 * 60) + delta.seconds
    random_second = random.randrange(int_delta)
    random_date = start + timedelta(seconds=random_second)
    return random_date.strftime("%Y-%m-%dT%H:%M:%S")

def generate_random_flight():
    """Gera um objeto de voo com dados aleatórios conforme requisitos."""
    origem = random.choice(AEROPORTOS)
    destino = random.choice([a for a in AEROPORTOS if a != origem])
    
    return {
        "companhia": random.choice(COMPANHIAS),
        "origem_aeroporto": origem,
        "destino_aeroporto": destino,
        "data_partida": generate_random_date(START_DATE, END_DATE),
        "distancia_km": round(random.uniform(500.0, 5000.0), 1)
    }

def run_test():
    # 1. Gerar 50 voos
    voos = [generate_random_flight() for _ in range(50)]
    
    # 2. Medir tempo e Enviar 1 POST com a lista
    try:
        start_time = datetime.now()
        response = requests.post(URL, json=voos)
        end_time = datetime.now()
        
        duration = (end_time - start_time).total_seconds()
        
        # 3. Validar Resposta
        if response.status_code in [200, 201]:
            print(f"✅ Sucesso! Processados 50 voos em {duration:.2f} segundos")
        else:
            print(f"❌ Erro HTTP {response.status_code}")
            print(f"Detalhes: {response.text}")
            
    except requests.exceptions.RequestException as e:
        print(f"❌ Erro de Conexão: {e}")

if __name__ == "__main__":
    run_test()
