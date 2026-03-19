package com.enseeiht.puissance4.service;

import com.enseeiht.puissance4.entity.Game;
import com.enseeiht.puissance4.entity.Invitation;
import com.enseeiht.puissance4.entity.User;
import com.enseeiht.puissance4.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final GameService gameService;

    public Invitation sendInvitation(User sender, User receiver) {
        if (sender.getId().equals(receiver.getId()))
            throw new RuntimeException("Vous ne pouvez pas vous inviter vous-même");
        Invitation invitation = Invitation.builder()
                .sender(sender).receiver(receiver).build();
        return invitationRepository.save(invitation);
    }

    @Transactional
    public Game acceptInvitation(Long invitationId, User receiver) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitation non trouvée"));
        if (!invitation.getReceiver().getId().equals(receiver.getId()))
            throw new RuntimeException("Non autorisé");

        invitation.setStatus(Invitation.Status.ACCEPTED);
        invitationRepository.save(invitation);

        Game game = gameService.createGame(invitation.getSender());
        return gameService.joinGame(game.getId(), receiver);
    }

    public void declineInvitation(Long invitationId, User receiver) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitation non trouvée"));
        invitation.setStatus(Invitation.Status.DECLINED);
        invitationRepository.save(invitation);
    }

    public List<Invitation> getMyInvitations(User receiver) {
        return invitationRepository.findByReceiverAndStatus(
                receiver, Invitation.Status.PENDING);
    }
}
