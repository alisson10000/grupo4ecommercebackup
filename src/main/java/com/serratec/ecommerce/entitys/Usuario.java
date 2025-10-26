package com.serratec.ecommerce.entitys;

import com.serratec.ecommerce.enums.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "Usuario", description = "Representa um usuário do sistema com autenticação e vínculo a um endereço.")
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do usuário", example = "1")
    private Long id;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 80, message = "O nome deve ter no máximo 80 caracteres.")
    @Column(nullable = false, length = 80)
    @Schema(description = "Nome completo do usuário", example = "Alisson Lima")
    private String nome;

    @Email(message = "O e-mail deve ser válido.")
    @NotBlank(message = "O e-mail é obrigatório.")
    @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres.")
    @Column(nullable = false, unique = true, length = 100)
    @Schema(description = "Endereço de e-mail usado para login", example = "alisson@admin.com")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    @Column(nullable = false)
    @Schema(description = "Senha do usuário (armazenada criptografada no banco de dados)", example = "123456")
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "Perfil de acesso do usuário", example = "ADMIN", allowableValues = {"ADMIN", "USER"})
    private Perfil perfil;

    @Size(max = 10, message = "O número da casa deve ter no máximo 10 caracteres.")
    @Column(length = 10)
    @Schema(description = "Número da residência", example = "100")
    private String numero;

    @Size(max = 100, message = "O complemento deve ter no máximo 100 caracteres.")
    @Column(length = 100)
    @Schema(description = "Complemento do endereço", example = "Prédio principal, 3º andar")
    private String complemento;

    @ManyToOne
    @JoinColumn(name = "endereco_id", nullable = false)
    @Schema(description = "Endereço associado ao usuário")
    private Endereco endereco;

    //  Construtores
    public Usuario() {}

    public Usuario(String nome, String email, String senha, Perfil perfil, String numero, String complemento, Endereco endereco) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
        this.numero = numero;
        this.complemento = complemento;
        this.endereco = endereco;
    }

    //  Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }
}
