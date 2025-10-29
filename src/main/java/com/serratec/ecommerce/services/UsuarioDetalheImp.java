package com.serratec.ecommerce.services;

import com.serratec.ecommerce.entitys.Usuario;
import com.serratec.ecommerce.repositorys.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioDetalheImp implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetalheImp(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name());
        List<GrantedAuthority> authorities = List.of(authority);

        System.out.println("Perfis carregados para " + usuario.getEmail() + ": " + authorities);

        return new User(usuario.getEmail(), usuario.getSenha(), authorities);
    }
}