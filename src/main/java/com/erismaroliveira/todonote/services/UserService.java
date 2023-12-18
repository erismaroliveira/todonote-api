package com.erismaroliveira.todonote.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erismaroliveira.todonote.models.User;
import com.erismaroliveira.todonote.repositories.TaskRepository;
import com.erismaroliveira.todonote.repositories.UserRepository;

@Service
public class UserService {
  
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TaskRepository taskRepository;

  public User findById(Long id) {
    Optional<User> user = this.userRepository.findById(id);
    return user.orElseThrow(() -> new RuntimeException("User not found" + "ID: " + id + ", Type: " + User.class.getName()));
  }

  @Transactional
  public User create(User user) {
    user.setId(null);
    user = this.userRepository.save(user);
    this.taskRepository.saveAll(user.getTasks());
    return user;
  }

  @Transactional
  public User update(User user) {
    User newUser = this.findById(user.getId());
    newUser.setPassword(user.getPassword());
    return this.userRepository.save(newUser);
  }

  public void delete(Long id) {
    this.findById(id);
    try {
      this.userRepository.deleteById(id);
    } catch (Exception e) {
      throw new RuntimeException("User has tasks, it can't be deleted");
    }
  }
}
