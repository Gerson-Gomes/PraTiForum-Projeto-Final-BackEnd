package com.maisprati.forum.service;

import com.maisprati.forum.dto.request.UserRegisterDto;
import com.maisprati.forum.dto.request.UserUpdateDto;
import com.maisprati.forum.dto.response.UserRegisterResponseDto;
import com.maisprati.forum.dto.response.UserProfileResponseDto;
import com.maisprati.forum.model.User;
import com.maisprati.forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserRegisterResponseDto registerUser(UserRegisterDto userDto) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new RuntimeException("Senhas não correspondem.");
        }
        if (userRepository.findByUserName(userDto.getEmail()) != null) {
            throw new RuntimeException("Usuário já existe.");
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setUserName(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFullName());
        user = userRepository.save(user);

        return new UserRegisterResponseDto(user);
    }

    public void storeRefreshToken(String email, String refreshToken) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
        }
    }

    public List<UserProfileResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserProfileResponseDto::new)
                .collect(Collectors.toList());
    }

    public UserProfileResponseDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
        return new UserProfileResponseDto(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public UserProfileResponseDto editUser(Long id, UserUpdateDto userUpdateDto, HttpServletRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());
        user.setEmail(userUpdateDto.getEmail());
        user.setDescription(userUpdateDto.getDescription());
        user.setBirthDate(userUpdateDto.getBirthDate());

        user = userRepository.save(user);
        return new UserProfileResponseDto(user);
    }

    public void deleteUser(Long id, HttpServletRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
        userRepository.delete(user);
    }

    public UserProfileResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado.");
        }
        return new UserProfileResponseDto(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado.");
        }
        return user;
    }
}
