package com.serratec.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;

public class LoginDTO {

	@JsonAlias({ "username" })
	private String email;

	@JsonAlias({ "senha" })
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
