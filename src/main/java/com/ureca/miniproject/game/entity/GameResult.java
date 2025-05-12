package com.ureca.miniproject.game.entity;

import com.ureca.miniproject.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private GameRoom gameRoom;

    @Enumerated(EnumType.STRING)
    private GameResultStatus status;

    @Builder
    private GameResult(User user, GameRoom gameRoom, GameResultStatus status) {
        this.user = user;
        this.gameRoom = gameRoom;
        this.status = status;
    }
}
