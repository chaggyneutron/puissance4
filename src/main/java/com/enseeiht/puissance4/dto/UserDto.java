package com.enseeiht.puissance4.dto;

import com.enseeiht.puissance4.entity.User;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private int wins;
    private int losses;
    private boolean isOnline;
    private String role;
    private LocalDateTime createdAt;

    public static UserDto from(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setWins(user.getWins());
        dto.setLosses(user.getLosses());
        dto.setOnline(user.isOnline());
        dto.setRole(user.getRole().name());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
