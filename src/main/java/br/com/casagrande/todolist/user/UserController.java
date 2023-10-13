package br.com.casagrande.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody UserModel userModel) {

        UserModel user = this.userRepository.findByUsername(userModel.getUsername());

        if (user != null) return ResponseEntity.badRequest().body("User already exists");

        return ResponseEntity.ok(this.userRepository.save(userModel));
    }
}
