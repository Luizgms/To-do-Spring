package com.luizgms.todosimple.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = User.TABLE_NAME) // Cria o a table no banco de dados com esse nome
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class User {
    public interface CreateUser {
    }// isso garante que tanto no create quanto no validate eu tenha que validar
     // todas os requerimentos

    public interface UpdateUser {
    }

    public static final String TABLE_NAME = "user"; // Variavel que define o nome do banco de dados

    @Id // Annotation usadas para ids
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Usado para gerar um valor de id unico para cada novo user
    @Column(name = "id", unique = true) // Definição de como vai estar o ID na tabela de user
    private Long id;

    @Column(name = "username", length = 100, nullable = false, unique = true)
    @NotNull(groups = CreateUser.class) // impede que esse valor seja nulo
    @NotEmpty(groups = CreateUser.class) // impede que esse valor seja uma string vazia
    @Size(min = 2, max = 100) // define o tamanho que esse atributo tem no banco de dados

    private String username;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "password", length = 60, nullable = false)
    @NotNull(groups = { CreateUser.class, UpdateUser.class }) // impede que esse valor seja nulo
    @NotEmpty(groups = { CreateUser.class, UpdateUser.class }) // impede que esse valor seja uma string vazia
    @Size(min = 8, max = 60)
    private String password;

    @OneToMany(mappedBy = "user") // isso indica qual variavel da outra classe que vai mapear, nesse caso vai ser
                                  // usado userID
    @JsonProperty(access = Access.WRITE_ONLY)
    private List<Task> tasks = new ArrayList<Task>();

}
