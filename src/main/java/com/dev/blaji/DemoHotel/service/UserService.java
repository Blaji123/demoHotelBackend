package com.dev.blaji.DemoHotel.service;

import com.dev.blaji.DemoHotel.model.User;

import java.util.List;

public interface UserService {
    User registerUser(User user);
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);
}
