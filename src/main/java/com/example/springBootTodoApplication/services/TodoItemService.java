package com.example.springBootTodoApplication.services;/*
Author: Azarya Silaen
 */

import com.example.springBootTodoApplication.models.TodoItem;
import com.example.springBootTodoApplication.repositories.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class TodoItemService {

    @Autowired
    private TodoItemRepository todoItemRepository;

    public Iterable<TodoItem> getAll() {
        return todoItemRepository.findAll();
    }

    public Optional<TodoItem> getById(Long id) {
        return todoItemRepository.findById(id);
    }

    public TodoItem save(TodoItem todoItem){
        if (todoItem.getId() == null) {
            todoItem.setCreatedAt(Instant.now());
        }
        todoItem.setUpdatedAt(Instant.now());
        return todoItemRepository.save(todoItem); //20:30 FIXME
    }

    public void delete(TodoItem todoItem) {
        todoItemRepository.delete(todoItem);
    }
}
