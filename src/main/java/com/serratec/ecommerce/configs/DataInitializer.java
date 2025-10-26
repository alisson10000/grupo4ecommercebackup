package com.serratec.ecommerce.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.serratec.ecommerce.entitys.Endereco;
import com.serratec.ecommerce.entitys.Usuario;
import com.serratec.ecommerce.enums.Perfil;
import com.serratec.ecommerce.repositorys.UsuarioRepository;
import com.serratec.ecommerce.repositorys.EnderecoRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository,
                                   EnderecoRepository enderecoRepository,
                                   BCryptPasswordEncoder encoder) {
        return args -> {

            // 🔹 1. Verifica se já existe um ADMIN
            if (usuarioRepository.findByEmail("admin@admin.com").isEmpty()) {

                // 🔹 2. Cria um endereço padrão
                Endereco endereco = new Endereco();
                endereco.setCep("25600-000");
                endereco.setCidade("Petrópolis");
                endereco.setUf("RJ");
                endereco.setBairro("Centro");
                endereco.setLogradouro("Rua do Imperador");
                enderecoRepository.save(endereco);

                // 🔹 3. Cria o usuário ADMIN padrão
                Usuario admin = new Usuario();
                admin.setNome("Administrador do Sistema"); // agora obrigatório
                admin.setEmail("admin@admin.com");
                admin.setSenha(encoder.encode("123456")); // senha criptografada
                admin.setPerfil(Perfil.ADMIN);
                admin.setNumero("100");
                admin.setComplemento("Prédio principal");
                admin.setEndereco(endereco);

                usuarioRepository.save(admin);

                System.out.println("✅ Usuário ADMIN criado: admin@admin.com / senha: 123456");
            } else {
                System.out.println("ℹ️ Usuário ADMIN já existe no banco.");
            }
        };
    }
}
