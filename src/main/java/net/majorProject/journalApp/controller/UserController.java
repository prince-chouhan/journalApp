package net.majorProject.journalApp.controller;

import lombok.extern.slf4j.Slf4j;
import net.majorProject.journalApp.api.response.WeatherResponse;
import net.majorProject.journalApp.entity.User;
import net.majorProject.journalApp.repository.UserRepository;
import net.majorProject.journalApp.service.UserService;
import net.majorProject.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

   @Autowired
   private WeatherService weatherService;

    private static final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

//    @GetMapping
//    public List<User> getAllUsers(){
//        return userService.getAll();
//    }


    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User userInDB = userService.findByUserName(userName);
            userInDB.setUserName(user.getUserName());
            userInDB.setPassword(user.getPassword());
        userService.saveNewUser(userInDB);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserById(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @GetMapping
//    public ResponseEntity<?> greeting() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userName = authentication.getName();
//        String greeting="";
//        WeatherResponse weatherResponse= weatherService.getWeather("Bhopal");
//        if(weatherResponse!=null){
//            greeting="feels like " + weatherResponse.getCurrent().getTemperature() + " , weather is " + weatherResponse.getCurrent().getCondition().getText();
//        }
//        return new ResponseEntity<>("Hi" +" " +  userName +" "+ greeting , HttpStatus.OK);
//    }
}
