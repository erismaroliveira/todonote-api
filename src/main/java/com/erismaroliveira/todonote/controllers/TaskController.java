package com.erismaroliveira.todonote.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.erismaroliveira.todonote.models.Task;
import com.erismaroliveira.todonote.services.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/tasks")
@Validated
public class TaskController {

  @Autowired
  private TaskService taskService;

  @GetMapping("/{id}")
  public ResponseEntity<Task> findById(@PathVariable Long id) {
    Task task = this.taskService.findById(id);
    return ResponseEntity.ok().body(task);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<Task>> findAllByUserId(@PathVariable Long userId) {
    List<Task> tasks = this.taskService.findAllByUserId(userId);
    return ResponseEntity.ok().body(tasks);
  }

  @PostMapping
  @Validated
  public ResponseEntity<Void> create(@Valid @RequestBody Task task) {
    this.taskService.create(task);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
      .path("/{id}").buildAndExpand(task.getId()).toUri();
    return ResponseEntity.created(uri).build();
  }

  @PutMapping("/{id}")
  @Validated
  public ResponseEntity<Void> update(@Valid @PathVariable Long id, @RequestBody Task task) {
    task.setId(id);
    this.taskService.update(task);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    this.taskService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
