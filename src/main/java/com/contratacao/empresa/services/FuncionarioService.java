package com.contratacao.empresa.services;

import com.contratacao.empresa.dto.FuncionarioDTO;
import com.contratacao.empresa.entidade.Empresa;
import com.contratacao.empresa.entidade.Funcionario;
import com.contratacao.empresa.repositories.EmpresaRepository;
import com.contratacao.empresa.repositories.FuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository; // Precisamos para associar o funcionário

    // --- MÉTODOS DE CONVERSÃO (MAPEAMENTO) ---

    private FuncionarioDTO toFuncionarioDTO(Funcionario funcionario) {
        FuncionarioDTO dto = new FuncionarioDTO();
        dto.setId(funcionario.getId());
        dto.setNome(funcionario.getNome());
        dto.setCargo(funcionario.getCargo());
        dto.setCpf(funcionario.getCpf());
        if (funcionario.getEmpresa() != null) {
            dto.setEmpresaId(funcionario.getEmpresa().getId());
        }
        return dto;
    }

    // --- MÉTODOS DE NEGÓCIO ---

    @Transactional(readOnly = true)
    public List<FuncionarioDTO> listarFuncionariosPorEmpresa(Long idEmpresa) {
        if (!empresaRepository.existsById(idEmpresa)) {
            throw new EntityNotFoundException("Empresa não encontrada com o ID: " + idEmpresa);
        }
        List<Funcionario> funcionarios = funcionarioRepository.findByEmpresaId(idEmpresa);
        return funcionarios.stream()
                .map(this::toFuncionarioDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FuncionarioDTO contratar(Long idEmpresa, FuncionarioDTO funcionarioDTO) {
        // Busca a empresa que vai contratar o funcionário
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com o ID: " + idEmpresa));

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(funcionarioDTO.getNome());
        funcionario.setCargo(funcionarioDTO.getCargo());
        funcionario.setCpf(funcionarioDTO.getCpf());
        funcionario.setEmpresa(empresa); // Associa o funcionário à empresa!

        funcionario = funcionarioRepository.save(funcionario);
        return toFuncionarioDTO(funcionario);
    }

    @Transactional
    public void demitir(Long idFuncionario) {
        if (!funcionarioRepository.existsById(idFuncionario)) {
            throw new EntityNotFoundException("Funcionário não encontrado com o ID: " + idFuncionario);
        }
        funcionarioRepository.deleteById(idFuncionario);
    }
    // Dentro da classe FuncionarioService

    @Transactional
    public FuncionarioDTO atualizar(Long idFuncionario, FuncionarioDTO funcionarioDTO) {
        // Busca o funcionário no banco de dados
        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado com o ID: " + idFuncionario));

        // Atualiza apenas os campos que fazem sentido serem alterados
        funcionario.setNome(funcionarioDTO.getNome());
        funcionario.setCargo(funcionarioDTO.getCargo());
        // Note que não permitimos a troca do CPF ou da empresa por este método

        funcionario = funcionarioRepository.save(funcionario);
        return toFuncionarioDTO(funcionario); // Reutilizamos nosso método de conversão
    }
}