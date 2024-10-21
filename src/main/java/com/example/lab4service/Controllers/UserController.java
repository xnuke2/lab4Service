package com.example.lab4service.Controllers;

import com.example.lab4service.Entity.UserEntity;
import com.example.lab4service.Services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UsersService usersService;



    @GetMapping
    public ResponseEntity<List<UserEntity>> getListUsers(){
        List<UserEntity> allUsers = usersService.GetAllUsers();
        return allUsers != null &&  !allUsers.isEmpty()
                ? new ResponseEntity<>(allUsers, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/{login}")
    public ResponseEntity<?> getUser( @PathVariable String login){
        UserEntity user = usersService.GetUserById(login);

        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping
    public ResponseEntity<?> addUser(@RequestParam String login,@RequestParam String name,@RequestParam int age){
        return usersService.AddUser(login,name,age)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestParam String login){
        return usersService.DeleteUserById(login)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
    @PutMapping("/{login}")
    public ResponseEntity<?> changeUser
            (@PathVariable String login,
             @RequestParam String newlogin,
             @RequestParam String name,
             @RequestParam int age){
        return usersService.ChangeUser(login,newlogin,name,age)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
