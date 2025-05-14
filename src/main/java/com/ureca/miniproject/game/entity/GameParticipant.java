package com.ureca.miniproject.game.entity;

import com.ureca.miniproject.common.BaseTimeEntity;
import com.ureca.miniproject.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameParticipant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private GameRoom gameRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ParticipantStatus status;

    @Enumerated(EnumType.STRING)
    private ParticipantRole role;

    @Column(nullable = false)
    private Boolean isAlive;

    @Builder
    private GameParticipant(GameRoom gameRoom, User user, ParticipantStatus status, ParticipantRole role, Boolean isAlive) {
        this.gameRoom = gameRoom;
        this.user = user;
        this.status = status;
        this.role = role;
        this.isAlive = isAlive;
    }
}
