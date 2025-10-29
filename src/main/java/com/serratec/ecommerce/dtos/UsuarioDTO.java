package com.serratec.ecommerce.dtos;

import com.serratec.ecommerce.entitys.Usuario;
import com.serratec.ecommerce.enums.Perfil;

public class UsuarioDTO {

    private Long id;
    private String nome;
    private String email;
    private Perfil perfil;
    private String numero;
    private String complemento;
    private EnderecoDTO endereco;

    public UsuarioDTO() {}

    // 🔹 Construtor que recebe a entidade diretamente
    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.perfil = usuario.getPerfil();
        this.numero = usuario.getNumero();
        this.complemento = usuario.getComplemento();

        if (usuario.getEndereco() != null) {
            this.endereco = new EnderecoDTO(usuario.getEndereco());
        }
    }

    // 🔹 Construtor completo
    public UsuarioDTO(Long id, String nome, String email, Perfil perfil,
                      String numero, String complemento, EnderecoDTO endereco) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
        this.numero = numero;
        this.complemento = complemento;
        this.endereco = endereco;
    }

    // 🔹 Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil perfil) { this.perfil = perfil; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public EnderecoDTO getEndereco() { return endereco; }
    public void setEndereco(EnderecoDTO endereco) { this.endereco = endereco; }
}
