package com.example.authorization.services;

import com.example.authorization.entities.User;
import com.example.authorization.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@Service
public class UserServiceImpl implements UserService{
    private final EntityManager entityManager;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(EntityManager entityManager, UserRepository userRepository) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(User user) {
        TypedQuery<User> typedQuery = entityManager.createQuery(
                "FROM User WHERE password = :password AND username = :username", User.class);
        try {
            User userFromDB = typedQuery.setParameter("password", user.getPassword()).setParameter("username", user.getNickname()).getSingleResult();
            return userFromDB;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public User insertIntoDatabase(User user) {
        return userRepository.save(user);
    }
}
