package com.serratec.ecommerce.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serratec.ecommerce.dtos.ClienteDTO;
import com.serratec.ecommerce.dtos.NotaFiscalDTO;
import com.serratec.ecommerce.dtos.NotaFiscalItemDTO;
import com.serratec.ecommerce.dtos.NotaFiscalPedidoDTO;
import com.serratec.ecommerce.entitys.Cliente;
import com.serratec.ecommerce.entitys.Endereco;
import com.serratec.ecommerce.exceptions.EntityNotFoundException;
import com.serratec.ecommerce.repositorys.ClienteRepository;
import com.serratec.ecommerce.repositorys.EnderecoRepository;
import com.serratec.ecommerce.repositorys.ProjecaoPedidoClienteRepository;

import jakarta.transaction.Transactional;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ViaCepService viaCepService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private EnderecoRepository enderecoRepository;

	public List<Cliente> listarTodos() {
		return clienteRepository.findAll();
	}

	public Cliente buscarPorId(Long id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);

		if (cliente.isPresent()) {
			return cliente.get();
		} else {
			throw new RuntimeException("Cliente não encontrado");
		}
	}

	@Transactional
	public Cliente criarCliente(ClienteDTO clienteDto) {
		Cliente cliente = new Cliente();

		cliente.setNome(clienteDto.getNome());
		cliente.setEmail(clienteDto.getEmail());
		cliente.setCpf(clienteDto.getCpf());
		cliente.setTelefone(clienteDto.getTelefone());
		cliente.setNumero(clienteDto.getNumero());
		cliente.setComplemento(clienteDto.getComplemento());

		Endereco endereco = viaCepService.buscarEnderecoPorCep(clienteDto.getEndereco().getCep());
		cliente.setEndereco(endereco);

		Cliente salvo = clienteRepository.save(cliente);

		emailService.enviarEmailCliente(salvo.getEmail(), "Cadastro realizado com sucesso!",
				"Olá " + salvo.getNome() + ", seu cadastro foi criado com sucesso!");

		return salvo;
	}

	@Transactional
	public Cliente atualizarCliente(Long id, ClienteDTO clienteDto) {
		Cliente existente = buscarPorId(id);

		existente.setNome(clienteDto.getNome());
		existente.setEmail(clienteDto.getEmail());
		existente.setTelefone(clienteDto.getTelefone());
		existente.setNumero(clienteDto.getNumero());
		existente.setComplemento(clienteDto.getComplemento());

		Cliente atualizado = clienteRepository.save(existente);

		emailService.enviarEmailCliente(atualizado.getEmail(), "Cadastro atualizado!",
				"Olá " + atualizado.getNome() + ", seus dados foram atualizados com sucesso!");

		return atualizado;
	}

	public void excluirCliente(Long id) {
		Cliente cliente = buscarPorId(id);
		clienteRepository.delete(cliente);
	}

	public NotaFiscalDTO gerarNotaFiscal(Long clienteId, Long pedidoId) {
		List<ProjecaoPedidoClienteRepository> projecoes = clienteRepository
				.findDetalhesPedidosByClienteIdAndPedidoId(clienteId, pedidoId);

		if (projecoes.isEmpty()) {
			throw new EntityNotFoundException(
					"Pedido ID " + pedidoId + " não encontrado para o Cliente ID " + clienteId);
		}

		NotaFiscalDTO notaFiscal = new NotaFiscalDTO(projecoes.get(0));

		NotaFiscalPedidoDTO pedidoDTO = new NotaFiscalPedidoDTO(projecoes.get(0));
		for (ProjecaoPedidoClienteRepository proj : projecoes) {
			pedidoDTO.adicionarItem(new NotaFiscalItemDTO(proj));
		}

		notaFiscal.setPedido(pedidoDTO);

		return notaFiscal;
	}
}
