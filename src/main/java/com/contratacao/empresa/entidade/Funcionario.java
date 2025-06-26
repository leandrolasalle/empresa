package com.contratacao.empresa.entidade;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "funcionarios")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do funcionário não pode ser vazio.")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "O cargo não pode ser vazio.")
    private String cargo;

    @NotBlank(message = "O CPF não pode ser vazio.")
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    // Relacionamento: Muitos funcionários para uma empresa
    @ManyToOne(fetch = FetchType.LAZY) // LAZY: Carrega a empresa somente quando for necessário
    @JoinColumn(name = "empresa_id", nullable = false) // Define a coluna de chave estrangeira
    @JsonBackReference // Evita loop infinito ao serializar para JSON (lado "filho" da relação)
    private Empresa empresa;
}