package br.com.casagrande.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.casagrande.todolist.user.IUserRepository;
import br.com.casagrande.todolist.user.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();

        if (servletPath.equals("/tasks/")) {
            // pegar a auth (username e password)
            String authorization = request.getHeader("Authorization");

            // pega apenas o token em Base64
            String token64 = authorization.substring("Basic".length()).trim();

            String authDecode = new String(Base64.getDecoder().decode(token64));

            String[] credentials = authDecode.split(":");

            String username = credentials[0];
            String password = credentials[1];

            UserModel user = this.userRepository.findByUsername(username);

            if (user == null) {
                response.sendError(401);
            } else {
                // valida a senha
                BCrypt.Result passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

                if (passwordVerify.verified) {
                    // segue viagem
                    filterChain.doFilter(request, response);

                } else {
                    response.sendError(401);
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
