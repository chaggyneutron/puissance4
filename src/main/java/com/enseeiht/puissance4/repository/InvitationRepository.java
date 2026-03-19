package com.enseeiht.puissance4.repository;

import com.enseeiht.puissance4.entity.Invitation;
import com.enseeiht.puissance4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByReceiverAndStatus(User receiver, Invitation.Status status);
    List<Invitation> findBySender(User sender);
}
