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

            // ========================= ENDEREÇOS =========================
            if (enderecoRepository.count() == 0) {
                List<Endereco> enderecos = new ArrayList<>();

                for (int i = 1; i <= 20; i++) {
                    Endereco e = new Endereco(
                            faker.address().streetName(),
                            faker.address().streetAddressNumber(),
                            faker.address().cityName(),
                            "RJ",
                            faker.address().zipCode().replace("-", "")
                    );
                    enderecos.add(e);
                }

                enderecoRepository.saveAll(enderecos);
                System.out.println("✅ Endereços seedados com sucesso!");
            }

            // ========================= CLIENTES =========================
            if (clienteRepository.count() == 0) {
                List<Endereco> enderecos = enderecoRepository.findAll();
                List<Cliente> clientes = new ArrayList<>();

                for (int i = 1; i <= 20; i++) {
                    Endereco e = enderecos.get((i - 1) % enderecos.size());

                    String nome = faker.name().fullName();
                    String email = faker.internet().emailAddress();
                    String cpf = faker.cpf().valid().replaceAll("\\D", ""); // CPF válido
                    String telefone = "24" + faker.phoneNumber().subscriberNumber(9);

                    Cliente c = new Cliente(
                            null,
                            nome,
                            email,
                            cpf,
                            telefone,
                            String.valueOf(faker.number().numberBetween(1, 999)),
                            faker.address().secondaryAddress(),
                            e
                    );

                    clientes.add(c);
                }

                clienteRepository.saveAll(clientes);
                System.out.println("✅ Clientes seedados com sucesso!");
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

                // Gera 20 pedidos, 1 para cada cliente (ou mais, se desejar)
                for (int i = 0; i < 20; i++) {
                    Cliente cliente = clientes.get(i % clientes.size());

                    Pedido pedido = new Pedido(cliente);
                    pedido.setDataPedido(LocalDateTime.now().minusDays(i));
                    pedido.setStatus((i % 2 == 0) ? StatusPedido.PENDENTE : StatusPedido.PENDENTE);

                    // Adiciona 3 itens por pedido
                    Produto produto1 = produtos.get(i % produtos.size());
                    Produto produto2 = produtos.get((i + 1) % produtos.size());
                    Produto produto3 = produtos.get((i + 2) % produtos.size());

                    pedido.adicionarItem(produto1, faker.number().numberBetween(1, 3), BigDecimal.ZERO);
                    pedido.adicionarItem(produto2, faker.number().numberBetween(1, 2), BigDecimal.valueOf(5.0));
                    pedido.adicionarItem(produto3, faker.number().numberBetween(1, 2), BigDecimal.valueOf(10.0));

                    pedidos.add(pedido);
                }

                pedidoRepository.saveAll(pedidos);
                System.out.println("✅ Pedidos e Itens seedados com sucesso (via cascade)!");
            }

            System.out.println("🎯 Seeder completo: 20 Endereços, 20 Clientes, 20 Pedidos e Itens criados!");
        };
    }
}
