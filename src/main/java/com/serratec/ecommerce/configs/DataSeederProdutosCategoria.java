package com.serratec.ecommerce.configs;

import com.serratec.ecommerce.entitys.Categoria;
import com.serratec.ecommerce.entitys.Produto;
import com.serratec.ecommerce.repositorys.CategoriaRepository;
import com.serratec.ecommerce.repositorys.ProdutoRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



import java.util.*;

@Configuration
public class DataSeederProdutosCategoria {

    // 🧾 Listas fixas para gerar nomes coerentes
    private static final String[] ADJETIVOS = {
            "Elegante", "Resistente", "Durável", "Econômico", "Compacto",
            "Confortável", "Sofisticado", "Potente", "Leve", "Moderno"
    };

    // Mapa que define produtos coerentes com cada categoria
    private static final Map<String, String[]> PRODUTOS_POR_CATEGORIA = Map.ofEntries(
            Map.entry("Eletrônicos", new String[]{"notebook", "celular", "televisão", "tablet", "monitor", "fone de ouvido", "mouse", "teclado", "caixa de som", "relógio inteligente"}),
            Map.entry("Vestuário", new String[]{"camisa", "calça", "jaqueta", "tênis", "vestido", "blusa", "bermuda", "casaco"}),
            Map.entry("Beleza e Cuidados", new String[]{"perfume", "shampoo", "creme facial", "sabonete", "hidratante", "condicionador"}),
            Map.entry("Casa e Jardim", new String[]{"sofá", "panela", "cadeira", "mesa", "vaso", "tapete"}),
            Map.entry("Esportes e Lazer", new String[]{"bicicleta", "bola", "raquete", "patins", "barraca", "mochila esportiva"}),
            Map.entry("Pet Shop", new String[]{"coleira", "ração", "brinquedo para cachorro", "arranhador", "caminha"}),
            Map.entry("Automotivo", new String[]{"pneu", "óleo de motor", "limpador de para-brisa", "bateria", "tapete automotivo"}),
            Map.entry("Papelaria e Escritório", new String[]{"caneta", "caderno", "impressora", "papel A4", "grampeador"}),
            Map.entry("Brinquedos e Jogos", new String[]{"lego", "boneca", "carrinho", "jogo de tabuleiro", "puzzle"}),
            Map.entry("Alimentos e Bebidas", new String[]{"arroz", "feijão", "suco", "refrigerante", "chocolate"})
    );

    @Bean
    CommandLineRunner seedProdutosECategorias(CategoriaRepository categoriaRepository,
                                              ProdutoRepository produtoRepository) {
        return args -> {

            //  Evita duplicar dados
            if (categoriaRepository.count() > 0 || produtoRepository.count() > 0) {
                System.out.println("⚠️ Banco já possui dados de categorias/produtos. Seeder ignorado.");
                return;
            }

            Faker faker = new Faker(new Locale("pt-BR"));
            Random random = new Random();

            //  Cria categorias e produtos correspondentes
            for (Map.Entry<String, String[]> entry : PRODUTOS_POR_CATEGORIA.entrySet()) {
                String nomeCategoria = entry.getKey();
                String[] produtosPossiveis = entry.getValue();

                String descricaoCategoria = "Categoria voltada para produtos de " + nomeCategoria.toLowerCase();
                Categoria categoria = new Categoria(nomeCategoria, descricaoCategoria);
                categoriaRepository.save(categoria);

                // Cria 10 produtos coerentes para cada categoria
                for (int j = 0; j < 10; j++) {
                    Produto produto = new Produto();

                    String nomeProduto = ADJETIVOS[random.nextInt(ADJETIVOS.length)] + " " +
                            produtosPossiveis[random.nextInt(produtosPossiveis.length)];

                    // Gera descrição natural em português
                    String descricaoProduto = "O " + nomeProduto.toLowerCase() + " é " +
                            faker.lorem().sentence(8).replace(".", "") + ".";

                    double preco = Math.round((50 + (1500 - 50) * random.nextDouble()) * 100.0) / 100.0;


                    int quantidadeEstoque = random.nextInt(10, 100);

                    produto.setNome(nomeProduto);
                    produto.setDescricao(descricaoProduto);
                    produto.setPreco(preco);
                    produto.setQuantidadeEstoque(quantidadeEstoque);
                    produto.setCategoria(categoria);

                    produtoRepository.save(produto);
                }
            }

            System.out.println(" Banco populado com categorias e produtos coerentes em português 🇧🇷!");
        };
    }
}
