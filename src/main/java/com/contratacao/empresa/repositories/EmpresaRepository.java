package com.contratacao.empresa.repositories;

import com.contratacao.empresa.entidade.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Marca esta interface como um componente de repositório do Spring
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    // JpaRepository<TipoDaEntidade, TipoDaChavePrimaria>

    // O Spring Data JPA cria a query automaticamente a partir do nome do método!
    // "Encontre uma Empresa pelo seu atributo 'cnpj'"
    Optional<Empresa> findByCnpj(String cnpj);

    // Métodos como findAll(), findById(), save(), deleteById() já vêm de graça!
}