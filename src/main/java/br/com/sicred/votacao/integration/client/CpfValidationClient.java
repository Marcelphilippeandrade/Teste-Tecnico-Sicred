package br.com.sicred.votacao.integration.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.sicred.votacao.enums.CpfVoteStatus;
import br.com.sicred.votacao.exception.business.CpfInvalidoException;
import br.com.sicred.votacao.exception.business.CpfNaoPodeVotarException;
import br.com.sicred.votacao.integration.config.CpfValidationProperties;
import br.com.sicred.votacao.integration.dtos.CpfValidationResponse;

@Component
public class CpfValidationClient {

	private final RestTemplate restTemplate;
	private final CpfValidationProperties properties;

	public CpfValidationClient(RestTemplate restTemplate, CpfValidationProperties properties) {
		this.restTemplate = restTemplate;
		this.properties = properties;
	}

	public void validarCpfParaVoto(String cpf) {

		String url = properties.getBaseUrl() + "/users/{cpf}";

		try {
			CpfValidationResponse response = restTemplate.getForObject(url, CpfValidationResponse.class, cpf);

			if (response == null || CpfVoteStatus.UNABLE_TO_VOTE.name().equals(response.status())) {
				throw new CpfNaoPodeVotarException();
			}

		} catch (HttpClientErrorException.NotFound ex) {
			throw new CpfInvalidoException();
		}
	}
}
