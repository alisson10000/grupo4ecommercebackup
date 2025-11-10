package com.serratec.ecommerce.configs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.serratec.ecommerce.entitys.Cliente;
import com.serratec.ecommerce.entitys.Endereco;
import com.serratec.ecommerce.entitys.Pedido;
import com.serratec.ecommerce.entitys.Produto;
import com.serratec.ecommerce.enums.StatusPedido;
import com.serratec.ecommerce.repositorys.ClienteRepository;
import com.serratec.ecommerce.repositorys.EnderecoRepository;
import com.serratec.ecommerce.repositorys.PedidoRepository;
import com.serratec.ecommerce.repositorys.ProdutoRepository;

import net.datafaker.Faker;

@Configuration
public class DataSeederClientesPedidos {

    @Bean
    CommandLineRunner seedClientesPedidos(
            EnderecoRepository enderecoRepository,
            ClienteRepository clienteRepository,
            PedidoRepository pedidoRepository,
            ProdutoRepository produtoRepository) {

        return args -> {

            Faker faker = new Faker(new Locale("pt-BR"));

            // ========================= CLIENTES (com novos endereços) =========================
            if (clienteRepository.count() == 0) {
                List<Cliente> clientes = new ArrayList<>();

                for (int i = 1; i <= 20; i++) {
                    // 👉 Cria um NOVO Endereço (não salvo no banco)
                    Endereco endereco = new Endereco(
                            faker.address().streetName(),
                            faker.address().streetAddressNumber(),
                            faker.address().cityName(),
                            "RJ",
                            faker.address().zipCode().replace("-", "")
                    );

                    // 👉 Cria o Cliente e associa o Endereço
                    Cliente cliente = new Cliente(
                            null,
                            faker.name().fullName(),
                            faker.internet().emailAddress(),
                            faker.cpf().valid().replaceAll("\\D", ""), // CPF válido
                            "24" + faker.phoneNumber().subscriberNumber(8),
                            String.valueOf(faker.number().numberBetween(1, 999)),
                            faker.address().secondaryAddress(),
                            endereco // 👈 Endereço ainda não salvo — Cascade PERSIST vai cuidar
                    );

                    clientes.add(cliente);
                }

                clienteRepository.saveAll(clientes);
                System.out.println("✅ 20 Clientes (com endereços) seedados com sucesso!");
            }

            // ========================= PEDIDOS + ITENS =========================
            if (pedidoRepository.count() == 0) {
                List<Cliente> clientes = clienteRepository.findAll();
                List<Produto> produtos = produtoRepository.findAll();
                List<Pedido> pedidos = new ArrayList<>();

                if (produtos.isEmpty()) {
                    System.err.println("⚠️ Nenhum produto encontrado! Execute o seeder de produtos antes.");
                    return;
                }

                for (int i = 0; i < 20; i++) {
                    Cliente cliente = clientes.get(i % clientes.size());

                    Pedido pedido = new Pedido(cliente);
                    pedido.setDataPedido(LocalDateTime.now().minusDays(i));
                    pedido.setStatus((i % 2 == 0) ? StatusPedido.PENDENTE : StatusPedido.PENDENTE);

                    // Adiciona 3 produtos em cada pedido
                    Produto produto1 = produtos.get(i % produtos.size());
                    Produto produto2 = produtos.get((i + 1) % produtos.size());
                    Produto produto3 = produtos.get((i + 2) % produtos.size());

                    pedido.adicionarItem(produto1, faker.number().numberBetween(1, 3), BigDecimal.ZERO);
                    pedido.adicionarItem(produto2, faker.number().numberBetween(1, 2), BigDecimal.valueOf(5.0));
                    pedido.adicionarItem(produto3, faker.number().numberBetween(1, 2), BigDecimal.valueOf(10.0));

                    pedidos.add(pedido);
                }

                pedidoRepository.saveAll(pedidos);
                System.out.println("✅ 20 Pedidos e Itens seedados com sucesso (via cascade)!");
            }

            System.out.println("🎯 Seeder completo: 20 Clientes, Endereços, Pedidos e Itens criados!");
        };
    }
}
