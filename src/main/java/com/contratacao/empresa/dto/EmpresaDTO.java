package com.contratacao.empresa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class EmpresaDTO {

    private Long id;

    @NotBlank(message = "O nome da empresa não pode ser vazio.")
    private String nome;

    @NotBlank(message = "O CNPJ não pode ser vazio.")
    @Size(min = 14, max = 14, message = "O CNPJ deve ter 14 caracteres.")
    private String cnpj;

    // Ponto chave do DTO: A empresa não expõe a ENTIDADE Funcionario,
    // mas sim uma lista de FuncionarioDTO.
    private List<FuncionarioDTO> funcionarios;
}