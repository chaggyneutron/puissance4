package com.enseeiht.puissance4.service;

import com.enseeiht.puissance4.entity.Ranking;
import com.enseeiht.puissance4.entity.User;
import com.enseeiht.puissance4.repository.RankingRepository;
import com.enseeiht.puissance4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RankingRepository rankingRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User register(String username, String email, String password) {
        if (userRepository.existsByEmail(email))
            throw new RuntimeException("Email déjà utilisé");
        if (userRepository.existsByUsername(username))
            throw new RuntimeException("Username déjà pris");

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        userRepository.save(user);

        Ranking ranking = Ranking.builder().user(user).build();
        rankingRepository.save(ranking);

        return user;
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new RuntimeException("Mot de passe incorrect");
        return user;
    }

    public User getProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}
