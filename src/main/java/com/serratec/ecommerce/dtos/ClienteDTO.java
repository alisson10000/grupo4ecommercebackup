package com.serratec.ecommerce.dtos;

import org.hibernate.validator.constraints.br.CPF;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Representa os dados necessários para cadastrar ou atualizar um cliente.")
public class ClienteDTO {

	@Schema(description = "Nome completo do cliente.", example = "Natália Sophia Regina Melo")
	@NotBlank(message = "O nome é obrigatório.")
	private String nome;

	@Schema(description = "Endereço de e-mail do cliente, usado para comunicação e autenticação.", example = "natalia-melo98@marcossousa.com")
	@Email(message = "E-mail inválido.")
	@NotBlank(message = "O e-mail é obrigatório.")
	private String email;

	@Schema(description = "CPF do cliente, utilizado para identificação única.", example = "33409826785")
	@CPF(message = "CPF inválido.")
	@NotBlank(message = "O CPF é obrigatório.")
	private String cpf;

	@Schema(description = "Número de telefone do cliente, incluindo DDD.", example = "21984041731")
	@NotBlank(message = "O telefone é obrigatório.")
	private String telefone;

	@Schema(description = "Número do endereço do cliente (número da casa, apartamento, etc.).", example = "45")
	@NotBlank(message = "O número é obrigatório.")
	private String numero;

	@Schema(description = "Complemento do endereço, caso exista (ex: bloco, andar, referência).", example = "Casa dos fundos")
	private String complemento;

	@Schema(description = "Endereço associado ao cliente, contendo o CEP e demais informações.", implementation = EnderecoDTO.class)
	private EnderecoDTO endereco;

	public ClienteDTO(String nome, String email, String cpf, String telefone, String numero, String complemento,
			EnderecoDTO endereco) {
		super();
		this.nome = nome;
		this.email = email;
		this.cpf = cpf;
		this.telefone = telefone;
		this.numero = numero;
		this.complemento = complemento;
		this.endereco = endereco;
	}

	public ClienteDTO() {
		super();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public EnderecoDTO getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoDTO endereco) {
		this.endereco = endereco;
	}

}
