package com.serratec.ecommerce.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.serratec.ecommerce.entitys.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long>{
	Optional<Endereco> findByCep(String cep);
}
