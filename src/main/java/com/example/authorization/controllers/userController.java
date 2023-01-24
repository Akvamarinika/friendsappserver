package com.example.authorization.controllers;

import com.example.authorization.entities.User;
import com.example.authorization.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
public class userController {
    @Autowired
    UserService userService;

    @GetMapping("/getUser")
    public User getUser(@RequestBody User user) {
        return userService.getUser(user);
    }

    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) {
        return userService.insertIntoDatabase(user);
    }

    @PostMapping("/clientSendToken")
    public String sendToken(@RequestBody String token) {
        //userService.
        return "1111111111";
    } //@RequestBody в теле запроса отправляет клиент
}
