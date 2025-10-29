package com.serratec.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;

public class LoginDTO {

    @JsonAlias({ "username" })
    @Schema(description = "Email do usuário", example = "admin@admin.com")
    private String email;

    @JsonAlias({ "senha" })
    @Schema(description = "Senha do usuário", example = "123456")
    private String password;

    public LoginDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
