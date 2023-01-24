package com.example.authorization.services;

import com.example.authorization.entities.User;

public interface UserService {
    public User getUser(User user);
    public User insertIntoDatabase(User user);
}
