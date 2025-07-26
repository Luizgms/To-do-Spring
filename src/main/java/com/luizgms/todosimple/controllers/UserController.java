package com.luizgms.todosimple.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.luizgms.todosimple.models.User;
import com.luizgms.todosimple.models.User.CreateUser;
import com.luizgms.todosimple.models.User.UpdateUser;
import com.luizgms.todosimple.services.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController//creio que aqui é onde nos vamos lidar com as requisiçoes do front end, é onde vamos receber as informaçoes e usar os nossos services
@RequestMapping("/user")//acho que isso define o caminho das buscas, delimita que a rota base desse controller é o /user
@Validated//temos que validar esse controler, as validações dele
public class UserController {
    
    @Autowired// isso funciona como um contrutor para instnaciar essa classe
    private UserService userService;

    //essa requisição ficaria "localhost:8080/user/{id}"
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(/*isso indica que ele tem que lincar o ID com esse aqui */ @PathVariable  Long id){
        //isso trata os dados, em vez de chegar baguçado ele retorna os dados limpos e organizados

        User user = this.userService.userFindById(id);

        return ResponseEntity.ok().body(user);
    }

    @PostMapping
    @Validated(CreateUser.class)
    public ResponseEntity<Void> create(@Valid @RequestBody User user){
        this.userService.createUser(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @Validated(UpdateUser.class)
    public ResponseEntity<Void> update(@Valid @RequestBody User user, @PathVariable long id){
        user.setId(id);
        this.userService.updatUser(user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

}
