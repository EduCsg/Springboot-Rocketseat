package br.com.casagrande.todolist.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/")
    public String create(@RequestBody UserModel userModel) {
        return "O usuário " + userModel.getName() + " foi criado!";
    }
}
