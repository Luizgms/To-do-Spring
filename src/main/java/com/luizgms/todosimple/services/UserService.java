package com.luizgms.todosimple.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luizgms.todosimple.models.User;
import com.luizgms.todosimple.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service //avisa pro spring subir isso junto com o servidor como um service
public class UserService {
    //camada de negocios do spring que tras muita reusabilidade


    @Autowired
    private UserRepository userRepository;

    public User userFindById(Long id){
        Optional<User> user= this.userRepository.findById(id);//isso aqui pode ou não receber um usuário e opcinal, caso por exemplo não exista um usuário com esse ID no banco de dados

        return user.orElseThrow(
            () -> new RuntimeException(
                "Usuário de id:" +id+ " não foi encontrado"
            )// exeção que não para o programa em tempo de execução
        );//isso é usado junto com o tipo optional, ele so vai retonar se tiver um usuário, caso não tenha ele vai jogar uma excessão
    }

    @Transactional// tem um controle melhor do que ta acontecendo, ou ele vai salvar tudo ou não vai salvar nada, usar sempre em coisas que salvam no banco, inserçoes no banco
    public User createUser(User user){
        user.setId(null);// temos que garantir que o ID venha zerado, isso pode causar confusão no banco de dados
        user= this.userRepository.save(user);
        return user;
    }

    @Transactional
    public User updatUser(User user){
        User nUser= this.userFindById(user.getId());
        nUser.setPassword(user.getPassword());
        
        return this.userRepository.save(nUser);
    }

    public void deleteUser(Long id){
        User nUser= this.userFindById(id);
        try {
            this.userRepository.delete(nUser);
        } catch (Exception e) {
            throw new RuntimeException(
                "Usuário "+nUser.getUsername()+" não encontrado"
            );
        }
        
    }
}
