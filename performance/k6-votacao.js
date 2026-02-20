import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 50,
  duration: '30s',
};

export default function () {
  const pautaId = 'UUID_DA_PAUTA_AQUI';

  const payload = JSON.stringify({
    cpf: `${Math.floor(Math.random() * 10000000000)}`,
    voto: 'SIM'
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  let res = http.post(
    `http://localhost:8080/api/v1/pautas/${pautaId}/votos`,
    payload,
    params
  );

  check(res, {
    'status é 200 ou 422 ou 409': (r) =>
      r.status === 200 ||
      r.status === 409 ||  // voto duplicado
      r.status === 422 ||  // CPF não pode votar / sessão encerrada
      r.status === 404,    // CPF inválido
  });

  sleep(1);
}