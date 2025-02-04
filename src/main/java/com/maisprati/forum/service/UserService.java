package com.maisprati.forum.service;


import com.maisprati.forum.dto.SocialMediaDto;
import com.maisprati.forum.dto.request.UserRegisterDto;
import com.maisprati.forum.dto.request.UserUpdateDto;
import com.maisprati.forum.dto.response.UserProfileResponseDto;
import com.maisprati.forum.dto.response.UserRegisterResponseDto;
import com.maisprati.forum.model.User;
import com.maisprati.forum.model.UserSocialMidia;
import com.maisprati.forum.repository.TopicRepository;
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
        String username = tokenService.extractUsername(token);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        User userToken = userRepository.findByUserName(username);

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
        if (userUpdateDto.getSocialMedia() != null) {
            updateSocialMedia(user, userUpdateDto.getSocialMedia());
        }

        userRepository.save(user);
        return new UserProfileResponseDto(user);
    }

    private void updateSocialMedia(User user, List<SocialMediaDto> socialMediaDtos) {
        List<UserSocialMidia> existingSocialMedia = user.getUserSocialMidia();

        // Remover as redes sociais antigas que não estão mais no DTO
        List<UserSocialMidia> toRemove = existingSocialMedia.stream()
                .filter(existing -> socialMediaDtos.stream()
                        .noneMatch(dto -> isSocialMediaMatching(existing, dto)))
                .toList();
        existingSocialMedia.removeAll(toRemove);

        // Atualizar ou criar novas redes sociais
        for (SocialMediaDto dto : socialMediaDtos) {
            UserSocialMidia matchingSocialMedia = existingSocialMedia.stream()
                    .filter(existing -> isSocialMediaMatching(existing, dto))
                    .findFirst()
                    .orElse(null);

            if (matchingSocialMedia != null) {
                // Atualizar rede social existente
                matchingSocialMedia.setGitProfile(dto.getGitProfile());
                matchingSocialMedia.setDiscordProfile(dto.getDiscordProfile());
                matchingSocialMedia.setLinkedinProfile(dto.getLinkedinProfile());
                matchingSocialMedia.setInstagramProfile(dto.getInstagramProfile());
            } else {
                // Criar nova rede social
                UserSocialMidia newSocialMedia = UserSocialMidia.builder()
                        .gitProfile(dto.getGitProfile())
                        .discordProfile(dto.getDiscordProfile())
                        .linkedinProfile(dto.getLinkedinProfile())
                        .instagramProfile(dto.getInstagramProfile())
                        .user(user)
                        .build();
                existingSocialMedia.add(newSocialMedia);
            }
        }

        // Persistir as mudanças no banco
        userSocialMidiaRepository.saveAll(existingSocialMedia);
        userSocialMidiaRepository.deleteAll(toRemove); // Remover redes sociais obsoletas
    }
    private boolean isSocialMediaMatching(UserSocialMidia existing, SocialMediaDto dto) {
        return Objects.equals(existing.getGitProfile(), dto.getGitProfile()) &&
                Objects.equals(existing.getDiscordProfile(), dto.getDiscordProfile()) &&
                Objects.equals(existing.getLinkedinProfile(), dto.getLinkedinProfile()) &&
                Objects.equals(existing.getInstagramProfile(), dto.getInstagramProfile());
    }



    public void deleteUser(Long id, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = tokenService.extractUsername(token);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        User userToken = userRepository.findByUserName(username);

        if (!id.equals(userToken.getId())) {
            throw new SecurityException("Você só pode deletar o seu próprio perfil.");
        }

        userRepository.delete(user);
    }

    public List<UserProfileResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserProfileResponseDto::new).toList();
    }

    @Transactional
    public UserProfileResponseDto getUserById(Long id) {
        var user = userRepository.findById(id);
        return userRepository.findById(id)
                .map(UserProfileResponseDto::new).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado") );
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
        var user = userRepository.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado.");
        }
        return user;
    }
    public UserRegisterResponseDto registerUser(UserRegisterDto userDto) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new RuntimeException("Senhas não correspondem.") ;
        }
        if ( userRepository.findByUserName(userDto.getEmail()) != null ) {
            throw new RuntimeException("Usuario inválido.");
        }


        var userSaved = userRepository.save(userDto.createUser(userDto, passwordEncoder));

        return new UserRegisterResponseDto(userSaved);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id); // delega para o repositório JPA
    }

}