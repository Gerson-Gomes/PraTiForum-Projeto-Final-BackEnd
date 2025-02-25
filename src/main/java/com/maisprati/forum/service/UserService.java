package com.maisprati.forum.service;

import com.maisprati.forum.dto.request.UserRegisterDto;
import com.maisprati.forum.dto.request.UserUpdateDto;
import com.maisprati.forum.dto.response.UserProfileResponseDto;
import com.maisprati.forum.dto.response.UserRegisterResponseDto;
import com.maisprati.forum.exception.*;
import com.maisprati.forum.model.User;
import com.maisprati.forum.model.UserRole;
import com.maisprati.forum.repository.UserRepository;
import com.maisprati.forum.utils.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public UserProfileResponseDto editUser(Long id, UserUpdateDto userUpdateDto, HttpServletRequest request) {
        String token = tokenService.getTokenFromRequest(request);
        verifyToken(token);

        Long userId = tokenService.extractUserId(token);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        User userToken = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        if (!id.equals(userToken.getId())) {
            throw new InvalidTokenException("Você só pode editar o seu próprio perfil.");
        }

        // Atualizar informações baicas do usuario
        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());
        user.setEmail(userUpdateDto.getEmail());
        user.setUserName(userUpdateDto.getEmail());
        user.setDescription(userUpdateDto.getDescription());
        user.setBirthDate(LocalDate.parse(userUpdateDto.getBirthDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        user.setLastEditionDate(LocalDateTime.now());

        return new UserProfileResponseDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id, HttpServletRequest request) {
        String token = tokenService.getTokenFromRequest(request);
        verifyToken(token);

        String username = tokenService.extractUsername(token);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        User userToken = userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        if (!id.equals(userToken.getId())) {
            throw new TokenInvalidExpection("Você só pode deletar o seu próprio perfil.");
        }

        userRepository.delete(user);
    }

    @Transactional
    public Page<UserProfileResponseDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserProfileResponseDto::new);
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
    public User registerUserGoogle(User googleUser) {
        if (userRepository.findByEmail(googleUser.getEmail()).isPresent()) {
            return userRepository.findByEmail(googleUser.getEmail()).get();
        }
        googleUser.setRole(UserRole.USER);
        return userRepository.save(googleUser);
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

    private static void verifyToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new TokenInvalidExpection("Token inválido.");
        }
    }


    @Transactional
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
    }
}
