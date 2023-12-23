package com.erismaroliveira.todonote.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erismaroliveira.todonote.models.Task;
import com.erismaroliveira.todonote.models.projection.TaskProjection;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  
  List<TaskProjection> findByUser_Id(Long id);

  // @Query(value = "SELECT t FROM Task t WHERE t.user.id = :id")
  // List<Task> findByUserId(@Param("id") Long id);

  // @Query(value = "SELECT * FROM task t WHERE t.user_id = :id", nativeQuery = true)
  // List<Task> findByUserId(@Param("id") Long id);

}
