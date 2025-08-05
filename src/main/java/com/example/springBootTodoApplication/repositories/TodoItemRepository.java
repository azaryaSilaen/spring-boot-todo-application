package com.example.springBootTodoApplication.repositories;/*
Author: Azarya Silaen
 */

import com.example.springBootTodoApplication.models.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
}
