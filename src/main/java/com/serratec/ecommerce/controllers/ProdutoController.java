package com.serratec.ecommerce.controllers;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.serratec.ecommerce.dtos.ProdutoDTO;
import com.serratec.ecommerce.dtos.ProdutoInserirDTO;
import com.serratec.ecommerce.services.ProdutoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@Validated
@Tag(name = "Produtos", description = "CRUD de produtos e consultas auxiliares")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // --------------------- CRUD PRINCIPAL ---------------------

    @GetMapping
    @Operation(summary = "Lista todos os produtos")
    public ResponseEntity<List<ProdutoDTO>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um produto por ID")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @PostMapping
    @Operation(
        summary = "Cadastra um novo produto",
        responses = {
            @ApiResponse(responseCode = "201", description = "Produto criado",
                content = @Content(schema = @Schema(implementation = ProdutoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validação/Regra de negócio violada")
        }
    )
    public ResponseEntity<ProdutoDTO> inserir(
            @Valid @RequestBody ProdutoInserirDTO dto,
            UriComponentsBuilder uriBuilder) {

        ProdutoDTO salvo = produtoService.inserir(dto);
        URI uri = uriBuilder.path("/produtos/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(uri).body(salvo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um produto existente (substitui os campos enviados)")
    public ResponseEntity<ProdutoDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoInserirDTO dto) {
        return ResponseEntity.ok(produtoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um produto por ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // --------------------- AJUSTES E CONSULTAS ---------------------

    @PatchMapping("/{id}/estoque")
    @Operation(summary = "Ajusta o estoque (ex.: +5 ou -3) via query param 'ajuste'")
    public ResponseEntity<ProdutoDTO> ajustarEstoque(
            @PathVariable Long id,
            @RequestParam Integer ajuste) {
        return ResponseEntity.ok(produtoService.ajustarEstoque(id, ajuste));
    }

    @GetMapping("/search")
    @Operation(summary = "Busca produtos pelo nome (contendo, case-insensitive)")
    public ResponseEntity<List<ProdutoDTO>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(produtoService.buscarPorNome(nome));
    }

    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Lista produtos por categoria")
    public ResponseEntity<List<ProdutoDTO>> buscarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(produtoService.buscarPorCategoria(categoriaId));
    }

    @GetMapping("/estoque/abaixo")
    @Operation(summary = "Lista produtos com estoque abaixo do valor informado")
    public ResponseEntity<List<ProdutoDTO>> comEstoqueAbaixoDe(@RequestParam Integer quantidade) {
        return ResponseEntity.ok(produtoService.comEstoqueAbaixoDe(quantidade));
    }

    @GetMapping("/preco/ate")
    @Operation(summary = "Lista produtos com preço menor que o valor informado")
    public ResponseEntity<List<ProdutoDTO>> comPrecoAbaixoDe(@RequestParam Double valor) {
        return ResponseEntity.ok(produtoService.comPrecoAbaixoDe(valor));
    }

    // --------------------- UPLOAD DE IMAGEM ---------------------

 // --------------------- UPLOAD DE IMAGEM ---------------------
    @PostMapping("/{id}/upload")
    @Operation(summary = "Faz upload da imagem de um produto e atualiza o campo 'foto'")
    public ResponseEntity<String> uploadImagem(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/produtos/";
            File directory = new File(uploadDir);
            if (!directory.exists()) directory.mkdirs();

            String fileName = id + ".jpg";
            Path filePath = Paths.get(uploadDir + fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 🔗 Gera a URL pública completa
            String fotoUrl = "http://localhost:8080/uploads/produtos/" + fileName;

            // 🧩 Atualiza no banco
            produtoService.atualizarFoto(id, fotoUrl);

            System.out.println("✅ [ProdutoController] Imagem salva e URL registrada: " + fotoUrl);

            return ResponseEntity.ok("Imagem enviada com sucesso: " + fotoUrl);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erro ao fazer upload: " + e.getMessage());
        }
    }


}
