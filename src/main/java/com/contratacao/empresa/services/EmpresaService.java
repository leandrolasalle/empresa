package com.contratacao.empresa.services;

import com.contratacao.empresa.dto.EmpresaDTO;
import com.contratacao.empresa.dto.FuncionarioDTO;
import com.contratacao.empresa.entidade.Empresa;
import com.contratacao.empresa.repositories.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service // Marca a classe como um serviço gerenciado pelo Spring
public class EmpresaService {

    @Autowired // Injeta a dependência do nosso repositório
    private EmpresaRepository empresaRepository;

    // --- MÉTODOS DE CONVERSÃO (MAPEAMENTO) ---

    private EmpresaDTO toEmpresaDTO(Empresa empresa) {
        EmpresaDTO dto = new EmpresaDTO();
        dto.setId(empresa.getId());
        dto.setNome(empresa.getNome());
        dto.setCnpj(empresa.getCnpj());

        // Mapeia a lista de entidades Funcionario para uma lista de FuncionarioDTO
        if (empresa.getFuncionarios() != null) {
            List<FuncionarioDTO> funcionarioDTOs = empresa.getFuncionarios().stream().map(funcionario -> {
                FuncionarioDTO funcDto = new FuncionarioDTO();
                funcDto.setId(funcionario.getId());
                funcDto.setNome(funcionario.getNome());
                funcDto.setCargo(funcionario.getCargo());
                funcDto.setCpf(funcionario.getCpf());
                funcDto.setEmpresaId(empresa.getId()); // Adiciona o ID da empresa
                return funcDto;
            }).collect(Collectors.toList());
            dto.setFuncionarios(funcionarioDTOs);
        }
        return dto;
    }

    private Empresa toEmpresaEntity(EmpresaDTO dto) {
        Empresa empresa = new Empresa();
        empresa.setId(dto.getId());
        empresa.setNome(dto.getNome());
        empresa.setCnpj(dto.getCnpj());
        // A lista de funcionários é gerenciada pelo FuncionarioService
        return empresa;
    }

    // --- MÉTODOS DE NEGÓCIO ---

    @Transactional(readOnly = true) // Transação apenas de leitura, mais otimizada
    public List<EmpresaDTO> listarTodas() {
        List<Empresa> empresas = empresaRepository.findAll();
        // Converte a lista de Entidades para uma lista de DTOs
        return empresas.stream()
                .map(this::toEmpresaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmpresaDTO buscarPorId(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com o ID: " + id));
        return toEmpresaDTO(empresa);
    }

    @Transactional // Transação de escrita (padrão)
    public EmpresaDTO criar(EmpresaDTO empresaDTO) {
        // Validação de negócio (ex: verificar se CNPJ já existe)
        empresaRepository.findByCnpj(empresaDTO.getCnpj()).ifPresent(e -> {
            throw new IllegalArgumentException("Uma empresa com este CNPJ já existe.");
        });

        // ====================================================================
        // CORREÇÃO APLICADA AQUI:
        // Esta linha garante que, ao criar uma nova empresa, o ID seja nulo.
        // Isso força o JPA a executar um INSERT (criar novo) em vez de um
        // UPDATE (atualizar existente), resolvendo o erro anterior.
        empresaDTO.setId(null);
        // ====================================================================

        Empresa empresa = toEmpresaEntity(empresaDTO);
        empresa = empresaRepository.save(empresa);
        return toEmpresaDTO(empresa);
    }

    @Transactional
    public EmpresaDTO atualizar(Long id, EmpresaDTO empresaDTO) {
        // Busca a empresa existente para garantir que ela existe antes de atualizar
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com o ID: " + id));

        // Atualiza os dados da entidade com os dados do DTO
        empresa.setNome(empresaDTO.getNome());
        empresa.setCnpj(empresaDTO.getCnpj());

        // Salva a entidade atualizada
        empresa = empresaRepository.save(empresa);
        return toEmpresaDTO(empresa);
    }

    @Transactional
    public void deletar(Long id) {
        if (!empresaRepository.existsById(id)) {
            throw new EntityNotFoundException("Empresa não encontrada com o ID: " + id);
        }
        empresaRepository.deleteById(id);
    }


}
