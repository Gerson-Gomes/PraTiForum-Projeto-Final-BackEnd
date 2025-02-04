package com.maisprati.forum.service;


import com.maisprati.forum.dto.SocialMediaDto;
import com.maisprati.forum.dto.request.UserRegisterDto;
import com.maisprati.forum.dto.request.UserUpdateDto;
import com.maisprati.forum.dto.response.UserProfileResponseDto;
import com.maisprati.forum.dto.response.UserRegisterResponseDto;
import com.maisprati.forum.model.User;
import com.maisprati.forum.model.UserSocialMidia;
import com.maisprati.forum.repository.UserRepository;
import com.maisprati.forum.repository.UserSocialMidiaRepository;
import com.maisprati.forum.service.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSocialMidiaRepository userSocialMidiaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Transactional
    public UserProfileResponseDto editUser(Long id, UserUpdateDto userUpdateDto, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        verifyToken(token);

        String username = tokenService.extractUsername(token);

        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        User userToken = userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));;

        if (!id.equals(userToken.getId())) {
            throw new SecurityException("Você só pode editar o seu próprio perfil.");
        }

        // Atualizar informações básicas do usuário
        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());
        user.setEmail(userUpdateDto.getEmail());
        user.setUserName(userUpdateDto.getEmail());
        user.setDescription(userUpdateDto.getDescription());
        user.setBirthDate(userUpdateDto.getBirthDate());
        user.setLastEditionDate(LocalDateTime.now());

        // Atualizar ou adicionar redes sociais
        if (userUpdateDto.getSocialMedia() != null && !userUpdateDto.getSocialMedia().isEmpty()) {
            updateSocialMedia(user, userUpdateDto.getSocialMedia());
        }

        return new UserProfileResponseDto(userRepository.save(user));
    }

    public void deleteUser(Long id, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        verifyToken(token);
        String username = tokenService.extractUsername(token);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        User userToken = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));;

        if (!id.equals(userToken.getId())) {
            throw new SecurityException("Você só pode deletar o seu próprio perfil.");
        }

        userRepository.delete(user);
    }

    public List<UserProfileResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserProfileResponseDto::new).toList();
    }

    public UserProfileResponseDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserProfileResponseDto::new).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado") );
    }
    public UserProfileResponseDto getUserByUsername(String username) {
        return userRepository.findByUserName(username)
                .map(UserProfileResponseDto::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    public UserRegisterResponseDto registerUser(UserRegisterDto userDto) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new RuntimeException("Senhas não correspondem.") ;
        }
        if (userRepository.findByUserName(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Usuario inválido.");
        }

        return new UserRegisterResponseDto(userRepository.save(userDto.createUser(userDto, passwordEncoder)));
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id); // delega para o repositório JPA
    }


    private static void verifyToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new SecurityException("Token inválido.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username).orElseThrow(() ->
                new UsernameNotFoundException("Usuário não encontrado"));
    }

    private boolean isSocialMediaMatching(UserSocialMidia existing, SocialMediaDto dto) {
        return Objects.equals(existing.getGitProfile(), dto.getGitProfile()) &&
                Objects.equals(existing.getDiscordProfile(), dto.getDiscordProfile()) &&
                Objects.equals(existing.getLinkedinProfile(), dto.getLinkedinProfile()) &&
                Objects.equals(existing.getInstagramProfile(), dto.getInstagramProfile());
    }

    @Transactional
    //BUG ENCONTRADO QUANDO USUÁRIO É ATUALIZADO.
    protected void updateSocialMedia(User user, List<SocialMediaDto> socialMediaDtos) {
        List<UserSocialMidia> existingSocialMedia = new ArrayList<>(user.getUserSocialMidia());

        List<UserSocialMidia> toRemove = existingSocialMedia.stream()
                .filter(existing -> socialMediaDtos.stream().noneMatch(dto -> isSocialMediaMatching(existing, dto)))
                .toList();

        List<UserSocialMidia> updatedSocialMedia = new ArrayList<>(existingSocialMedia);
        updatedSocialMedia.removeAll(toRemove);

        for (SocialMediaDto dto : socialMediaDtos) {
            UserSocialMidia matchingSocialMedia = updatedSocialMedia.stream()
                    .filter(existing -> isSocialMediaMatching(existing, dto))
                    .findFirst()
                    .orElse(null);

            if (matchingSocialMedia != null) {
                matchingSocialMedia.setGitProfile(dto.getGitProfile());
                matchingSocialMedia.setDiscordProfile(dto.getDiscordProfile());
                matchingSocialMedia.setLinkedinProfile(dto.getLinkedinProfile());
                matchingSocialMedia.setInstagramProfile(dto.getInstagramProfile());
            } else {
                UserSocialMidia newSocialMedia = UserSocialMidia.builder()
                        .gitProfile(dto.getGitProfile())
                        .discordProfile(dto.getDiscordProfile())
                        .linkedinProfile(dto.getLinkedinProfile())
                        .instagramProfile(dto.getInstagramProfile())
                        .user(user)
                        .build();
                updatedSocialMedia.add(newSocialMedia);
            }
        }

        user.setUserSocialMidia(updatedSocialMedia);

        // Persistindo as mudanças no banco
        userSocialMidiaRepository.saveAll(updatedSocialMedia);
        userSocialMidiaRepository.deleteAll(toRemove);
    }
}