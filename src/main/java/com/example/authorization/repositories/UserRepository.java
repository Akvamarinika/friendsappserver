package com.example.authorization.repositories;

import com.example.authorization.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,String> {
}
