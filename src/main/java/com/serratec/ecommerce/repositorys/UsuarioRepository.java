package com.serratec.ecommerce.repositorys;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.serratec.ecommerce.entitys.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca usuário pelo e-mail (usado na autenticação JWT)
    Optional<Usuario> findByEmail(String email);
    
    
}
