package com.ureca.miniproject.game.entity;

import com.ureca.miniproject.common.BaseTimeEntity;
import com.ureca.miniproject.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User hostUser;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;

    @Column(nullable = false)
    private Integer maxPlayer;

    @Column(nullable = false)
    private Integer currentPlayer;

    @OneToMany(mappedBy = "gameRoom", cascade = CascadeType.ALL)
    private List<GameParticipant> participants = new ArrayList<>();

    public static GameRoom createGameRoom(User hostUser, String title, RoomStatus roomStatus, Integer maxPlayer, GameParticipant gameParticipant) {
        GameRoom gameRoom = new GameRoom();
        gameRoom.hostUser = hostUser;
        gameRoom.title = title;
        gameRoom.roomStatus = roomStatus;
        gameRoom.maxPlayer = maxPlayer;
        gameRoom.currentPlayer = 1;

        gameRoom.addGameParticipant(gameParticipant);
        return gameRoom;
    }

    public void addGameParticipant(GameParticipant gameParticipant) {
        gameParticipant.setGameRoom(this);
        participants.add(gameParticipant);
    }

    public void addCurrentPlayer() {
        this.currentPlayer++;
    }

    public void removeCurrentPlayer() {
        this.currentPlayer--;
    }
}
