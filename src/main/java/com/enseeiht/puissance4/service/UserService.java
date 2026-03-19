package com.enseeiht.puissance4.service;

import com.enseeiht.puissance4.entity.User;
import com.enseeiht.puissance4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getOnlinePlayers() {
        return userRepository.findByIsOnlineTrue();
    }

    public User setOnline(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setOnline(true);
        return userRepository.save(user);
    }

    public User setOffline(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setOnline(false);
        return userRepository.save(user);
    }
}
