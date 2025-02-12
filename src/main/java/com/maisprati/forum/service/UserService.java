package com.maisprati.forum.service;

import com.maisprati.forum.dto.request.UserRegisterDto;
import com.maisprati.forum.dto.request.UserUpdateDto;
import com.maisprati.forum.dto.response.UserProfileResponseDto;
import com.maisprati.forum.dto.response.UserRegisterResponseDto;
import com.maisprati.forum.exception.*;
import com.maisprati.forum.model.User;
import com.maisprati.forum.repository.UserRepository;
import com.maisprati.forum.utils.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public UserProfileResponseDto editUser(Long id, UserUpdateDto userUpdateDto, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        verifyToken(token);

        String username = tokenService.extractUsername(token);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        User userToken = userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        if (!id.equals(userToken.getId())) {
            throw new InvalidTokenException("Você só pode editar o seu próprio perfil.");
        }

        // Atualizar informações básicas do usuário
        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());
        user.setEmail(userUpdateDto.getEmail());
        user.setUserName(userUpdateDto.getEmail());
        user.setDescription(userUpdateDto.getDescription());
        user.setBirthDate(userUpdateDto.getBirthDate());
        user.setLastEditionDate(LocalDateTime.now());

        return new UserProfileResponseDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        verifyToken(token);

        String username = tokenService.extractUsername(token);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        User userToken = userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        if (!id.equals(userToken.getId())) {
            throw new InvalidTokenException("Você só pode deletar o seu próprio perfil.");
        }

        userRepository.delete(user);
    }

    @Transactional
    public List<UserProfileResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserProfileResponseDto::new).toList();
    }

    @Transactional
    public UserProfileResponseDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserProfileResponseDto::new)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
    }

    @Transactional
    public UserProfileResponseDto getUserByUsername(String username) {
        return userRepository.findByUserName(username)
                .map(UserProfileResponseDto::new)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
    }

    @Transactional
    public UserRegisterResponseDto registerUser(UserRegisterDto userDto) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new InvalidPasswordException("Senhas não correspondem.");
        }
        if (userRepository.findByUserName(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Usuário já existe.");
        }

        return new UserRegisterResponseDto(userRepository.save(userDto.createUser(userDto, passwordEncoder)));
    }

    @Transactional
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void storeRefreshToken(String email, String refreshToken) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new InvalidTokenException("Token não fornecido ou inválido.");
    }

    private static void verifyToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new InvalidTokenException("Token inválido.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
    }
}
