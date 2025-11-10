package com.serratec.ecommerce.entitys;

import java.util.List;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.serratec.ecommerce.dtos.EnderecoDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "cliente")
@Schema(description = "Entidade que representa um cliente cadastrado no sistema.")
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "Identificador único do cliente.", example = "1")
	private Long id;

	@Schema(description = "Nome completo do cliente.", example = "Natália Sophia Regina Melo")
	@NotBlank(message = "O nome é obrigatório.")
	@Size(max = 100, message = "O campo Nome deve ter no máximo 100 caracteres")
	@Column(nullable = false, length = 100)
	private String nome;

	@Email(message = "O E-mail é inválido.")
	@Schema(description = "E-mail do cliente.", example = "natalia-melo98@marcossousa.com")
	@NotBlank(message = "O campo e-mail é obrigatório.")
	@Column(unique = true, length = 100, nullable = false)
	private String email;

	@CPF(message = "O CPF é inválido.")
	@Schema(description = "CPF do cliente (11 dígitos).", example = "33409826785")
	@Column(unique = true, nullable = false, length = 11)
	@Size(min = 11, max = 11, message = "O campo CPF deve conter 11 dígitos.")
	private String cpf;

	@Schema(description = "Número de telefone do cliente, incluindo DDD.", example = "21984041731")
	@NotBlank(message = "O campo Telefone é obrigatório.")
	@Size(max = 11, message = "O campo telefone não pode ter mais de 11 dígitos")
	@Column(nullable = false, length = 11)
	private String telefone;

	@Schema(description = "Número do endereço do cliente (número da casa, apartamento, etc.).", example = "45")
	@NotBlank(message = "O número da casa é obrigatório.")
	@Size(max = 10, message = "O campo Numero deve ter no máximo 10 caracteres")
	@Column(nullable = false, length = 10)
	private String numero;

	@Schema(description = "Complemento do endereço, caso exista (ex: bloco, andar, referência).", example = "Casa dos fundos")
	@Size(max = 60, message = "O campo Complemento deve ter no máximo 60 caracteres")
	@Column(nullable = true, length = 60)
	private String complemento;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "endereco_id", nullable = false)
	@Schema(description = "Endereço associado ao cliente, contendo o CEP e demais informações.", implementation = EnderecoDTO.class)
	private Endereco endereco;

	@OneToMany(mappedBy = "cliente")
	@Schema(description = "Lista de pedidos vinculados ao cliente.")
	@JsonIgnore
	private List<Pedido> pedidos;

	public Cliente(Long id, String nome, String email, String cpf, String telefone, String numero, String complemento,
			Endereco endereco) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.cpf = cpf;
		this.telefone = telefone;
		this.numero = numero;
		this.complemento = complemento;
		this.endereco = endereco;
	}

	public Cliente() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}
}
