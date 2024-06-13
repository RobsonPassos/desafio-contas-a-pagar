package br.com.bancorob.desafiocontasapagar.application;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    public MyUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Aqui você pode carregar o usuário do banco de dados
        String encodedPassword = passwordEncoder.encode("admin");

        return User.withUsername("admin")
                .password(encodedPassword) // Define a senha codificada
                .roles("USER")
                .build();
    }
}