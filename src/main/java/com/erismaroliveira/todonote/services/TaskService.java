package com.erismaroliveira.todonote.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erismaroliveira.todonote.models.Task;
import com.erismaroliveira.todonote.models.User;
import com.erismaroliveira.todonote.models.enums.ProfileEnum;
import com.erismaroliveira.todonote.models.projection.TaskProjection;
import com.erismaroliveira.todonote.repositories.TaskRepository;
import com.erismaroliveira.todonote.security.UserSpringSecurity;
import com.erismaroliveira.todonote.services.exceptions.AuthorizationException;
import com.erismaroliveira.todonote.services.exceptions.DataBindingViolationException;
import com.erismaroliveira.todonote.services.exceptions.ObjectNotFoundException;

@Service
public class TaskService {

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private UserService userService;

  public Task findById(Long id) {
    Task task = this.taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
      "Task not found" + "ID: " + id + ", Type: " + Task.class.getName()));

    UserSpringSecurity userSpringSecurity = UserService.authenticated();
    if (Objects.isNull(userSpringSecurity)
      || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !userHasTask(userSpringSecurity, task))
      throw new AuthorizationException("Access denied");

    return task;
  }

  public List<TaskProjection> findAllByUser() {
    UserSpringSecurity userSpringSecurity = UserService.authenticated();
    if (Objects.isNull(userSpringSecurity))
      throw new AuthorizationException("Access denied");

    List<TaskProjection> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());
    return tasks;
  }

  @Transactional
  public Task create(Task task) {
    UserSpringSecurity userSpringSecurity = UserService.authenticated();
    if (Objects.isNull(userSpringSecurity))
      throw new AuthorizationException("Access denied");

    User user = this.userService.findById(userSpringSecurity.getId());
    task.setId(null);
    task.setUser(user);
    task = this.taskRepository.save(task);
    return task;
  }

  @Transactional
  public Task update(Task task) {
    Task newTask = this.findById(task.getId());
    newTask.setDescription(task.getDescription());
    return this.taskRepository.save(newTask);
  }

  public void delete(Long id) {
    this.findById(id);
    try {
      this.taskRepository.deleteById(id);
    } catch (Exception e) {
      throw new DataBindingViolationException("Task not found" + "ID: " + id + ", Type: " + Task.class.getName());
    }
  }

  public Boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task) {
    return task.getUser().getId().equals(userSpringSecurity.getId());
  }
}
