package com.example.springBootTodoApplication.controllers;/*
Author: Azarya Silaen
 */

import com.example.springBootTodoApplication.models.TodoItem;
import com.example.springBootTodoApplication.repositories.TodoItemRepository;
import com.example.springBootTodoApplication.services.TodoItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

@Controller
public class TodoFormController {

    @Autowired
    private TodoItemService todoItemService;
    private TodoItemRepository todoItemRepository;
    public static ArrayList<Long> deletedID = new ArrayList<>();

    @GetMapping("/create-todo")
    public String showCreateForm(TodoItem todoItem) {
        return "new-todo-item";
    }

    @PostMapping("/todo")
    public String createTodoItem(@Valid TodoItem todoItem, BindingResult result, Model model) throws SQLException {
        String jdbcURL = "jdbc:h2:file:./data/demo";
        String username = "admin";
        String password = "password";
        Connection connection = DriverManager.getConnection(jdbcURL, username, password);
        System.out.println("Connected to H2 in server mode. (CREATE)");
        if (!deletedID.isEmpty()) {
            
            int first = Math.toIntExact(deletedID.get(0));
            String sql = "ALTER SEQUENCE address_sequence RESTART WITH " + first;
            deletedID.remove(0);
            Statement statement = connection.createStatement();
            System.out.println("ADD HOLE SUCCESSFUL");
            statement.executeUpdate(sql);
            connection.close();
        
    }
        else {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT COUNT (*) FROM TODO_ITEMS");
            int next = 0;
            if (rs.next()) {
                next = rs.getInt(1)+1;
            }
            System.out.println(next);
            statement.executeUpdate("ALTER SEQUENCE address_sequence RESTART WITH " + next);

        }

        TodoItem item = new TodoItem();
        item.setDescription(todoItem.getDescription());
        item.setIsComplete(todoItem.getIsComplete());

        todoItemService.save(todoItem);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteTodoItem(@PathVariable("id") Long id, Model model) {
        TodoItem todoItem = todoItemService
                .getById(id)
                .orElseThrow(() -> new IllegalArgumentException("TodoItem id: " + id + " not found"));

        deletedID.add(id);
        Collections.sort(deletedID);

       /* Optional<TodoItem> todoItem = todoItemService.getById(id);
        if (todoItem.isPresent()) {
            todoItem.get(); }*/

        todoItemService.delete(todoItem);

            try {
                String jdbcURL = "jdbc:h2:file:./data/demo";
                String username = "admin";
                String password = "password";

                Connection connection = DriverManager.getConnection(jdbcURL, username, password);
                System.out.println("Connected to H2 in server mode.");
                String sql = "ALTER SEQUENCE address_sequence RESTART WITH 1";
                System.out.println("DELETE SUCCESSFUL");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT COUNT (*) FROM TODO_ITEMS");

                int next = -1;
                if (rs.next()) {
                    next = rs.getInt(1);
                }
                if (next == 0) {
                    System.out.println("START FROM BOTTOM");
                    statement.executeUpdate(sql);
                    connection.close();
                    deletedID.clear();
                }
            } catch(Exception e){ System.out.println(e);}

        return "redirect:/";

    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        TodoItem todoItem = todoItemService
                .getById(id)
                .orElseThrow(() -> new IllegalArgumentException("TodoItem id: " + id + " not found"));

        model.addAttribute("todo", todoItem);
        return "edit-todo-item";
    }

    @PostMapping("/todo/{id}")
    public String UpdateTodoItem(@PathVariable("id") Long id, @Valid TodoItem todoItem, BindingResult bindingResult, Model model) {
        TodoItem item = todoItemService
                .getById(id)
                .orElseThrow(() -> new IllegalArgumentException("TodoItem id: " + id + " not found"));

        item.setIsComplete(todoItem.getIsComplete());
        item.setDescription(todoItem.getDescription());

        todoItemService.save(todoItem);

        return "redirect:/";
    }
}
