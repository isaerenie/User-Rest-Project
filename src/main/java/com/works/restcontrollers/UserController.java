package com.works.restcontrollers;

import com.works.entities.User;
import com.works.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public ResponseEntity list() {
        return userService.list();
    }

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody User user) {
        return userService.update(user);
    }
    @DeleteMapping("/delete")
    public ResponseEntity delete(String id){
        return userService.delete(id);
    }
    @GetMapping("/searchS")
    public ResponseEntity searchS(@RequestParam String id){
        return userService.searchS(id);
    }
    @GetMapping("/searchP/{q}")
    public ResponseEntity searchP(@PathVariable String q){
        return userService.searchP(q);
    }
}