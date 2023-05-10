package com.akvamarin.friendsappserver.repositories;

import com.akvamarin.friendsappserver.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByVkId(String vkId);

    Optional<User> findByEmailOrVkId(String email, String vkId);

    Optional<User> findUserByUsername(String login);

}
