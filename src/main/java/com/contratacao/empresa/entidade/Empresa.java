package com.contratacao.empresa.entidade;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data // Anotação do Lombok para gerar Getters, Setters, toString, etc.
@Entity // Marca esta classe como uma entidade JPA
@Table(name = "empresas") // Define o nome da tabela no banco de dados
public class Empresa {

    @Id // Marca este campo como a chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // O banco de dados gerará o ID automaticamente
    private Long id;

    @NotBlank(message = "O nome da empresa não pode ser vazio.")
    @Column(nullable = false, unique = true)
    private String nome;

    @NotBlank(message = "O CNPJ não pode ser vazio.")
    @Size(min = 14, max = 14, message = "O CNPJ deve ter 14 caracteres.")
    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;

    // Relacionamento: Uma empresa para muitos funcionários
    // cascade = CascadeType.ALL: Se salvarmos/deletarmos uma empresa, os funcionários associados também serão.
    // orphanRemoval = true: Se um funcionário for removido da lista, ele será deletado do banco.
    // mappedBy = "empresa": Indica que o lado 'Funcionario' é o dono do relacionamento.
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // Evita loop infinito ao serializar para JSON (lado "pai" da relação)
    private List<Funcionario> funcionarios;
}