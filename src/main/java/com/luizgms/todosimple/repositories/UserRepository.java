package com.luizgms.todosimple.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luizgms.todosimple.models.User;

@Repository // isso aqui serve como consultas para o banco de dados do user, podemos salvar
            // dados, dar updates, deletar, tudo por aqui
public interface UserRepository extends JpaRepository<User, Long> {

    // User findByUsername(String username);//acha o usuário pelo nome de usuario

}