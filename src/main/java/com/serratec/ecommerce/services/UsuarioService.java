package com.serratec.ecommerce.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.serratec.ecommerce.dtos.EnderecoDTO;
import com.serratec.ecommerce.dtos.UsuarioDTO;
import com.serratec.ecommerce.dtos.UsuarioInserirDTO;
import com.serratec.ecommerce.entitys.Endereco;
import com.serratec.ecommerce.entitys.Usuario;
import com.serratec.ecommerce.enums.Perfil;
import com.serratec.ecommerce.repositorys.EnderecoRepository;
import com.serratec.ecommerce.repositorys.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          EnderecoRepository enderecoRepository,
                          BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.enderecoRepository = enderecoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 🔹 Criar usuário
    public UsuarioDTO criar(UsuarioInserirDTO dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        Endereco endereco = enderecoRepository.findById(dto.getEnderecoId())
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado."));

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setPerfil(dto.getPerfil() != null ? dto.getPerfil() : Perfil.USER);
        usuario.setNumero(dto.getNumero());
        usuario.setComplemento(dto.getComplemento());
        usuario.setEndereco(endereco);

        usuarioRepository.save(usuario);
        return toDTO(usuario);
    }

    // 🔹 Listar todos os usuários
    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Buscar por ID
    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
        return toDTO(usuario);
    }

    // 🔹 Atualizar usuário
    public UsuarioDTO atualizar(Long id, UsuarioInserirDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        if (dto.getNome() != null && !dto.getNome().isBlank()) {
            usuario.setNome(dto.getNome());
        }

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        if (dto.getPerfil() != null) {
            usuario.setPerfil(dto.getPerfil());
        }

        usuario.setNumero(dto.getNumero());
        usuario.setComplemento(dto.getComplemento());

        if (dto.getEnderecoId() != null) {
            Endereco endereco = enderecoRepository.findById(dto.getEnderecoId())
                    .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado."));
            usuario.setEndereco(endereco);
        }

        usuarioRepository.save(usuario);
        return toDTO(usuario);
    }

    // 🔹 Deletar usuário
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado para exclusão.");
        }
        usuarioRepository.deleteById(id);
    }

    // 🔹 Conversão Entity → DTO
    private UsuarioDTO toDTO(Usuario usuario) {
        Endereco endereco = usuario.getEndereco();
        EnderecoDTO enderecoDTO = endereco != null ? new EnderecoDTO(endereco) : null;

        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.getNumero(),
                usuario.getComplemento(),
                enderecoDTO
        );
    }
}
