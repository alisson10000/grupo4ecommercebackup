package com.serratec.ecommerce.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/validar-cupom")
@Tag(name = "Cupom", description = "Gerencia os cupons (ADMIN)")
public class ValidarCupomController {

    // "banco de dados" pra armazenar os cupons em memória
    private final List<String> cuponsValidos = List.of("SERRATEC25", "GRUPO4", "FRETEGRATIS", "ISABELLA");

    @GetMapping
    public ResponseEntity<String> validar(@RequestParam(name = "codigo", required = false) String codigo) {

        // aqui podem ser 3 coisas: código é nulo, ele não ta na lista 
		// ou pode ter sido digitado errado
        if (codigo == null || !cuponsValidos.contains(codigo.toUpperCase())) {
            throw new EntityNotFoundException("Cupom inválido ou não informado.");
        }
		
        // se não, o cupom é válido
        return ResponseEntity.ok("Cupom '" + codigo + "' aplicado com sucesso!");
    }
}