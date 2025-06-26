package com.contratacao.empresa.controller;

import com.contratacao.empresa.dto.FuncionarioDTO;
import com.contratacao.empresa.services.FuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// Note o caminho: as operações de funcionários estão "aninhadas" sob o caminho de uma empresa específica
@RequestMapping("/api/empresas/{idEmpresa}/funcionarios")
@Tag(name = "Funcionários", description = "Endpoints para gerenciamento de funcionários de uma empresa")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @Operation(summary = "Lista todos os funcionários de uma empresa específica")
    @GetMapping
    public ResponseEntity<List<FuncionarioDTO>> listarFuncionariosPorEmpresa(@PathVariable Long idEmpresa) {
        List<FuncionarioDTO> funcionarios = funcionarioService.listarFuncionariosPorEmpresa(idEmpresa);
        return ResponseEntity.ok(funcionarios);
    }

    @Operation(summary = "Contrata um novo funcionário para uma empresa")
    @PostMapping
    public ResponseEntity<FuncionarioDTO> contratar(@PathVariable Long idEmpresa, @Valid @RequestBody FuncionarioDTO funcionarioDTO) {
        FuncionarioDTO novoFuncionario = funcionarioService.contratar(idEmpresa, funcionarioDTO);
        return new ResponseEntity<>(novoFuncionario, HttpStatus.CREATED);
    }

    @Operation(summary = "Demite (deleta) um funcionário pelo seu ID")
    // A URL aqui não precisa do ID da empresa, pois o ID do funcionário já é único
    @DeleteMapping("/{idFuncionario}")
    public ResponseEntity<Void> demitir(@PathVariable Long idEmpresa, @PathVariable Long idFuncionario) {
        // O idEmpresa pode ser usado para validações extras, se necessário
        funcionarioService.demitir(idFuncionario);
        return ResponseEntity.noContent().build();
    }
    // Dentro da classe FuncionarioController

    @Operation(summary = "Atualiza os dados de um funcionário existente")
    @PutMapping("/{idFuncionario}")
    public ResponseEntity<FuncionarioDTO> atualizar(@PathVariable Long idEmpresa,
                                                    @PathVariable Long idFuncionario,
                                                    @Valid @RequestBody FuncionarioDTO funcionarioDTO) {
        // O idEmpresa não é usado aqui, mas mantém a estrutura da URL consistente.
        // Poderia ser usado para uma validação extra, se necessário.
        FuncionarioDTO funcionarioAtualizado = funcionarioService.atualizar(idFuncionario, funcionarioDTO);
        return ResponseEntity.ok(funcionarioAtualizado);
    }
}