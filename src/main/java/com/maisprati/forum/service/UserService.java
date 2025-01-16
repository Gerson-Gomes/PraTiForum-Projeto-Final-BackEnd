package com.maisprati.forum.service;


import com.maisprati.forum.dto.LoginDto;
import com.maisprati.forum.dto.LoginResponseDto;
import com.maisprati.forum.dto.UserRegisterDto;
import com.maisprati.forum.dto.UserRegisterResponseDto;
import com.maisprati.forum.model.User;
import com.maisprati.forum.repository.UserRepository;
import com.maisprati.forum.util.JwtUtil;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;



    public UserRegisterResponseDto registerUser(UserRegisterDto userDto) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            return null;
        }
        if ( userRepository.findByEmail(userDto.getEmail()) != null ) {
            return null;
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFullName());
        var userSaved = userRepository.save(user);

        return new UserRegisterResponseDto(userSaved);

    }



    public ResponseEntity<?> forgotPassword(String email) {
        // Lógica para recuperação de senha
        return ResponseEntity.ok("Instruções para recuperação de senha enviadas para o email!");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
