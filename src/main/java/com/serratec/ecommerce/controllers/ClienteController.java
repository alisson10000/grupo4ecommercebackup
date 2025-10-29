package com.serratec.ecommerce.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.serratec.ecommerce.dtos.ClienteDTO;
import com.serratec.ecommerce.dtos.NotaFiscalDTO;
import com.serratec.ecommerce.entitys.Cliente;
import com.serratec.ecommerce.services.ClienteService;
import com.serratec.ecommerce.services.PdfService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Cliente", description = "Gerenciamento de clientes - cadastro, atualização, listagem e exclusão")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private PdfService pdfService;

	@Operation(summary = "Listar todos os clientes", description = "Retorna uma lista com todos os clientes cadastrados no sistema.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content) })
	@GetMapping
	public ResponseEntity<List<Cliente>> listarTodos() {
		return ResponseEntity.ok(clienteService.listarTodos());
	}

	@Operation(summary = "Buscar cliente por ID", description = "Busca e retorna um cliente específico a partir do ID informado.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content) })
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(clienteService.buscarPorId(id));
	}

	@GetMapping("/{clienteId}/pedidos/{pedidoId}/nota-fiscal-pdf")
	@Operation(summary = "Gerar PDF de uma Nota Fiscal", description = "Retorna um PDF da nota fiscal de um pedido específico, validando se o pedido pertence ao cliente.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "PDF gerado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Cliente ou Pedido não encontrado (ou pedido não pertence ao cliente)") })
	public ResponseEntity<byte[]> gerarNotaFiscalPdf(@PathVariable Long clienteId, @PathVariable Long pedidoId) {
		try {
			NotaFiscalDTO notaFiscalDTO = clienteService.gerarNotaFiscal(clienteId, pedidoId);

			byte[] pdfBytes = pdfService.gerarPdfNotaFiscal(notaFiscalDTO);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);

			String nomeArquivo = "nota_fiscal_pedido_" + pedidoId + ".pdf";
			headers.setContentDisposition(ContentDisposition.builder("attachment").filename(nomeArquivo).build());

			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			headers.setContentLength(pdfBytes.length);
			return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

		} catch (IOException e) {
			throw new RuntimeException("Erro ao gerar PDF: " + e.getMessage());
		}
	}

	@Operation(summary = "Cadastrar novo cliente", description = "Cadastra um novo cliente com base nas informações fornecidas no corpo da requisição.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Cliente criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
			@ApiResponse(responseCode = "400", description = "Os Dados enviados na requisição são inválidos", content = @Content) })
	@PostMapping
	public ResponseEntity<Cliente> criarCliente(@Valid @RequestBody ClienteDTO clienteDto) {
		Cliente novoCliente = clienteService.criarCliente(clienteDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
	}

	@Operation(summary = "Atualizar cliente existente", description = "Atualiza as informações de um cliente existente no banco de dados, com base no ID informado.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content),
			@ApiResponse(responseCode = "400", description = "Os Dados enviados na requisição são inválidos", content = @Content) })
	@PutMapping("/{id}")
	public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @Valid @RequestBody ClienteDTO dto) {
		Cliente clienteAtualizado = clienteService.atualizarCliente(id, dto);
		return ResponseEntity.ok(clienteAtualizado);
	}

	@Operation(summary = "Excluir cliente", description = "Exclui permanentemente um cliente com base no ID informado.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso", content = @Content),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content) })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirCliente(@PathVariable Long id) {
		clienteService.excluirCliente(id);
		return ResponseEntity.noContent().build();
	}
}