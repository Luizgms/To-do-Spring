package com.luizgms.todosimple.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Entity
@Table(name = User.TABLE_NAME) // Cria o a table no banco de dados com esse nome
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
    private List<Task> tasks = new ArrayList<Task>();

    @JsonIgnore//ignora a lista de tarefas se tentarmos retornar todos os dados de um usuário
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public User() {
    }

    public User(Long id,
            @NotNull(groups = CreateUser.class) @NotEmpty(groups = CreateUser.class) @Size(min = 2, max = 100) String username,
            @NotNull(groups = { CreateUser.class, UpdateUser.class }) @NotEmpty(groups = { CreateUser.class,
                    UpdateUser.class }) @Size(min = 8, max = 60) String password,
            List<Task> tasks) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.tasks = tasks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        return true;
    }

}
