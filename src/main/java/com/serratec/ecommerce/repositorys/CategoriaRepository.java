package com.serratec.ecommerce.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.serratec.ecommerce.entitys.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	Optional<Categoria> findByNome(String nome);
}
