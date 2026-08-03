package net.majorProject.journalApp.controller;

import net.majorProject.journalApp.entity.User;
import net.majorProject.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/public")
public class publicController {

    @Autowired
    private UserService userService;
    PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    @GetMapping("/health-check")
    public String healthCheck(){return "ok";}

    @PostMapping("/create-user")
    public void createUser(@RequestBody User user){
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRoles(Arrays.asList("USER"));
        userService.saveNewUser(user);
    }
}

