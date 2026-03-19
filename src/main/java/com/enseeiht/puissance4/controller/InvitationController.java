package com.enseeiht.puissance4.controller;

import com.enseeiht.puissance4.entity.Invitation;
import com.enseeiht.puissance4.entity.User;
import com.enseeiht.puissance4.repository.UserRepository;
import com.enseeiht.puissance4.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;
    private final UserRepository userRepository;

    private User getUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping
    public ResponseEntity<Invitation> sendInvitation(
            @RequestBody Map<String, Long> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        User receiver = userRepository.findById(body.get("receiverId"))
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(
                invitationService.sendInvitation(getUser(userDetails), receiver));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<?> acceptInvitation(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                invitationService.acceptInvitation(id, getUser(userDetails)));
    }

    @PostMapping("/{id}/decline")
    public ResponseEntity<?> declineInvitation(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        invitationService.declineInvitation(id, getUser(userDetails));
        return ResponseEntity.ok(Map.of("message", "Invitation refusée"));
    }

    @GetMapping
    public ResponseEntity<List<Invitation>> getMyInvitations(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                invitationService.getMyInvitations(getUser(userDetails)));
    }
}
