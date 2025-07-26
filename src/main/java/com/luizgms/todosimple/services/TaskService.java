package com.luizgms.todosimple.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luizgms.todosimple.models.Task;
import com.luizgms.todosimple.models.User;
import com.luizgms.todosimple.repositories.TaskRepository;

import jakarta.transaction.Transactional;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task taskfindById(Long id){
        Optional<Task> task = this.taskRepository.findById(id);

        return task.orElseThrow(
            () -> new RuntimeException(
                "Task de id: "+id+" não encontrada"
            )
        );
    }

    @Transactional
    public Task createTask(Task task){
        User user= this.userService.userFindById(task.getUser().getId());
        task.setUser(user);
        task.setId(null);
        task= this.taskRepository.save(task);
        return task;
    }

    @Transactional
    public Task updateTask(Task task){
        Task nTask= this.taskfindById(task.getId());
        nTask.setDescription(task.getDescription());
        return this.taskRepository.save(nTask);
    }

    public void deleteTask(Long id){
        Task nTask= this.taskfindById(id);
        try {
            this.taskRepository.delete(nTask);
        } catch (Exception e) {
            throw new RuntimeException(
                "Task de id "+nTask.getId()+" não foi deletada"
            );
        }
    }

    public List<Task> findAllByUserId(Long id){
        List<Task> lista = this.taskRepository.findByUser_id(id);
        return lista;
    }
 }
