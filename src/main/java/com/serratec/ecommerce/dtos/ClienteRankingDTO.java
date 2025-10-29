package com.serratec.ecommerce.dtos;

public class ClienteRankingDTO {
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private Double valorTotalCompras;

    public ClienteRankingDTO(Long id, String nome, String email, String cpf, Double valorTotalCompras) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.valorTotalCompras = valorTotalCompras;
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

    public Double getValorTotalCompras() {
        return valorTotalCompras;
    }

    public void setValorTotalCompras(Double valorTotalCompras) {
        this.valorTotalCompras = valorTotalCompras;
    }
}
