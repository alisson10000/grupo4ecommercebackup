package com.serratec.ecommerce.entitys;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O logradouro não pode estar em branco.")
    @Size(max = 120, message = "O logradouro deve ter no máximo 120 caracteres.")
    @Column(length = 120, nullable = false)
    private String logradouro;

    @NotBlank(message = "O bairro não pode estar em branco.")
    @Size(max = 80, message = "O bairro deve ter no máximo 80 caracteres.")
    @Column(length = 80, nullable = false)
    private String bairro;

    //@NotBlank(message = "A cidade não pode estar em branco.")
    @Size(max = 80, message = "A cidade deve ter no máximo 80 caracteres.")
    @Column(length = 80, nullable = true)
    private String cidade;

    @NotBlank(message = "A UF (estado) é obrigatória.")
    @Size(min = 2, max = 2, message = "A UF deve conter exatamente 2 caracteres.")
    @Pattern(regexp = "^[A-Z]{2}$", message = "A UF deve conter apenas letras maiúsculas (ex: RJ, SP, MG).")
    @Column(length = 2, nullable = false)
    private String uf;

    @NotBlank(message = "O CEP é obrigatório.")
    @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "O CEP deve estar no formato 00000-000 ou 00000000.")
    @Column(length = 9, nullable = false)
    private String cep;

    @OneToMany(mappedBy = "endereco", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Usuario> usuarios;
    
    
    @OneToMany(mappedBy = "endereco", cascade = CascadeType.ALL)
    private List<Cliente> clientes;
    
    
    

    public Endereco() {}

    public Endereco(String logradouro, String bairro, String cidade, String uf, String cep) {
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
    }

    // Getters e Setters
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

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
