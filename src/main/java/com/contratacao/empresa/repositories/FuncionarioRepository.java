package com.contratacao.empresa.repositories;

import com.contratacao.empresa.entidade.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    // Também podemos criar queries para buscar por atributos de entidades relacionadas.
    // "Encontre todos os Funcionarios pelo ID da sua entidade 'empresa'"
    List<Funcionario> findByEmpresaId(Long empresaId);

}