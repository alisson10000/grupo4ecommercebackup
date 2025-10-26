package com.serratec.ecommerce.dtos;

import com.serratec.ecommerce.enums.Perfil;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioInserirDTO {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 80, message = "O nome deve ter no máximo 80 caracteres.")
    private String nome;

    @Email(message = "O e-mail deve ser válido.")
    @NotBlank(message = "O e-mail é obrigatório.")
    @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres.")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    private String senha;

    private Perfil perfil;

    @Size(max = 10, message = "O número da residência deve ter no máximo 10 caracteres.")
    private String numero;

    @Size(max = 100, message = "O complemento deve ter no máximo 100 caracteres.")
    private String complemento;

    private Long enderecoId;

    public UsuarioInserirDTO() {}

    // 🔹 Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil perfil) { this.perfil = perfil; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public Long getEnderecoId() { return enderecoId; }
    public void setEnderecoId(Long enderecoId) { this.enderecoId = enderecoId; }
}
