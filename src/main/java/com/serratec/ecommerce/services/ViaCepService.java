package com.serratec.ecommerce.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.serratec.ecommerce.entitys.Endereco;

@Service
public class ViaCepService {

	 public Endereco buscarEnderecoPorCep(String cep) {
	        RestTemplate restTemplate = new RestTemplate();
	        String url = "https://viacep.com.br/ws/" + cep + "/json/";

	        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

	        if (response == null || response.containsKey("erro")) {
	            throw new RuntimeException("CEP inválido ou não encontrado: " + cep);
	        }

	        Endereco endereco = new Endereco();
	        endereco.setCep((String) response.get("cep"));
	        endereco.setLogradouro((String) response.get("logradouro"));
	        endereco.setBairro((String) response.get("bairro"));
	        endereco.setCidade((String) response.get("localidade")); // ✅ aqui é o ponto-chave!
	        endereco.setUf((String) response.get("uf"));

	        return endereco;
	    }
}
