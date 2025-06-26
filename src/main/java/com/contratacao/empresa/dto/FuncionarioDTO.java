package com.contratacao.empresa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FuncionarioDTO {

    private Long id; // Útil para respostas da API

    @NotBlank(message = "O nome do funcionário não pode ser vazio.")
    private String nome;

    @NotBlank(message = "O cargo não pode ser vazio.")
    private String cargo;

    @NotBlank(message = "O CPF não pode ser vazio.")
    private String cpf;

    // Importante: Note que não colocamos a entidade 'Empresa' aqui.
    // Para requisições de criação, o ID da empresa virá pela URL.
    // Para respostas, podemos adicionar um 'empresaId' se necessário.
    private Long empresaId;
}