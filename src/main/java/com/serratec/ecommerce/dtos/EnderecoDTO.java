package com.serratec.ecommerce.dtos;

import com.serratec.ecommerce.entitys.Endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EnderecoDTO {

    private Long id;

    @NotBlank(message = "O logradouro é obrigatório.")
    @Size(max = 100, message = "O logradouro deve ter no máximo 100 caracteres.")
    private String logradouro;

    @NotBlank(message = "O bairro é obrigatório.")
    @Size(max = 50, message = "O bairro deve ter no máximo 50 caracteres.")
    private String bairro;

   // @NotBlank(message = "A cidade é obrigatória.")
    @Size(max = 50, message = "A cidade deve ter no máximo 50 caracteres.")
    private String cidade;

    @NotBlank(message = "O estado (UF) é obrigatório.")
    @Size(min = 2, max = 2, message = "A UF deve conter exatamente 2 caracteres (ex: RJ).")
    private String uf;

    @NotBlank(message = "O CEP é obrigatório.")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "O CEP deve estar no formato 00000-000.")
    private String cep;

    // 🔹 Construtores
    public EnderecoDTO() {}

    public EnderecoDTO(Endereco endereco) {
        if (endereco != null) {
            this.id = endereco.getId();
            this.logradouro = endereco.getLogradouro();
            this.bairro = endereco.getBairro();
            this.cidade = endereco.getCidade();
            this.uf = endereco.getUf();
            this.cep = endereco.getCep();
        }
    }

    //  Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    //  Conversão reversa (DTO → Entidade)
    public Endereco toEntity() {
        Endereco endereco = new Endereco();
        endereco.setId(this.id);
        endereco.setLogradouro(this.logradouro);
        endereco.setBairro(this.bairro);
        endereco.setCidade(this.cidade);
        endereco.setUf(this.uf);
        endereco.setCep(this.cep);
        return endereco;
    }

    //  Representação legível (útil para logs e debugging)
    @Override
    public String toString() {
        return String.format("%s, %s - %s/%s, CEP: %s", logradouro, bairro, cidade, uf, cep);
    }
}
