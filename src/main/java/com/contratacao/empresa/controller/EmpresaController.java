package com.contratacao.empresa.controller;

import com.contratacao.empresa.dto.EmpresaDTO;
import com.contratacao.empresa.services.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Anotação que combina @Controller e @ResponseBody, simplificando a criação de APIs REST
@RequestMapping("/api/empresas") // Define o caminho base para todos os endpoints neste controller
@Tag(name = "Empresas", description = "Endpoints para gerenciamento de empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Operation(summary = "Lista todas as empresas cadastradas")
    @GetMapping
    public ResponseEntity<List<EmpresaDTO>> listarTodas() {
        List<EmpresaDTO> empresas = empresaService.listarTodas();
        return ResponseEntity.ok(empresas); // Retorna 200 OK com a lista no corpo
    }

    @Operation(summary = "Busca uma empresa pelo seu ID")
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDTO> buscarPorId(@PathVariable Long id) {
        EmpresaDTO empresa = empresaService.buscarPorId(id);
        return ResponseEntity.ok(empresa);
    }

    @Operation(summary = "Cria uma nova empresa")
    @PostMapping
    public ResponseEntity<EmpresaDTO> criar(@Valid @RequestBody EmpresaDTO empresaDTO) {
        // @Valid aciona as validações que definimos na DTO
        // @RequestBody indica que os dados da empresa vêm no corpo da requisição
        EmpresaDTO novaEmpresa = empresaService.criar(empresaDTO);
        return new ResponseEntity<>(novaEmpresa, HttpStatus.CREATED); // Retorna 201 Created
    }

    @Operation(summary = "Atualiza os dados de uma empresa existente")
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody EmpresaDTO empresaDTO) {
        EmpresaDTO empresaAtualizada = empresaService.atualizar(id, empresaDTO);
        return ResponseEntity.ok(empresaAtualizada);
    }

    @Operation(summary = "Deleta uma empresa pelo seu ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        empresaService.deletar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content, indicando sucesso sem corpo de resposta
    }
}