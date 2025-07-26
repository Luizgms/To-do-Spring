package com.luizgms.todosimple.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luizgms.todosimple.models.Task;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser_id(Long id);// aqui tentamos retornar todas as tasks de um usuário a partir do objeto de
                                      // User, usando o atributo id dele

    // Optional<Task> findById(Long id);

    // @Query(value = "select t FROM Task Where t.user.id = :id")
    // List<Task> findByUser_id(@Param("id") long id); //query muito parecida com
    // SQL padrão

    // @Query(value ="SELECT * FROM task t WHERE t.user_id = :id",nativeQuery =
    // true)//isso aqui seria uma consulta como deveria ser feita em um SQL
    // List<Task> findByUser_id(@Param("id") long id);

}
